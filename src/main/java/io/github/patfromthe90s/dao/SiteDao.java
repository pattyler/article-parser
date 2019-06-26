package io.github.patfromthe90s.dao;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;

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
	public LocalDateTime getLastUpdated(URL url) throws RecordNotInDatabaseException, SQLException;
	
	/**
	 * Associate the given {@code url} in the database with {@code newLastUpdated}.<br/>
	 * Assumes {@code newLastUpdated} is more recent than the current value in the database.
	 * 
	 * 
	 * @param url {@link URL} representing site to update.
	 * @param newLastUpdated New {@code lastUpdated} time to insert into the database.
	 * @throws RecordNotInDatabaseException if the given {@code url} doesn't exist in the database.
	 * @throws SQLException If there is some underlying problem.
	 */
	public void updateLastUpdated(URL url, LocalDateTime newLastUpdated) throws RecordNotInDatabaseException, SQLException;

}