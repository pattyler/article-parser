package io.github.patfromthe90s.service;

import java.util.List;

import io.github.patfromthe90s.model.ArticleLinkDate;

/**
 * Service for grabbing articles from a web-site. </br>
 * Given a web-site, the service should check for any articles that should be downloaded according to <<should>>. If so,
 * the service proceeds to download the content of the article and, if desired, any other data in the database. <br />
 * Loosely based on the Proxy pattern to tie together various other services in some meaningful way.
 *  
 * @author Patrick
 *
 */
public interface ArticleGrabberService {

	/**
	 * Checks for, and returns, any articles that should be grabbed. <br/>
	 * This method essentially defines the rule by which this Service should download articles.
	 * 
	 * @return THe <code>List</code> of articles to grab, or the empty list if there are none.
	 */
	public List<ArticleLinkDate> articlesToGrab();
	
	/**
	 * 
	 * @param articleLinkDates
	 * @return
	 */
	public int grabAndPersist(List<ArticleLinkDate> articleLinkDates);
	
	/**
	 * Update the database site table to contain the most recent article as lastUpdated. <br/>
	 * This should be called directly after <code>grabAndPersist</code>
	 */
	public void updateLastUpdated();
	
}
