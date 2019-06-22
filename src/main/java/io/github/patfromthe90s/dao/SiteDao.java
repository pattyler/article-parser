package io.github.patfromthe90s.dao;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * DAO for accessing the Site table in the database.
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
	 * @throws SQLException if the given {@code url} doesn't exist in the database, or some other unexpected reason.
	 * @return Last known time the given site was updated.
	 */
	public LocalDateTime getLastUpdated(URL url) throws SQLException;
	
	/**
	 * Associate the given {@code url} in the database with {@code newLastUpdated}.<br/>
	 * Assumes {@code newLastUpdated} is more recent than the current value in the database.
	 * 
	 * 
	 * @param url {@link URL} representing site to update.
	 * @param newLastUpdated New {@code lastUpdated} time to insert into the database.
	 * @return 
	 * @throws SQLException if the given {@code url} doesn't exist in the database, or some other unexpected reason.
	 */
	public boolean updateLastUpdated(URL url, LocalDateTime newLastUpdated) throws SQLException;

}