/*
 * Copyright (c) 2015 The Android Open Source Project
 * Copyright (C) 2015 Samsung LSI
 * Copyright (c) 2008-2009, Motorola, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - Neither the name of the Motorola, Inc. nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package javax.obex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

import android.util.Log;

/**
 * This class implements the <code>Operation</code> interface. It will read and
 * write data via puts and gets.
 * @hide
 */
public final class ClientOperation implements Operation, BaseStream {

    private static final String TAG = "ClientOperation";

    private static final boolean V = ObexHelper.VDBG;

    private ClientSession mParent;

    private boolean mInputOpen;

    private PrivateInputStream mPrivateInput;

    private boolean mPrivateInputOpen;

    private PrivateOutputStream mPrivateOutput;

    private boolean mPrivateOutputOpen;

    private String mExceptionMessage;

    private int mMaxPacketSize;

    private boolean mOperationDone;

    private boolean mGetOperation;

    private boolean mGetFinalFlag;

    private HeaderSet mRequestHeader;

    private HeaderSet mReplyHeader;

    private boolean mEndOfBodySent;

    private boolean mSendBodyHeader = true;
    // A latch - when triggered, there is not way back ;-)
    private boolean mSrmActive = false;

    // Assume SRM disabled - until support is confirmed
    // by the server
    private boolean mSrmEnabled = false;
    // keep waiting until final-bit is received in request
    // to handle the case where the SRM enable header is in
    // a different OBEX packet than the SRMP header.
    private boolean mSrmWaitingForRemote = true;


    /**
     * Creates new OperationImpl to read and write data to a server
     * @param maxSize the maximum packet size
     * @param p the parent to this object
     * @param type <code>true</code> if this is a get request;
     *        <code>false</code. if this is a put request
     * @param header the header to set in the initial request
     * @throws IOException if the an IO error occurred
     */
    public ClientOperation(int maxSize, ClientSession p, HeaderSet header, boolean type)
            throws IOException {

        mParent = p;
        mEndOfBodySent = false;
        mInputOpen = true;
        mOperationDone = false;
        mMaxPacketSize = maxSize;
        mGetOperation = type;
        mGetFinalFlag = false;

        mPrivateInputOpen = false;
        mPrivateOutputOpen = false;
        mPrivateInput = null;
        mPrivateOutput = null;

        mReplyHeader = new HeaderSet();

        mRequestHeader = new HeaderSet();

        int[] headerList = header.getHeaderList();

        if (headerList != null) {

            for (int i = 0; i < headerList.length; i++) {
                mRequestHeader.setHeader(headerList[i], header.getHeader(headerList[i]));
            }
        }

        if ((header).mAuthChall != null) {
            mRequestHeader.mAuthChall = new byte[(header).mAuthChall.length];
            System.arraycopy((header).mAuthChall, 0, mRequestHeader.mAuthChall, 0,
                    (header).mAuthChall.length);
        }

        if ((header).mAuthResp != null) {
            mRequestHeader.mAuthResp = new byte[(header).mAuthResp.length];
            System.arraycopy((header).mAuthResp, 0, mRequestHeader.mAuthResp, 0,
                    (header).mAuthResp.length);

        }

        if ((header).mConnectionID != null) {
            mRequestHeader.mConnectionID = new byte[4];
            System.arraycopy((header).mConnectionID, 0, mRequestHeader.mConnectionID, 0,
                    4);

        }
    }

    /**
     * Allows to set flag which will force GET to be always sent as single packet request with
     * final flag set. This is to improve compatibility with some profiles, i.e. PBAP which
     * require requests to be sent this way.
     */
    public void setGetFinalFlag(boolean flag) {
        mGetFinalFlag = flag;
    }

    /**
     * Sends an ABORT message to the server. By calling this method, the
     * corresponding input and output streams will be closed along with this
     * object.
     * @throws IOException if the transaction has already ended or if an OBEX
     *         server called this method
     */
    public synchronized void abort() throws IOException {
        ensureOpen();
        //no compatible with sun-ri
        if ((mOperationDone) && (mReplyHeader.responseCode != ResponseCodes.OBEX_HTTP_CONTINUE)) {
            throw new IOException("Operation has already ended");
        }

        mExceptionMessage = "Operation aborted";
        if ((!mOperationDone) && (mReplyHeader.responseCode == ResponseCodes.OBEX_HTTP_CONTINUE)) {
            mOperationDone = true;
            /*
             * Since we are not sending any headers or returning any headers then
             * we just need to write and read the same bytes
             */
            mParent.sendRequest(ObexHelper.OBEX_OPCODE_ABORT, null, mReplyHeader, null, false);

            if (mReplyHeader.responseCode != ResponseCodes.OBEX_HTTP_OK) {
                throw new IOException("Invalid response code from server");
            }

            mExceptionMessage = null;
        }

        close();
    }

    /**
     * Retrieves the response code retrieved from the server. Response codes are
     * defined in the <code>ResponseCodes</code> interface.
     * @return the response code retrieved from the server
     * @throws IOException if an error occurred in the transport layer during
     *         the transaction; if this method is called on a
     *         <code>HeaderSet</code> object created by calling
     *         <code>createHeaderSet</code> in a <code>ClientSession</code>
     *         object
     */
    public synchronized int getResponseCode() throws IOException {
        //avoid dup validateConnection
        if ((mReplyHeader.responseCode == -1)
                || (mReplyHeader.responseCode == ResponseCodes.OBEX_HTTP_CONTINUE)) {
            validateConnection();
        }

        return mReplyHeader.responseCode;
    }

    /**
     * This method will always return <code>null</code>
     * @return <code>null</code>
     */
    public String getEncoding() {
        return null;
    }

    /**
     * Returns the type of content that the resource connected to is providing.
     * E.g. if the connection is via HTTP, then the value of the content-type
     * header field is returned.
     * @return the content type of the resource that the URL references, or
     *         <code>null</code> if not known
     */
    public String getType() {
        try {
            return (String)mReplyHeader.getHeader(HeaderSet.TYPE);
        } catch (IOException e) {
            if(V) Log.d(TAG, "Exception occured - returning null",e);
            return null;
        }
    }

    /**
     * Returns the length of the content which is being provided. E.g. if the
     * connection is via HTTP, then the value of the content-length header field
     * is returned.
     * @return the content length of the resource that this connection's URL
     *         references, or -1 if the content length is not known
     */
    public long getLength() {
        try {
            Long temp = (Long)mReplyHeader.getHeader(HeaderSet.LENGTH);

            if (temp == null) {
                return -1;
            } else {
                return temp.longValue();
            }
        } catch (IOException e) {
            if(V) Log.d(TAG,"Exception occured - returning -1",e);
            return -1;
        }
    }

    /**
     * Open and return an input stream for a connection.
     * @return an input stream
     * @throws IOException if an I/O error occurs
     */
    public InputStream openInputStream() throws IOException {

        ensureOpen();

        if (mPrivateInputOpen)
            throw new IOException("no more input streams available");
        if (mGetOperation) {
            // send the GET request here
            validateConnection();
        } else {
            if (mPrivateInput == null) {
                mPrivateInput = new PrivateInputStream(this);
            }
        }

        mPrivateInputOpen = true;

        return mPrivateInput;
    }

    /**
     * Open and return a data input stream for a connection.
     * @return an input stream
     * @throws IOException if an I/O error occurs
     */
    public DataInputStream openDataInputStream() throws IOException {
        return new DataInputStream(openInputStream());
    }

    /**
     * Open and return an output stream for a connection.
     * @return an output stream
     * @throws IOException if an I/O error occurs
     */
    public OutputStream openOutputStream() throws IOException {

        ensureOpen();
        ensureNotDone();

        if (mPrivateOutputOpen)
            throw new IOException("no more output streams available");

        if (mPrivateOutput == null) {
            // there are 3 bytes operation headers and 3 bytes body headers //
            mPrivateOutput = new PrivateOutputStream(this, getMaxPacketSize());
        }

        mPrivateOutputOpen = true;

        return mPrivateOutput;
    }

    public int getMaxPacketSize() {
        return mMaxPacketSize - 6 - getHeaderLength();
    }

    public int getHeaderLength() {
        // OPP may need it
        byte[] headerArray = ObexHelper.createHeader(mRequestHeader, false);
        return headerArray.length;
    }

    /**
     * Open and return a data output stream for a connection.
     * @return an output stream
     * @throws IOException if an I/O error occurs
     */
    public DataOutputStream openDataOutputStream() throws IOException {
        return new DataOutputStream(openOutputStream());
    }

    /**
     * Closes the connection and ends the transaction
     * @throws IOException if the operation has already ended or is closed
     */
    public void close() throws IOException {
        mInputOpen = false;
        mPrivateInputOpen = false;
        mPrivateOutputOpen = false;
        mParent.setRequestInactive();
    }

    /**
     * Returns the headers that have been received during the operation.
     * Modifying the object returned has no effect on the headers that are sent
     * or retrieved.
     * @return the headers received during this <code>Operation</code>
     * @throws IOException if this <code>Operation</code> has been closed
     */
    public HeaderSet getReceivedHeader() throws IOException {
        ensureOpen();

        return mReplyHeader;
    }

    /**
     * Specifies the headers that should be sent in the next OBEX message that
     * is sent.
     * @param headers the headers to send in the next message
     * @throws IOException if this <code>Operation</code> has been closed or the
     *         transaction has ended and no further messages will be exchanged
     * @throws IllegalArgumentException if <code>headers</code> was not created
     *         by a call to <code>ServerRequestHandler.createHeaderSet()</code>
     * @throws NullPointerException if <code>headers</code> is <code>null</code>
     */
    public void sendHeaders(HeaderSet headers) throws IOException {
        ensureOpen();
        if (mOperationDone) {
            throw new IOException("Operation has already exchanged all data");
        }

        if (headers == null) {
            throw new IOException("Headers may not be null");
        }

        int[] headerList = headers.getHeaderList();
        if (headerList != null) {
            for (int i = 0; i < headerList.length; i++) {
                mRequestHeader.setHeader(headerList[i], headers.getHeader(headerList[i]));
            }
        }
    }

    /**
     * Verifies that additional information may be sent. In other words, the
     * operation is not done.
     * @throws IOException if the operation is completed
     */
    public void ensureNotDone() throws IOException {
        if (mOperationDone) {
            throw new IOException("Operation has completed");
        }
    }

    /**
     * Verifies that the connection is open and no exceptions should be thrown.
     * @throws IOException if an exception needs to be thrown
     */
    public void ensureOpen() throws IOException {
        mParent.ensureOpen();

        if (mExceptionMessage != null) {
            throw new IOException(mExceptionMessage);
        }
        if (!mInputOpen) {
            throw new IOException("Operation has already ended");
        }
    }

    /**
     * Verifies that the connection is open and the proper data has been read.
     * @throws IOException if an IO error occurs
     */
    private void validateConnection() throws IOException {
        ensureOpen();

        // to sure only one privateInput object exist.
        if (mPrivateInput == null) {
            startProcessing();
        }
    }

    /**
     * Sends a request to the client of the specified type.
     * This function will enable SRM and set SRM active if the server
     * response allows this.
     * @param opCode the request code to send to the client
     * @return <code>true</code> if there is more data to send;
     *         <code>false</code> if there is no more data to send
     * @throws IOException if an IO error occurs
     */
    private boolean sendRequest(int opCode) throws IOException {
        boolean returnValue = false;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int bodyLength = -1;
        byte[] headerArray = ObexHelper.createHeader(mRequestHeader, true);
        if (mPrivateOutput != null) {
            bodyLength = mPrivateOutput.size();
        }

        /*
         * Determine if there is space to add a body request.  At present
         * this method checks to see if there is room for at least a 17
         * byte body header.  This number needs to be at least 6 so that
         * there is room for the header ID and length and the reply ID and
         * length, but it is a waste of resources if we can't send much of
         * the body.
         */
        final int MINIMUM_BODY_LENGTH = 3;
        if ((ObexHelper.BASE_PACKET_LENGTH + headerArray.length + MINIMUM_BODY_LENGTH)
                > mMaxPacketSize) {
            int end = 0;
            int start = 0;
            // split & send the headerArray in multiple packets.

            while (end != headerArray.length) {
                //split the headerArray

                end = ObexHelper.findHeaderEnd(headerArray, start, mMaxPacketSize
                        - ObexHelper.BASE_PACKET_LENGTH);
                // can not split
                if (end == -1) {
                    mOperationDone = true;
                    abort();
                    mExceptionMessage = "Header larger then can be sent in a packet";
                    mInputOpen = false;

                    if (mPrivateInput != null) {
                        mPrivateInput.close();
                    }

                    if (mPrivateOutput != null) {
                        mPrivateOutput.close();
                    }
                    throw new IOException("OBEX Packet exceeds max packet size");
                }

                byte[] sendHeader = new byte[end - start];
                System.arraycopy(headerArray, start, sendHeader, 0, sendHeader.length);
                if (!mParent.sendRequest(opCode, sendHeader, mReplyHeader, mPrivateInput, false)) {
                    return false;
                }

                if (mReplyHeader.responseCode != ResponseCodes.OBEX_HTTP_CONTINUE) {
                    return false;
                }

                start = end;
            }

            // Enable SRM if it should be enabled
            checkForSrm();

            if (bodyLength > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            /* All headers will fit into a single package */
            if(mSendBodyHeader == false) {
                /* As we are not to send any body data, set the FINAL_BIT */
                opCode |= ObexHelper.OBEX_OPCODE_FINAL_BIT_MASK;
            }
            out.write(headerArray);
        }

        if (bodyLength > 0) {
            /*
             * Determine if we can send the whole body or just part of
             * the body.  Remember that there is the 3 bytes for the
             * response message and 3 bytes for the header ID and length
             */
            if (bodyLength > (mMaxPacketSize - headerArray.length - 6)) {
                returnValue = true;

                bodyLength = mMaxPacketSize - headerArray.length - 6;
            }

            byte[] body = mPrivateOutput.readBytes(bodyLength);

            /*
             * Since this is a put request if the final bit is set or
             * the output stream is closed we need to send the 0x49
             * (End of Body) otherwise, we need to send 0x48 (Body)
             */
            if ((mPrivateOutput.isClosed()) && (!returnValue) && (!mEndOfBodySent)
                    && ((opCode & ObexHelper.OBEX_OPCODE_FINAL_BIT_MASK) != 0)) {
                out.write(HeaderSet.END_OF_BODY);
                mEndOfBodySent = true;
            } else {
                out.write(HeaderSet.BODY);
            }

            bodyLength += 3;
            out.write((byte)(bodyLength >> 8));
            out.write((byte)bodyLength);

            if (body != null) {
                out.write(body);
            }
        }

        if (mPrivateOutputOpen && bodyLength <= 0 && !mEndOfBodySent) {
            // only 0x82 or 0x83 can send 0x49
            if ((opCode & ObexHelper.OBEX_OPCODE_FINAL_BIT_MASK) == 0) {
                out.write(HeaderSet.BODY);
            } else {
                out.write(HeaderSet.END_OF_BODY);
                mEndOfBodySent = true;
            }

            bodyLength = 3;
            out.write((byte)(bodyLength >> 8));
            out.write((byte)bodyLength);
        }

        if (out.size() == 0) {
            if (!mParent.sendRequest(opCode, null, mReplyHeader, mPrivateInput, mSrmActive)) {
                return false;
            }
            // Enable SRM if it should be enabled
            checkForSrm();
            return returnValue;
        }
        if ((out.size() > 0)
                && (!mParent.sendRequest(opCode, out.toByteArray(),
                        mReplyHeader, mPrivateInput, mSrmActive))) {
            return false;
        }
        // Enable SRM if it should be enabled
        checkForSrm();

        // send all of the output data in 0x48,
        // send 0x49 with empty body
        if ((mPrivateOutput != null) && (mPrivateOutput.size() > 0))
            returnValue = true;

        return returnValue;
    }

    private void checkForSrm() throws IOException {
        Byte srmMode = (Byte)mReplyHeader.getHeader(HeaderSet.SINGLE_RESPONSE_MODE);
        if(mParent.isSrmSupported() == true && srmMode != null
                && srmMode == ObexHelper.OBEX_SRM_ENABLE) {
            mSrmEnabled = true;
        }
        /**
         * Call this only when a complete obex packet have been received.
         * (This is not optimal, but the current design is not really suited to
         * the way SRM is specified.)
         * The BT usage of SRM is not really safe - it assumes that the SRMP will fit
         * into every OBEX packet, hence if another header occupies the entire packet,
         * the scheme will not work - unlikely though.
         */
        if(mSrmEnabled) {
            mSrmWaitingForRemote = false;
            Byte srmp = (Byte)mReplyHeader.getHeader(HeaderSet.SINGLE_RESPONSE_MODE_PARAMETER);
            if(srmp != null && srmp == ObexHelper.OBEX_SRMP_WAIT) {
                mSrmWaitingForRemote = true;
                // Clear the wait header, as the absence of the header in the next packet
                // indicates don't wait anymore.
                mReplyHeader.setHeader(HeaderSet.SINGLE_RESPONSE_MODE_PARAMETER, null);
            }
        }
        if((mSrmWaitingForRemote == false) && (mSrmEnabled == true)) {
            mSrmActive = true;
        }
    }

    /**
     * This method starts the processing thread results. It will send the
     * initial request. If the response takes more then one packet, a thread
     * will be started to handle additional requests
     * @throws IOException if an IO error occurs
     */
    private synchronized void startProcessing() throws IOException {

        if (mPrivateInput == null) {
            mPrivateInput = new PrivateInputStream(this);
        }
        boolean more = true;

        if (mGetOperation) {
            if (!mOperationDone) {
                mReplyHeader.responseCode = ResponseCodes.OBEX_HTTP_CONTINUE;
                while ((more) && (mReplyHeader.responseCode == ResponseCodes.OBEX_HTTP_CONTINUE)) {
                    more = sendRequest(ObexHelper.OBEX_OPCODE_GET);
                }
                // For GET we need to loop until all headers have been sent,
                // And then we wait for the first continue package with the
                // reply.
                if (mReplyHeader.responseCode == ResponseCodes.OBEX_HTTP_CONTINUE) {
                    mParent.sendRequest(ObexHelper.OBEX_OPCODE_GET_FINAL,
                            null, mReplyHeader, mPrivateInput, mSrmActive);
                }
                if (mReplyHeader.responseCode != ResponseCodes.OBEX_HTTP_CONTINUE) {
                    mOperationDone = true;
                } else {
                    checkForSrm();
                }
            }
        } else {
            // PUT operation
            if (!mOperationDone) {
                mReplyHeader.responseCode = ResponseCodes.OBEX_HTTP_CONTINUE;
                while ((more) && (mReplyHeader.responseCode == ResponseCodes.OBEX_HTTP_CONTINUE)) {
                    more = sendRequest(ObexHelper.OBEX_OPCODE_PUT);
                }
            }

            if (mReplyHeader.responseCode == ResponseCodes.OBEX_HTTP_CONTINUE) {
                mParent.sendRequest(ObexHelper.OBEX_OPCODE_PUT_FINAL,
                        null, mReplyHeader, mPrivateInput, mSrmActive);
            }

            if (mReplyHeader.responseCode != ResponseCodes.OBEX_HTTP_CONTINUE) {
                mOperationDone = true;
            }
        }
    }

    /**
     * Continues the operation since there is no data to read.
     * @param sendEmpty <code>true</code> if the operation should send an empty
     *        packet or not send anything if there is no data to send
     * @param inStream <code>true</code> if the stream is input stream or is
     *        output stream
     * @throws IOException if an IO error occurs
     */
    public synchronized boolean continueOperation(boolean sendEmpty, boolean inStream)
            throws IOException {

        // One path to the first put operation - the other one does not need to
        // handle SRM, as all will fit into one packet.

        if (mGetOperation) {
            if ((inStream) && (!mOperationDone)) {
                // to deal with inputstream in get operation
                mParent.sendRequest(ObexHelper.OBEX_OPCODE_GET_FINAL,
                        null, mReplyHeader, mPrivateInput, mSrmActive);
                /*
                  * Determine if that was not the last packet in the operation
                  */
                if (mReplyHeader.responseCode != ResponseCodes.OBEX_HTTP_CONTINUE) {
                    mOperationDone = true;
                } else {
                    checkForSrm();
                }

                return true;

            } else if ((!inStream) && (!mOperationDone)) {
                // to deal with outputstream in get operation

                if (mPrivateInput == null) {
                    mPrivateInput = new PrivateInputStream(this);
                }
                sendRequest(ObexHelper.OBEX_OPCODE_GET);
                return true;

            } else if (mOperationDone) {
                return false;
            }

        } else {
            // PUT operation
            if ((!inStream) && (!mOperationDone)) {
                // to deal with outputstream in put operation
                if (mReplyHeader.responseCode == -1) {
                    mReplyHeader.responseCode = ResponseCodes.OBEX_HTTP_CONTINUE;
                }
                sendRequest(ObexHelper.OBEX_OPCODE_PUT);
                return true;
            } else if ((inStream) && (!mOperationDone)) {
                // How to deal with inputstream  in put operation ?
                return false;

            } else if (mOperationDone) {
                return false;
            }

        }
        return false;
    }

    /**
     * Called when the output or input stream is closed.
     * @param inStream <code>true</code> if the input stream is closed;
     *        <code>false</code> if the output stream is closed
     * @throws IOException if an IO error occurs
     */
    public void streamClosed(boolean inStream) throws IOException {
        if (!mGetOperation) {
            if ((!inStream) && (!mOperationDone)) {
                // to deal with outputstream in put operation

                boolean more = true;

                if ((mPrivateOutput != null) && (mPrivateOutput.size() <= 0)) {
                    byte[] headerArray = ObexHelper.createHeader(mRequestHeader, false);
                    if (headerArray.length <= 0)
                        more = false;
                }
                // If have not sent any data so send  all now
                if (mReplyHeader.responseCode == -1) {
                    mReplyHeader.responseCode = ResponseCodes.OBEX_HTTP_CONTINUE;
                }

                while ((more) && (mReplyHeader.responseCode == ResponseCodes.OBEX_HTTP_CONTINUE)) {
                    more = sendRequest(ObexHelper.OBEX_OPCODE_PUT);
                }

                /*
                 * According to the IrOBEX specification, after the final put, you
                 * only have a single reply to send.  so we don't need the while
                 * loop.
                 */
                while (mReplyHeader.responseCode == ResponseCodes.OBEX_HTTP_CONTINUE) {

                    sendRequest(ObexHelper.OBEX_OPCODE_PUT_FINAL);
                }
                mOperationDone = true;
            } else if ((inStream) && (mOperationDone)) {
                // how to deal with input stream in put stream ?
                mOperationDone = true;
            }
        } else {
            if ((inStream) && (!mOperationDone)) {

                // to deal with inputstream in get operation
                // Have not sent any data so send it all now

                if (mReplyHeader.responseCode == -1) {
                    mReplyHeader.responseCode = ResponseCodes.OBEX_HTTP_CONTINUE;
                }

                while (mReplyHeader.responseCode == ResponseCodes.OBEX_HTTP_CONTINUE && !mOperationDone) {
                    if (!sendRequest(ObexHelper.OBEX_OPCODE_GET_FINAL)) {
                        break;
                    }
                }
                while (mReplyHeader.responseCode == ResponseCodes.OBEX_HTTP_CONTINUE && !mOperationDone) {
                    mParent.sendRequest(ObexHelper.OBEX_OPCODE_GET_FINAL, null,
                            mReplyHeader, mPrivateInput, false);
                    // Regardless of the SRM state, wait for the response.
                }
                mOperationDone = true;
            } else if ((!inStream) && (!mOperationDone)) {
                // to deal with outputstream in get operation
                // part of the data may have been sent in continueOperation.

                boolean more = true;

                if ((mPrivateOutput != null) && (mPrivateOutput.size() <= 0)) {
                    byte[] headerArray = ObexHelper.createHeader(mRequestHeader, false);
                    if (headerArray.length <= 0)
                        more = false;
                }

                if (mPrivateInput == null) {
                    mPrivateInput = new PrivateInputStream(this);
                }
                if ((mPrivateOutput != null) && (mPrivateOutput.size() <= 0))
                    more = false;

                mReplyHeader.responseCode = ResponseCodes.OBEX_HTTP_CONTINUE;
                while ((more) && (mReplyHeader.responseCode == ResponseCodes.OBEX_HTTP_CONTINUE)) {
                    more = sendRequest(ObexHelper.OBEX_OPCODE_GET);
                }
                sendRequest(ObexHelper.OBEX_OPCODE_GET_FINAL);
                //                parent.sendRequest(0x83, null, replyHeaders, privateInput);
                if (mReplyHeader.responseCode != ResponseCodes.OBEX_HTTP_CONTINUE) {
                    mOperationDone = true;
                }
            }
        }
    }

    public void noBodyHeader(){
        mSendBodyHeader = false;
    }
}

--------------------

/*
 * Copyright (c) 2003-2009 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jme.scene;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.bounding.BoundingVolume;
import com.jme.intersection.CollisionResults;
import com.jme.intersection.PickResults;
import com.jme.math.Ray;
import com.jme.renderer.Renderer;
import com.jme.scene.state.RenderState;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.Savable;

/**
 * <code>Node</code> defines an internal node of a scene graph. The internal
 * node maintains a collection of children and handles merging said children
 * into a single bound to allow for very fast culling of multiple nodes. Node
 * allows for any number of children to be attached.
 * 
 * @author Mark Powell
 * @author Gregg Patton
 * @author Joshua Slack
 */
public class Node extends Spatial implements Serializable, Savable {
    private static final Logger logger = Logger.getLogger(Node.class.getName());

    private static final long serialVersionUID = 1L;

    /** This node's children. */
    protected List<Spatial> children;

    /**
     * Default constructor.
     */
    public Node() {
        logger.info("Node created.");
    }

    /**
     * Constructor instantiates a new <code>Node</code> with a default empty
     * list for containing children.
     * 
     * @param name
     *            the name of the scene element. This is required for
     *            identification and comparision purposes.
     */
    public Node(String name) {
        super(name);
        setCollisionMask(-1);
        // Newly constitued (not reconsituted) Nodes should allow for maximum
        // collision nesting.  If anything other than -1, the node would block
        // collisions for all descendant Spatials.
        logger.info("Node created.");
    }

    /**
     * 
     * <code>getQuantity</code> returns the number of children this node
     * maintains.
     * 
     * @return the number of children this node maintains.
     */
    public int getQuantity() {
        if(children == null) {
            return 0;
        } 
            
        return children.size();        
    }
    
    /**
     * <code>getTriangleCount</code> returns the number of triangles contained
     * in all sub-branches of this node that contain geometry.
     * @return the triangle count of this branch.
     */
    @Override
    public int getTriangleCount() {
        int count = 0;
        if(children != null) {
            for(int i = 0; i < children.size(); i++) {
                count += children.get(i).getTriangleCount();
            }
        }
        
        return count;
    }
    
    /**
     * <code>getVertexCount</code> returns the number of vertices contained
     * in all sub-branches of this node that contain geometry.
     * @return the vertex count of this branch.
     */
    @Override
    public int getVertexCount() {
        int count = 0;
        if(children != null) {
            for(int i = 0; i < children.size(); i++) {
               count += children.get(i).getVertexCount();
            }
        }
        
        return count;
    }

    /**
     * 
     * <code>attachChild</code> attaches a child to this node. This node
     * becomes the child's parent. The current number of children maintained is
     * returned.
     * <br>
     * If the child already had a parent it is detached from that former parent.
     * 
     * @param child
     *            the child to attach to this node.
     * @return the number of children maintained by this node.
     */
    public int attachChild(Spatial child) {
        if (child != null) {
            if (child.getParent() != this) {
                if (child.getParent() != null) {
                    child.getParent().detachChild(child);
                }
                child.setParent(this);
                if(children == null) {
                    children = Collections.synchronizedList(new ArrayList<Spatial>(1));  
                }
                children.add(child);
                logger.log(Level.INFO, "Child \"{0}\" attached to this node \"{1}\"", 
                            new String[] {child.getName(), getName()});
            }
        }
        
        if (children == null) return 0;
        return children.size();
    }
    
    /**
     * 
     * <code>attachChildAt</code> attaches a child to this node at an index. This node
     * becomes the child's parent. The current number of children maintained is
     * returned.
     * <br>
     * If the child already had a parent it is detached from that former parent.
     * 
     * @param child
     *            the child to attach to this node.
     * @return the number of children maintained by this node.
     */
    public int attachChildAt(Spatial child, int index) {
        if (child != null) {
            if (child.getParent() != this) {
                if (child.getParent() != null) {
                    child.getParent().detachChild(child);
                }
                child.setParent(this);
                if(children == null) {
                    children = Collections.synchronizedList(new ArrayList<Spatial>(1));  
                }
                children.add(index, child);
                logger.log(Level.INFO, "Child \"{0}\" attached to this node \"{1}\"", 
                        new String[] {child.getName(), getName()});
            }
        }

        if (children == null) return 0;
        return children.size();
    }

    /**
     * <code>detachChild</code> removes a given child from the node's list.
     * This child will no longer be maintained.
     * 
     * @param child
     *            the child to remove.
     * @return the index the child was at. -1 if the child was not in the list.
     */
    public int detachChild(Spatial child) {
        if(children == null) {
            return -1;
        }
        if (child == null)
            return -1;
        if (child.getParent() == this) {
            int index = children.indexOf(child);
            if (index != -1) {
                detachChildAt(index);
            }
            return index;
        } 
            
        return -1;        
    }

    /**
     * <code>detachChild</code> removes a given child from the node's list.
     * This child will no longer be maintained. Only the first child with a
     * matching name is removed.
     * 
     * @param childName
     *            the child to remove.
     * @return the index the child was at. -1 if the child was not in the list.
     */
    public int detachChildNamed(String childName) {
        if(children == null) {
            return -1;
        }
        if (childName == null)
            return -1;
        for (int x = 0, max = children.size(); x < max; x++) {
            Spatial child =  children.get(x);
            if (childName.equals(child.getName())) {
                detachChildAt( x );
                return x;
            }
        }
        return -1;
    }

    /**
     * 
     * <code>detachChildAt</code> removes a child at a given index. That child
     * is returned for saving purposes.
     * 
     * @param index
     *            the index of the child to be removed.
     * @return the child at the supplied index.
     */
    public Spatial detachChildAt(int index) {
        if(children == null) {
            return null;
        }
        Spatial child =  children.remove(index);
        if ( child != null ) {
            child.setParent( null );
            logger.info("Child removed.");
        }
        return child;
    }

    /**
     * 
     * <code>detachAllChildren</code> removes all children attached to this
     * node.
     */
    public void detachAllChildren() {
        if(children != null) {
            for ( int i = children.size() - 1; i >= 0; i-- ) {
                detachChildAt( i );
            }
            logger.info("All children removed.");
        }
    }

    public int getChildIndex(Spatial sp) {
        if(children == null) {
            return -1;
        }
        return children.indexOf(sp);
    }

    public void swapChildren(int index1, int index2) {
        Spatial c2 =  children.get(index2);
        Spatial c1 =  children.remove(index1);
        children.add(index1, c2);
        children.remove(index2);
        children.add(index2, c1);
    }

    /**
     * 
     * <code>getChild</code> returns a child at a given index.
     * 
     * @param i
     *            the index to retrieve the child from.
     * @return the child at a specified index.
     */
    public Spatial getChild(int i) {
        if(children == null) {
            return null;
        }
        return children.get(i);
    }

    /**
     * <code>getChild</code> returns the first child found with exactly the
     * given name (case sensitive.)
     * 
     * @param name
     *            the name of the child to retrieve. If null, we'll return null.
     * @return the child if found, or null.
     */
    public Spatial getChild(String name) {
        if (name == null) return null;
        for (int x = 0, cSize = getQuantity(); x < cSize; x++) {
            Spatial child = children.get(x);
            if (name.equals(child.getName())) {
                return child;
            } else if(child instanceof Node) {
                Spatial out = ((Node)child).getChild(name);
                if(out != null) {
                    return out;
                }
            }
        }
        return null;
    }
    
    /**
     * determines if the provided Spatial is contained in the children list of
     * this node.
     * 
     * @param spat
     *            the child object to look for.
     * @return true if the object is contained, false otherwise.
     */
    public boolean hasChild(Spatial spat) {
        if(children == null) {
            return false;
        }
        if (children.contains(spat))
            return true;

        for (int i = 0, max = getQuantity(); i < max; i++) {
            Spatial child =  children.get(i);
            if (child instanceof Node && ((Node) child).hasChild(spat))
                return true;
        }

        return false;
    }

    /**
     * <code>updateWorldData</code> updates all the children maintained by
     * this node.
     * 
     * @param time
     *            the frame time.
     */
    @Override
    public void updateWorldData(float time) {
        super.updateWorldData(time);

        Spatial child;
        for (int i = 0, n = getQuantity(); i < n; i++) {
            try {
                child = children.get(i);
            } catch (IndexOutOfBoundsException e) {
                // a child was removed in updateGeometricState (note: this may
                // skip one child)
                break;
            }
            if (child != null) {
                child.updateGeometricState(time, false);
            }
        }
    }

    @Override
    public void updateWorldVectors(boolean recurse) {
        if (((lockedMode & Spatial.LOCKED_TRANSFORMS) == 0)) {
            updateWorldScale();
            updateWorldRotation();
            updateWorldTranslation();
            
            if (recurse) {
                for (int i = 0, n = getQuantity(); i < n; i++) {
                    children.get(i).updateWorldVectors(true);
                }
            }
        }
    }
    
    @Override
    public void lockBounds() {
        super.lockBounds();
        for (int i = 0, max = getQuantity(); i < max; i++) {
            Spatial child =  children.get(i);
            if (child != null) {
                child.lockBounds();
            }
        }
    }

    @Override
    public void lockShadows() {
        super.lockShadows();
        for (int i = 0, max = getQuantity(); i < max; i++) {
            Spatial child =  children.get(i);
            if (child != null) {
                child.lockShadows();
            }
        }
    }
    
    @Override
    public void lockTransforms() {
        super.lockTransforms();
        for (int i = 0, max = getQuantity(); i < max; i++) {
            Spatial child =  children.get(i);
            if (child != null) {
                child.lockTransforms();
            }
        }
    }

    @Override
    public void lockMeshes(Renderer r) {
        super.lockMeshes(r);
        for (int i = 0, max = getQuantity(); i < max; i++) {
            Spatial child =  children.get(i);
            if (child != null) {
                child.lockMeshes(r);
            }
        }
    }
    
    @Override
    public void unlockBounds() {
        super.unlockBounds();
        for (int i = 0, max = getQuantity(); i < max; i++) {
            Spatial child =  children.get(i);
            if (child != null) {
                child.unlockBounds();
            }
        }
    }
    
    @Override
    public void unlockShadows() {
        super.unlockShadows();
        for (int i = 0, max = getQuantity(); i < max; i++) {
            Spatial child =  children.get(i);
            if (child != null) {
                child.unlockShadows();
            }
        }
    }
    
    @Override
    public void unlockTransforms() {
        super.unlockTransforms();
        for (int i = 0, max = getQuantity(); i < max; i++) {
            Spatial child =  children.get(i);
            if (child != null) {
                child.unlockTransforms();
            }
        }
    }

    @Override
    public void unlockMeshes(Renderer r) {
        super.unlockMeshes(r);
        for (int i = 0, max = getQuantity(); i < max; i++) {
            Spatial child =  children.get(i);
            if (child != null) {
                child.unlockMeshes(r);
            }
        }
    }

    /**
     * <code>draw</code> calls the onDraw method for each child maintained by
     * this node.
     * 
     * @see com.jme.scene.Spatial#draw(com.jme.renderer.Renderer)
     * @param r
     *            the renderer to draw to.
     */
    @Override
    public void draw(Renderer r) {
        if(children == null) {
            return;
        }
        Spatial child;
        for (int i = 0, cSize = children.size(); i < cSize; i++) {
            child =  children.get(i);
            if (child != null)
                child.onDraw(r);
        }
    }

    /**
     * Applies the stack of render states to each child by calling
     * updateRenderState(states) on each child.
     * 
     * @param states
     *            The Stack[] of render states to apply to each child.
     */
    @Override
    protected void applyRenderState(Stack<? extends RenderState>[] states) {
        if(children == null) {
            return;
        }
        for (int i = 0, cSize = children.size(); i < cSize; i++) {
            Spatial pkChild = getChild(i);
            if (pkChild != null)
                pkChild.updateRenderState(states);
        }
    }

    @Override
    public void sortLights() {
        if(children == null) {
            return;
        }
        for (int i = 0, cSize = children.size(); i < cSize; i++) {
            Spatial pkChild = getChild(i);
            if (pkChild != null)
                pkChild.sortLights();
        }
    }

    /**
     * <code>updateWorldBound</code> merges the bounds of all the children
     * maintained by this node. This will allow for faster culling operations.
     * 
     * @see com.jme.scene.Spatial#updateWorldBound()
     */
    @Override
    public void updateWorldBound() {
        if ((lockedMode & Spatial.LOCKED_BOUNDS) != 0) return;
        if (children == null) {
            return;
        }
        BoundingVolume worldBound = null;
        for (int i = 0, cSize = children.size(); i < cSize; i++) {
            Spatial child =  children.get(i);
            if (child != null) {
                if (worldBound != null) {
                    // merge current world bound with child world bound
                    worldBound.mergeLocal(child.getWorldBound());

                } else {
                    // set world bound to first non-null child world bound
                    if (child.getWorldBound() != null) {
                        worldBound = child.getWorldBound().clone(this.worldBound);
                    }
                }
            }
        }
        this.worldBound = worldBound;
    }

    @Override
    public void findCollisions(
            Spatial scene, CollisionResults results, int requiredOnBits) {
        if (getWorldBound() != null && isCollidable(requiredOnBits)
                && scene.isCollidable(requiredOnBits)) {
            if (getWorldBound().intersects(scene.getWorldBound())) {
                // further checking needed.
                for (int i = 0; i < getQuantity(); i++) {
                    getChild(i).findCollisions(scene, results, requiredOnBits);
                }
            }
        }
    }

    @Override
    public boolean hasCollision(
            Spatial scene, boolean checkTriangles, int requiredOnBits) {
        if (this == scene) return false;  // No Collision with "self"
        if (getWorldBound() != null && isCollidable(requiredOnBits)
                && scene.isCollidable(requiredOnBits)) {
            if (getWorldBound().intersects(scene.getWorldBound())) {
                if(children == null && !checkTriangles) {
                    return true;
                }
                // further checking needed.
                for (int i = 0; i < getQuantity(); i++) {
                    if (getChild(i).hasCollision(
                            scene, checkTriangles, requiredOnBits)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void findPick(Ray toTest, PickResults results, int requiredOnBits) {
        if(children == null) {
            return;
        }
        if (getWorldBound() != null && isCollidable(requiredOnBits)) {
            if (getWorldBound().intersects(toTest)) {
                // further checking needed.
                for (int i = 0; i < getQuantity(); i++) {
                    ( children.get(i)).findPick(toTest, results);
                }
            }
        }
    }

	/**
	 * Returns all children to this node.
	 *
	 * @return a list containing all children to this node
	 */
	public List<Spatial> getChildren() {
        return children;
    }

    /**
     * Used with Serialization. Do not call this directly.
     * 
     * @param s
     * @throws IOException
     * @throws ClassNotFoundException
     * @see java.io.Serializable
     */
    private void readObject(java.io.ObjectInputStream s) throws IOException,
            ClassNotFoundException {
        s.defaultReadObject();
        if (children != null) {
            // go through children and set parent to this node
            for (int x = 0, cSize = children.size(); x < cSize; x++) {
                Spatial child = children.get(x);
                child.parent = this;
            }
        }
    }

    public void childChange(Geometry geometry, int index1, int index2) {
        //just pass to parent
        if(parent != null) {
            parent.childChange(geometry, index1, index2);
        }
    }
    
    public void write(JMEExporter e) throws IOException {
        super.write(e);
        if (children == null)
            e.getCapsule(this).writeSavableArrayList(null, "children", null);
        else
            e.getCapsule(this).writeSavableArrayList(new ArrayList<Spatial>(children), "children", null);
    }

    @SuppressWarnings("unchecked")
    public void read(JMEImporter e) throws IOException {
        super.read(e);
        ArrayList<Spatial> cList = e.getCapsule(this).readSavableArrayList("children", null);
        if (cList == null)
            children = null;
        else
            children = Collections.synchronizedList(cList);

        // go through children and set parent to this node
        if (children != null) {
            for (int x = 0, cSize = children.size(); x < cSize; x++) {
                Spatial child = children.get(x);
                child.parent = this;
            }
        }
    }

    @Override
    public void setModelBound(BoundingVolume modelBound) {
        if(children != null) {
            for(int i = 0, max = children.size(); i < max; i++) {
                children.get(i).setModelBound(modelBound != null ? modelBound.clone(null) : null);
            }
        }
    }

    @Override
    public void updateModelBound() {
        if(children != null) {
            for(int i = 0, max = children.size(); i < max; i++) {
                children.get(i).updateModelBound();
            }
        }
    }

    /**
     * Returns flat list of Spatials implementing the specified class AND
     * with name matching the specified pattern.
     * </P> <P>
     * Note that we are <i>matching</i> the pattern, therefore the pattern
     * must match the entire pattern (i.e. it behaves as if it is sandwiched
     * between "^" and "$").
     * You can set regex modes, like case insensitivity, by using the (?X)
     * or (?X:Y) constructs.
     * </P> <P>
     * By design, it is always safe to code loops like:<CODE><PRE>
     *     for (Spatial spatial : node.descendantMatches(AClass.class, "regex"))
     * </PRE></CODE>
     * </P> <P>
     * "Descendants" does not include self, per the definition of the word.
     * To test for descendants AND self, you must do a
     * <code>node.matches(aClass, aRegex)</code> + 
     * <code>node.descendantMatches(aClass, aRegex)</code>.
     * <P>
     *
     * @param spatialSubclass Subclass which matching Spatials must implement.
     *                        Null causes all Spatials to qualify.
     * @param nameRegex  Regular expression to match Spatial name against.
     *                        Null causes all Names to qualify.
     * @return Non-null, but possibly 0-element, list of matching Spatials.
     *
     * @see java.util.regex.Pattern
     * @see Spatial#matches(Class<? extends Spatial>, String)
     */
    public List<Spatial> descendantMatches(
            Class<? extends Spatial> spatialSubclass, String nameRegex) {
        List<Spatial> newList = new ArrayList<Spatial>();
        if (getQuantity() < 1) return newList;
        for (Spatial child : getChildren()) {
            if (child.matches(spatialSubclass, nameRegex)) newList.add(child);
            if (child instanceof Node)
                newList.addAll(((Node) child).descendantMatches(
                        spatialSubclass, nameRegex));
        }
        return newList;
    }

    /**
     * Convenience wrapper.
     *
     * @see #descendantMatches(Class<? extends Spatial>, String)
     */
    public List<Spatial> descendantMatches(
            Class<? extends Spatial> spatialSubclass) {
        return descendantMatches(spatialSubclass, null);
    }

    /**
     * Convenience wrapper.
     *
     * @see #descendantMatches(Class<? extends Spatial>, String)
     */
    public List<Spatial> descendantMatches(String nameRegex) {
        return descendantMatches(null, nameRegex);
    }
}

--------------------

/*
Copyright (c) 2011, Rockwell Collins.
Developed with the sponsorship of the Defense Advanced Research Projects Agency (DARPA).

Permission is hereby granted, free of charge, to any person obtaining a copy of this data, 
including any software or models in source or binary form, as well as any drawings, specifications, 
and documentation (collectively "the Data"), to deal in the Data without restriction, including
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so, 
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or 
substantial portions of the Data.

THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE 
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
*/

package edu.umn.crisys.observability;


public interface ILogger {
	
	final public static int STATUS  = 0;
	final public static int ERROR   = 1;
	final public static int WARN    = 2;
	final public static int INFO    = 3;
	
	public void status(String msg);
	public void error(String msg);
	public void warn(String msg);
	public void info(String msg);
	public void status(Object obj);
	public void error(Object obj);
	public void warn(Object obj);
	public void info(Object obj);
}

--------------------

