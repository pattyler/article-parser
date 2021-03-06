package io.github.pattyler.grabber.exception;

import io.github.pattyler.grabber.service.SiteService;

/**
 * Thrown to indicate that something went wrong behind-the-scenes in {@link SiteService}.
 * 
 * @author Patrick
 *
 */
public class SiteServiceException extends Exception {
	
	/**
	 * TODO alter this?
	 */
	private static final long serialVersionUID = -974217763673742345L;

	public SiteServiceException() {
		super();
	}
	
	public SiteServiceException(String msg) {
		super(msg);
	}
	
	public SiteServiceException(Throwable cause) {
		super(cause);
	}
	
	public SiteServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
