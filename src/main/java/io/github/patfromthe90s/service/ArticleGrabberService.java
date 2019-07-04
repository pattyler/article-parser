package io.github.patfromthe90s.service;

import java.util.List;

import io.github.patfromthe90s.model.Article;

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
	 * Grabs and returns articles. <br/>
	 * This method essentially defines the rule by which this Service should download articles.
	 * 
	 * @return <code>List</code> of populated articles, or the empty list if there are none.
	 */
	public List<Article> grabArticles();
	
	/**
	 * 
	 * @param articleLinkDates
	 * @return
	 */
	public void persist(List<Article> articles);
	
	/**
	 * Update the database site table to contain the most recent article as lastUpdated. <br/>
	 * This should be called directly after <code>grabAndPersist</code>
	 */
	public void updateLastUpdated();
	
}
