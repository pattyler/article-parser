package io.github.patfromthe90s.backend.exception;

/**
 * Thrown to indicate that a requested HTTP header was not present in the HTTP response from the server.
 * 
 * @author Patrick
 */
public class HeaderNotPresentException extends Exception {
	

	/**
	 * TODO alter this?
	 */
	private static final long serialVersionUID = -1071772741946484635L;

	public HeaderNotPresentException() {
		super();
	}
	
	public HeaderNotPresentException(String msg) {
		super(msg);
	}
	
	public HeaderNotPresentException(Throwable cause) {
		super(cause);
	}
	
	public HeaderNotPresentException(String msg, Throwable cause) {
		super(msg, cause);
	}


}
