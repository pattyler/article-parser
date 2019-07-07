package io.github.patfromthe90s.service;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.List;

import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.model.Article;

/**
 * Interface for a service layer for mediating between a higher-level client class and lower level DAOs.
 * 
 * @author Patrick
 */
public interface DaoService {
	
	/**
	 * Retrieves the last known time the given site was updated. <br/>
	 * Assumes {@code url} is a valid database entry.
	 * 
	 * @param url {@link URL} representing site to retrieve the information from.
	 * @return Last known time the given site was updated.
	 * @throws DaoServiceException if the {@code url} doesn't exist in the database, or if something else
	 * went wrong related to the database.
	 */
	public ZonedDateTime getLastUpdated(String url) throws DaoServiceException;
	
	
	/**
	 * 
	 * @param url <code>URL</code> representing site to update.
	 * @throws DaoServiceException if the {@code url} doesn't exist in the database, or if something else
	 * went wrong related to the database.
	 * @see {@link SiteService}
	 */
	public void updateLastUpdated(String url) throws DaoServiceException;
	
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
