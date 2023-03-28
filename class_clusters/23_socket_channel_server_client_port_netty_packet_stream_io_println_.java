package net.minecraft.network.rcon;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public abstract class RConThreadBase implements Runnable
{
    private static final AtomicInteger THREAD_ID = new AtomicInteger(0);
    /** True if the Thread is running, false otherwise */
    protected boolean running;
    /** Reference to the IServer object. */
    protected IServer server;
    protected final String threadName;
    /** Thread for this runnable class */
    protected Thread rconThread;
    protected int maxStopWait = 5;
    /** A list of registered DatagramSockets */
    protected List<DatagramSocket> socketList = Lists.<DatagramSocket>newArrayList();
    /** A list of registered ServerSockets */
    protected List<ServerSocket> serverSocketList = Lists.<ServerSocket>newArrayList();

    protected RConThreadBase(IServer serverIn, String threadName)
    {
        this.server = serverIn;
        this.threadName = threadName;

        if (this.server.isDebuggingEnabled())
        {
            this.logWarning("Debugging is enabled, performance maybe reduced!");
        }
    }

    /**
     * Creates a new Thread object from this class and starts running
     */
    public synchronized void startThread()
    {
        this.rconThread = new Thread(this, this.threadName + " #" + THREAD_ID.incrementAndGet());
        this.rconThread.start();
        this.running = true;
    }

    /**
     * Returns true if the Thread is running, false otherwise
     */
    public boolean isRunning()
    {
        return this.running;
    }

    /**
     * Log debug message
     */
    protected void logDebug(String msg)
    {
        this.server.logDebug(msg);
    }

    /**
     * Log information message
     */
    protected void logInfo(String msg)
    {
        this.server.logInfo(msg);
    }

    /**
     * Log warning message
     */
    protected void logWarning(String msg)
    {
        this.server.logWarning(msg);
    }

    /**
     * Log severe error message
     */
    protected void logSevere(String msg)
    {
        this.server.logSevere(msg);
    }

    /**
     * Returns the number of players on the server
     */
    protected int getNumberOfPlayers()
    {
        return this.server.getCurrentPlayerCount();
    }

    /**
     * Registers a DatagramSocket with this thread
     */
    protected void registerSocket(DatagramSocket socket)
    {
        this.logDebug("registerSocket: " + socket);
        this.socketList.add(socket);
    }

    /**
     * Closes the specified DatagramSocket
     */
    protected boolean closeSocket(DatagramSocket socket, boolean removeFromList)
    {
        this.logDebug("closeSocket: " + socket);

        if (null == socket)
        {
            return false;
        }
        else
        {
            boolean flag = false;

            if (!socket.isClosed())
            {
                socket.close();
                flag = true;
            }

            if (removeFromList)
            {
                this.socketList.remove(socket);
            }

            return flag;
        }
    }

    /**
     * Closes the specified ServerSocket
     */
    protected boolean closeServerSocket(ServerSocket socket)
    {
        return this.closeServerSocket_do(socket, true);
    }

    /**
     * Closes the specified ServerSocket
     */
    protected boolean closeServerSocket_do(ServerSocket socket, boolean removeFromList)
    {
        this.logDebug("closeSocket: " + socket);

        if (null == socket)
        {
            return false;
        }
        else
        {
            boolean flag = false;

            try
            {
                if (!socket.isClosed())
                {
                    socket.close();
                    flag = true;
                }
            }
            catch (IOException ioexception)
            {
                this.logWarning("IO: " + ioexception.getMessage());
            }

            if (removeFromList)
            {
                this.serverSocketList.remove(socket);
            }

            return flag;
        }
    }

    /**
     * Closes all of the opened sockets
     */
    protected void closeAllSockets()
    {
        this.closeAllSockets_do(false);
    }

    /**
     * Closes all of the opened sockets
     */
    protected void closeAllSockets_do(boolean logWarning)
    {
        int i = 0;

        for (DatagramSocket datagramsocket : this.socketList)
        {
            if (this.closeSocket(datagramsocket, false))
            {
                ++i;
            }
        }

        this.socketList.clear();

        for (ServerSocket serversocket : this.serverSocketList)
        {
            if (this.closeServerSocket_do(serversocket, false))
            {
                ++i;
            }
        }

        this.serverSocketList.clear();

        if (logWarning && 0 < i)
        {
            this.logWarning("Force closed " + i + " sockets");
        }
    }
}
--------------------

package Internet.Servers;

import toolBox.Print;
import Hardware.Computer;

public class Server {
	
	/*
	 * 
	 *Still Working on this! 
	 * 
	 */
	
	public String address;
	
	public boolean on = false;
	
	/**
	 * 
	 * Sets up the server.
	 * 
	 * @param address - The address of the server
	 */
	public Server(String address) {
		this.address = address;
	}
	
	public Server() {
	}
	
	/**
	 * Starts the server.
	 */
	public void start() {
		on = true;
		Print.info("Starting server on " + address);
	}
	
	/**
	 * Stops the server
	 */
	public void stop() {
		on = false;
		Print.info("Shuting down the server!");
	}
	
	/**
	 * 
	 * This method send data to the computer
	 * 
	 * @param computer - The computer to send the data to
	 * @param address - The address of the server
	 * @param message - The message to send
	 */
	public void sendData(Computer computer, String address, String message) {
		computer.addData(address, message);
	}
	
	/**
	 * 
	 * This method gets data from the computer.
	 * It his different between every server.
	 * 
	 * @param computer - The computer to send the message to
	 * @param message - The message to send
	 */
	public void getData(Computer computer, String message) {
	}
}

--------------------

package uw.playdesigner6;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by Metta on 3/2/2015.
 */
public class TCPConnection extends Thread {

    public boolean running;
    private List<PrintWriter> outStreams;
    private int port;
    /**
     * Constructor of the class
     */
    public TCPConnection(List<PrintWriter> outStreams, int port) {
        this.port = port;
        this.outStreams = outStreams;
        this.running = true;
    }


    @Override
    public void run() {
        super.run();
            try {
                System.out.println("S: Connecting...");

                //create a server socket. A server socket waits for requests to come in over the network.
                ServerSocket serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress(port));
                serverSocket.setReuseAddress(true);
                System.out.println("Finish connecting " + port);
                while(running) {
                    //create client socket... the method accept() listens for a connection to be made to this socket and accepts it.
                    Socket client = serverSocket.accept();
                    System.out.println("S: Receiving...");
                    PrintWriter mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                    outStreams.add(mOut);
                }
            } catch (Exception e) {
                System.out.println("S: Error");
                e.printStackTrace();
            }
        }

    }




--------------------

