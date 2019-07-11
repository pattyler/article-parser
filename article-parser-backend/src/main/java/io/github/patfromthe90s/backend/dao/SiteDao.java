package io.github.patfromthe90s.backend.dao;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * DAO interface for accessing the Site table in the database.
 * 
 * @author Patrick
 *
 */
public interface SiteDao {
	

	/**
	 * Retrieves from the database the last known time the given site was updated. <br/>
	 * Assumes {@code siteId} is a valid database entry.
	 * 
	 * @param siteId The ID of the row to update (e.g. <code>"NHKEASY"</code>).
	 * @return Last known time the given site was updated, or the empty <code>Optional</code> if no row was found.
	 */
	public Optional<ZonedDateTime> getLastUpdated(String siteId);
	
	/**
	 * Update the database row containing <code>siteId</code> in the <code>site</code> table and set the <code>last_updated</code> 
	 * column to equal the <code>date</code> column of the most recent article associated with this <code>url</code> in
	 * the <code>article</code> table. 
	 * 
	 * @param siteId The ID of the row to update (e.g. <code>"NHKEASY"</code>).
	 * @return the number of rows updated. If successful, this should only be one.
	 */
	public int updateLastUpdated(String siteId);

}