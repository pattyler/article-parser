package io.github.patfromthe90s.dao;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;

import io.github.patfromthe90s.exception.RecordNotInDatabaseException;

/**
 * DAO interface for accessing the Site table in the database.
 * 
 * @author Patrick
 *
 */
public interface SiteDao {
	

	/**
	 * Retrieves from the database the last known time the given site was updated. <br/>
	 * Assumes {@code url} is a valid database entry.
	 * 
	 * @param url {@link URL} representing site to retrieve the information from.
	 * @return Last known time the given site was updated.
	 * @throws RecordNotInDatabaseException if the given {@code url} doesn't exist in the database.
	 * @throws SQLException If there is some underlying problem.
	 */
	public ZonedDateTime getLastUpdated(String url) throws RecordNotInDatabaseException, SQLException;
	
	/**
	 * Update the database row containing <code>url</code> in the <code>site</code> table and set the <code>lastUpdated</code> 
	 * column to equal the <code>date</code> column of the most recent article associated with this <code>url</code> in
	 * the <code>article</code> table. 
	 * 
	 * @param url <code>URL</code> representing site to update.
	 * @throws RecordNotInDatabaseException if the given {@code url} doesn't exist in the database.
	 * @throws SQLException If there was some underlying problem.
	 */
	public void updateLastUpdated(String url) throws RecordNotInDatabaseException, SQLException;

}