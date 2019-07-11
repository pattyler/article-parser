package io.github.patfromthe90s.backend.service;

import java.time.ZonedDateTime;

import io.github.patfromthe90s.backend.exception.SiteServiceException;

/**
 * Service for interacting with and parsing data from a web-site.
 * 
 * @author Patrick
 *
 */
public interface SiteService {
	
	/**
	 * Returns the last time, in UTC, the page associated with {@code url} was modified by looking at the {@code last-modified} header.
	 * 
	 * @param url The page to check.
	 * @return UTC Time the page was last modified according to the {@code last-modified} response header.
	 * @throws SiteServiceException if something went wrong.
	 */
	public ZonedDateTime getLastUpdated(String url) throws SiteServiceException;
	
	/**
	 * Given a URL, retrieve the HTML response as a String.
	 * 
	 * @param url The location of the data.
	 * @return The data.
	 * @throws SiteServiceException if something went wrong.
	 */
	public String getHtml(String url) throws SiteServiceException;
	
	/**
	 * Given a URL, retrieve the JSON response as a String.
	 * 
	 * @param url The location of the data.
	 * @return The data.
	 * @throws SiteServiceException if something went wrong.
	 */
	public String getJson(String url) throws SiteServiceException;

}
