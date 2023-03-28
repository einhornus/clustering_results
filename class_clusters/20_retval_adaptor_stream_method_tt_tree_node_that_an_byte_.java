/**
 * 
 */
package com.perforce.p4java.exception;

import com.perforce.p4java.Log;

// p4ic4idea: use IServerMessage
import com.perforce.p4java.server.IServerMessage;


/**
 * An exception to be used to signal that the Perforce server has detected
 * an error in processing or fielding a request. This error might be a usage
 * error in a command sent to the server, or a missing or bad parameter, or
 * a semantics error in the request. Note that this is not the same as a
 * connection exception, which is typically signaled when the Perforce server
 * itself can't be reached.<p>
 * 
 * RequestExceptions coming back from the server have a non-zero rawCode field
 * that is set to the corresponding raw code value sent from the server; this
 * code normally requires processing for use by callers, who typically want to
 * see the correspond generic and severity codes, but it's available here if needed.
 * Other segments or interpretations of the raw code -- subCode, subSystem,
 * uniqueCode, and the raw code itself -- are available here if you need them and know what
 * they mean. Note, though, that only the generic and severity codes are documented
 * in the main Perforce documentation, and only those fields are guaranteed to be
 * set to meaningful values here (in fact, all RequestExceptions constructed 'under
 * the covers' in the RPC layer will do what they can to get meaningful value for
 * these other fields, but this is not guaranteed)...<p>
 * 
 * Note that if you set the raw code yourself using the accessor methods, you are
 * required to also set the other fields to appropriate values yourself; failure
 * to do this will cause a lot of confusion up the chain, as the subsidiary codes are
 * only calculated once. The setCodes method is provided to make this easy.<p>
 * 
 * See the MessageSeverityCode and MessageGenericCode definitions for suitable help
 * with those types of code.
 */

public class RequestException extends P4JavaException {
	
	private static final long serialVersionUID = 1L;
	private IServerMessage message;
	private int rawCode = 0;
	private int severityCode = 0;
	private int genericCode = 0;
	private int uniqueCode = 0;
	private int subCode = 0;
	private int subSystem = 0;

	// p4ic4idea: removed
	/*
	public RequestException() {
		super();
	}
	*/

	// p4ic4idea: deprecated
	@Deprecated
	public RequestException(String message, Throwable cause) {
		super(message, cause);
	}

	// p4ic4idea: deprecated
	@Deprecated
	public RequestException(String message) {
		super(message);
	}

	// p4ic4idea: deprecated
	@Deprecated
	public RequestException(Throwable cause) {
		super(cause);
	}

	// p4ic4idea: removed
	/*
	public RequestException(String message, int rawCode) {
		super(message);
		setCodes(rawCode);
	}
	*/

	/** @deprecated should be a very specific exception */
	@Deprecated
	public RequestException(String message, int genericCode, int severityCode) {
		super(message);
		this.genericCode = genericCode;
		this.severityCode = severityCode;
	}

	/* p4ic4idea: removed

	public RequestException(String message, String codeString) {
		super(message);
		if (codeString != null) {
			try {
				setCodes(new Integer(codeString));
			} catch (Exception exc) {
				Log.exception(exc);
			}
		}
	}
	
	public RequestException(String message, int genericCode, int severityCode) {
		super(message);
		this.genericCode = genericCode;
		this.severityCode = severityCode;
	}
	
	public RequestException(Throwable cause, int genericCode, int severityCode) {
		super(cause);
		this.genericCode = genericCode;
		this.severityCode = severityCode;
	}
	
	public RequestException(String message, Throwable cause, int genericCode, int severityCode) {
		super(message, cause);
		this.genericCode = genericCode;
		this.severityCode = severityCode;
	}
	*/

	// p4ic4idea: new IServerMessage constructors
	public RequestException(IServerMessage message) {
		super(message.getLocalizedMessage());
		this.message = message;
		this.rawCode = message.getRawCode();
		this.subCode = message.getSubCode();
		this.subSystem = message.getSubSystem();
		this.uniqueCode = message.getUniqueCode();
		this.genericCode = message.getGeneric();
		this.severityCode = message.getSeverity();
	}

	public RequestException(IServerMessage message, Throwable t) {
		super(message.getLocalizedMessage(), t);
		this.message = message;
		this.rawCode = message.getRawCode();
		this.subCode = message.getSubCode();
		this.subSystem = message.getSubSystem();
		this.uniqueCode = message.getUniqueCode();
		this.genericCode = message.getGeneric();
		this.severityCode = message.getSeverity();
	}

	/**
	 * Set the raw code and associated subsidiary codes according to
	 * the passed-in values. If you only have the raw code from the server,
	 * this is probably the easiest and least error-prone way to set the
	 * request exception codes.
	 * 
	 * @param rawCode raw code from the server.
	 * @return 'this' for chaining.
	 */
	public RequestException setCodes(int rawCode) {
		this.rawCode = rawCode;
		this.subCode = ((rawCode >> 0) & 0x3FF);
		this.subSystem = ((rawCode >> 10) & 0x3F);
		this.uniqueCode = (rawCode & 0xFFFF);
		this.genericCode = ((rawCode >> 16) & 0xFF);
		this.severityCode = ((rawCode >> 28) & 0x00F);
		return this;
	}

	/** @deprecated p4ic4idea: error should be immutable */
	@Deprecated
	public void setSeverityCode(int severityCode) {
		this.severityCode = severityCode;
	}


    /** @deprecated p4ic4idea: error should be immutable */
	@Deprecated
	public void setGenericCode(int genericCode) {
		this.genericCode = genericCode;
	}
	
	public String getDisplayString() {
		return "" + (this.genericCode != 0 ? "Generic: " + this.genericCode : "")
				+ (this.severityCode != 0 ? " Severity: " + this.severityCode + "; " : "")
				// p4ic4idea: report sub-code
				+ (this.subCode != 0 ? "SubCode: " + this.subCode + "; " : "")
				+ this.getMessage()
				+ (this.getCause() != null ? this.getCause() : "");
	}

	public int getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(int uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public int getRawCode() {
		return this.rawCode;
	}

	public int getSubCode() {
		return subCode;
	}

	public void setSubCode(int subCode) {
		this.subCode = subCode;
	}

	public int getSubSystem() {
		return this.subSystem;
	}

	public void setSubSystem(int subSystem) {
		this.subSystem = subSystem;
	}

	// p4ic4idea: access IServerMessage
	public IServerMessage getServerMessage() {
		return message;
	}

	/** @deprecated use the server code instead */
	@Deprecated
	public boolean hasMessageFragment(String fragment) {
		return message == null
				? (getMessage() != null && getMessage().toLowerCase().equals(fragment.toLowerCase()))
				: message.hasMessageFragment(fragment);
	}

	/**
	 * Get the Perforce severity code associated with this exception, if any.
	 * See the MessageSeverityCode Javadocs for an explanation of these codes.
	 */
	public int getSeverityCode() {
		return this.severityCode;
	}

	/**
	 * Get the Perforce generic code associated with this exception, if any.
	 * See the MessageSGenericCode Javadocs for an explanation of these codes.
	 */
	public int getGenericCode() {
		return this.genericCode;
	}
}

--------------------

/*
######################################################################################
##                                                                                  ##
## (c) 2006-2012 Cable Television Laboratories, Inc.  All rights reserved.  Any use ##
## of this documentation/package is subject to the terms and conditions of the      ##
## CableLabs License provided to you on download of the documentation/package.      ##
##                                                                                  ##
######################################################################################


*/
package com.cablelabs.fsm;

public interface Conditional extends Cloneable {
	
	/** This implements a deep copy of the class for replicating 
	 * FSM information.
	 * 
	 * @throws CloneNotSupportedException if clone method is not supported
	 * @return Object
	 */ 
	public Object clone() throws CloneNotSupportedException;
	
	/**
	 * The display method is different because it attempts to try and format 
	 * the information in a more user friendly format than the toString
	 * method. 
	 * 
	 * Class that implement this method should attempt to display the
	 * information in a msg[msg_instance].hdr[hdr_instance].field format
	 * to keep it consistent.
	 * @return
	 */
//	public String display();
	
	/**
	 * Definition for the evaluate method. Its intent is
	 * to provide a common interface for all of the logical,
	 * comparison and binary operations
	 */
	public boolean evaluate(ComparisonEvaluator ce, MsgEvent event);

	public int getWildcardIndex();
	
    public boolean hasWildcardIndex();

	/**
	 * This method sets all of the wildcard attributes in each ArrayIndex back
	 * to the wildcard value of -1. 
	 */
	public void resetWildcardIndex();
	
	/**
	 * Require implementation of a printable represention of the derived
	 * classes information.
	 */
	@Override
	public String toString();
	
	/**
	 * This implements the deep updating of the fsmUID for all of the message
	 * and variable expression references in a FSM.
	 *
	 */
	public void updateUIDs(int newUID, int origUID);

	/**
	 * This method sets the value defined by the wildcard attribute in each
	 * ArrayIndex reference to the new value defined by the newIndex.
	 * 
	 */
	public void updateWildcardIndex(int newIndex);
	
	/**
	 * The display method is different because it attempts to try and format 
	 * the information in a more user friendly format than the toString
	 * method. 
	 * 
	 * Class that implement this method should attempt to display the
	 * information in a msg[msg_instance].hdr[hdr_instance].field format
	 * to keep it consistent.
	 * @return
	 */
	public String display();
}

--------------------

/*
 * Copyright (c) 2006-2014 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.lang.annotation.*;

/**
 * Used inside a {@linkplain MockUp mock-up} class to indicate a <em>mock method</em> whose implementation will
 * temporarily replace the implementation of a matching "real" method.
 * <p/>
 * The mock method must have the same name and the same parameters as the matching real method, except for an optional
 * first parameter of type {@link Invocation}; if this extra parameter is present, the remaining ones must match the
 * parameters in the real method.
 * The mock method must also have the same return type as the matching real method.
 * <p/>
 * Method modifiers (<code>public</code>, <code>final</code>, <code>static</code>, etc.) between mock and mocked
 * methods <em>don't</em> have to be the same.
 * It's perfectly fine to have a non-<code>static</code> mock method for a {@code static} mocked method (or vice-versa),
 * for example.
 * <p/>
 * Checked exceptions in the <code>throws</code> clause (if any) can also differ between the two matching methods.
 * A mock <em>method</em> can also target a <em>constructor</em>, in which case the previous considerations still apply,
 * except for the name of the mock method which must be "<code>$init</code>".
 * <p/>
 * A mock method can specify <em>constraints</em> on the number of invocations it should receive while in effect
 * (ie, from the time a real method/constructor is mocked to the time it is restored to its original definition).
 * <p/>
 * The special mock methods <strong>{@code void $init(...)}</strong> and <strong>{@code void $clinit()}</strong>
 * correspond to constructors and to {@code static} class initializers, respectively.
 * (Notice that it makes no difference if the real class contains more than one static initialization block, because the
 * compiler merges the sequence of static blocks into a single internal "&lt;clinit>" static method in the class file.)
 * Mock methods named {@code $init} will apply to the corresponding constructor in the real class, by matching the
 * declared parameters; just like regular mock methods, they can also have a first parameter of type {@link Invocation}.
 *
 * @see #invocations invocations
 * @see #minInvocations minInvocations
 * @see #maxInvocations maxInvocations
 * @see <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/StateBasedTesting.html#mocks">Tutorial</a>
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Mock
{
   /**
    * Number of expected invocations of the mock method.
    * If 0 (zero), no invocations will be expected.
    * A negative value (the default) means there is no expectation on the number of invocations;
    * that is, the mock can be called any number of times or not at all during any test which uses it.
    * <p/>
    * A non-negative value is equivalent to setting {@link #minInvocations minInvocations} and
    * {@link #maxInvocations maxInvocations} to that same value.
    *
    * @see <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/StateBasedTesting.html#constraints">Tutorial</a>
    */
   int invocations() default -1;

   /**
    * Minimum number of expected invocations of the mock method, starting from 0 (zero, which is the default).
    * 
    * @see #invocations invocations
    * @see #maxInvocations maxInvocations
    * @see <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/StateBasedTesting.html#constraints">Tutorial</a>
    */
   int minInvocations() default 0;

   /**
    * Maximum number of expected invocations of the mock method, if positive.
    * If zero the mock is not expected to be called at all.
    * A negative value (the default) means there is no expectation on the maximum number of invocations.
    * 
    * @see #invocations invocations
    * @see #minInvocations minInvocations
    * @see <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/StateBasedTesting.html#constraints">Tutorial</a>
    */
   int maxInvocations() default -1;
}

--------------------

