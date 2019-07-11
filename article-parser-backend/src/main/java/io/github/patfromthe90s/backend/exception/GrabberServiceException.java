package io.github.patfromthe90s.backend.exception;

/**
 * Thrown to indicate that something went wrong in the service layer
 * 
 * @author Patrick
 *
 */
public class GrabberServiceException extends Exception {

	/**
	 * TODO alter this?
	 */
	private static final long serialVersionUID = 5783488542925827759L;

	public GrabberServiceException() {
		super();
	}
	
	public GrabberServiceException(String msg) {
		super(msg);
	}
	
	public GrabberServiceException(Throwable cause) {
		super(cause);
	}
	
	public GrabberServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
