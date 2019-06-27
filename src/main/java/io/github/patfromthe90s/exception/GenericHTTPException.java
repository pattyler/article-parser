package io.github.patfromthe90s.exception;

/**
 * Thrown to indicate some kind of problem with the HTTP request.
 * 
 * @author Patrick
 */
public class GenericHTTPException extends Exception {
	
	/**
	 *  TODO update this properly
	 */
	private static final long serialVersionUID = 7799039046084838260L;

	public GenericHTTPException() {
		super();
	}
	
	public GenericHTTPException(String msg) {
		super(msg);
	}
	
	public GenericHTTPException(Throwable cause) {
		super(cause);
	}
	
	public GenericHTTPException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
