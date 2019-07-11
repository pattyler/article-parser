package io.github.patfromthe90s.backend.service;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.List;

import io.github.patfromthe90s.backend.exception.DaoServiceException;
import io.github.patfromthe90s.backend.model.Article;

/**
 * Interface for a service layer for mediating between a higher-level client class and lower level DAOs.
 * 
 * @author Patrick
 */
public interface DaoService {
	
	/**
	 * Retrieves the last known time the given site was updated. <br/>
	 * Assumes <code>siteId</code> is a valid database entry.
	 * 
	 * @param siteId <code>ID</code> of site to retrieve the information from.
	 * @return Last known time the given site was updated.
	 * @throws DaoServiceException if the <code>siteId</code> doesn't exist in the database
	 */
	public ZonedDateTime getLastUpdated(String siteId) throws DaoServiceException;
	
	
	/**
	 * Update the database row containing <code>siteId</code> in the <code>site</code> table and set the <code>last_updated</code> 
	 * column to equal the <code>date</code> column of the most recent article associated with this <code>url</code> in
	 * the <code>article</code> table. 
	 * 
	 * @param siteId <code>ID</code> of site row to update.
	 * @throws DaoServiceException if the <code>siteId</code> doesn't exist in the database
	 * @see {@link SiteService}
	 */
	public void updateLastUpdated(String siteId) throws DaoServiceException;
	
	/**
	 * Returns any articles that were written between {@code from} (inclusive) and {@code to} (inclusive). <br/>
	 * Both <code>from</code> and <code>to</code> are converted to UTC if they are not already.
	 * 
	 * @param from The date to search from (inclusive).
	 * @param to The date to search until (inclusive).
	 * @return	Any articles written between {@code from} and {@code to}.
	 * @throws SQLException if something went wrong.
	 */
	public List<Article> getArticlesBetween(ZonedDateTime from, ZonedDateTime to) throws DaoServiceException;
	
	/**
	 * Attempts to insert the given {@code article} into the database. No guarantees are made.
	 * @param article The article to insert.
	 * @throws SQLException if something went wrong
	 */
	public void insertArticle(Article article) throws DaoServiceException;

}
