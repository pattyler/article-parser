package io.github.patfromthe90s.util;


/**
 * Temporary util class providing queries for accessing the database. [Eventually converted to a properties file]
 * @author Patrick
 */
public class SQLQueries {

	public static final String GET_LAST_UPDATED = "SELECT lastUpdated FROM site WHERE siteUrl = ?;";
	public static final String UPDATE_LAST_UPDATED = "UPDATE site SET lastUpdated = ? WHERE siteUrl = ?;";
	
}
