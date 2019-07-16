package io.github.pattyler.grabber.http;

import java.time.ZonedDateTime;

import io.github.pattyler.grabber.exception.GenericHTTPException;
import io.github.pattyler.grabber.exception.HeaderNotPresentException;

/**
 * Interface for retrieving data over HTTP. <br/>
 * The data should be returned as a <code>String</code> in the <code>get()</code> method. The meaning of
 * this data depends on the implementation. (i.e. HTML, JSON, etc.)
 * 
 * @author Patrick
 *
 */
public interface Interactor {
	
	/**
	 * Returns the last time, in UTC, the page associated with {@code url} was modified by looking at the {@code last-modified} header.
	 * 
	 * @param url The page to check.
	 * @return UTC Time the page was last modified according to the {@code last-modified} response header.
	 * @throws GenericHTTPException if there was a problem communicating with the <code>url</code>
	 * @throws HeaderNotPresentException if the <code>last-modified</code> header is not present in the HTTP response.
	 */
	public ZonedDateTime getLastUpdated(String url) throws GenericHTTPException, HeaderNotPresentException;

	/**
	 * Given a URL, retrieve the data as a String.
	 * 
	 * @param url The location of the data.
	 * @return The data.
	 * @throws GenericHTTPException if there was a problem communicating with the <code>url</code>
	 */
	public String get(String url) throws GenericHTTPException;
	
}
