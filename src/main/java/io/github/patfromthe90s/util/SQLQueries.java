package io.github.patfromthe90s.util;


/**
 * Temporary utility class providing queries for accessing the database. [Eventually converted to a properties file]
 * @author Patrick
 */
public class SQLQueries {

	public static final String GET_LAST_UPDATED = "SELECT lastUpdated FROM site WHERE siteUrl = ?;";
	public static final String UPDATE_LAST_UPDATED = "UPDATE site SET lastUpdated = ? WHERE siteUrl = ?;";
	
	public static final String GET_ARTICLE = "SELECT url, data, date FROM article WHERE datetime(date) BETWEEN datetime(?) AND datetime(?) ORDER BY datetime(date) DESC";
	public static final String INSERT_ARTICLE = "INSERT INTO article (url, data, date) VALUES (?, ?, ?);";
}
