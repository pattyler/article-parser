package io.github.patfromthe90s.http;

import java.net.URL;
import java.time.ZonedDateTime;

import io.github.patfromthe90s.exception.GenericHTTPException;
import io.github.patfromthe90s.exception.HeaderNotPresentException;

/**
 * Interface for getting information and data from {@code URL}s.
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
	public ZonedDateTime getLastUpdated(URL url) throws GenericHTTPException, HeaderNotPresentException;
	
	/**
	 * Fetch the HTML page associated with {@code url}.
	 * @param url The page to fetch.
	 * @return The HTML of {@code url}.
	 * @throws GenericHTTPException if there was a problem communicating with the <code>url</code>
	 */
	public String getHtml(URL url) throws GenericHTTPException;
	
}
