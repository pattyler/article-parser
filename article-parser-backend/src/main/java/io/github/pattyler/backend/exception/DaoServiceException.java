package io.github.pattyler.backend.exception;

/**
 * Thrown to indicate that something went wrong in the service layer
 * 
 * @author Patrick
 *
 */
public class DaoServiceException extends Exception {
	
	/**
	 * TODO alter this?
	 */
	private static final long serialVersionUID = 7745143038371773926L;

	public DaoServiceException() {
		super();
	}
	
	public DaoServiceException(String msg) {
		super(msg);
	}
	
	public DaoServiceException(Throwable cause) {
		super(cause);
	}
	
	public DaoServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
