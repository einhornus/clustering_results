package au.notzed.jjmpeg.exception;

/**
 *
 * @author notzed
 */
public class AVInvalidStreamException extends AVException {

	public AVInvalidStreamException(String what) {
		super(what);
	}
}

--------------------

/**
 * 
 */
package org.openiaml.model.inference;


/**
 * @author jmwright
 *
 */
public class InferenceException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param e
	 */
	public InferenceException(Throwable e) {
		super(e.getMessage(), e);
	}
	
	public InferenceException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public InferenceException(String message, Throwable throwable) {
		super(message, throwable);
	}

}

--------------------

package words.exceptions;

@SuppressWarnings("serial")
public class ClassAlreadyExistsException extends WordsRuntimeException {
	
	private String className;
	
	public ClassAlreadyExistsException(String className) {
		this.className = className;
	}
	
	@Override
	public String toString() {
		return String.format("Class %s already exists and cannot be created again.", className);
	}
}

--------------------

