package io.github.patfromthe90s.dao;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import io.github.patfromthe90s.util.SQLQueries;

/**
 * Simple implementation of {@link SiteDao}
 * 
 * @author Patrick
 *
 */
public class SimpleSiteDao implements SiteDao {
	
	private final DataSource dataSource;
	
	/**
	 * @param dataSource The {@link DataSource} for retrieving the database {@link Connection}.
	 */
	public SimpleSiteDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public LocalDateTime getLastUpdated(URL url) throws SQLException {
		PreparedStatement ps = getPreparedStatement(SQLQueries.GET_LAST_UPDATED);
		ps.setString(1, url.toString()); 
		ResultSet rs = ps.executeQuery();
		rs.next();
		return LocalDateTime.parse(rs.getString(1));
	}
	
	public boolean updateLastUpdated(URL url, LocalDateTime newLastUpdated) throws SQLException {
		PreparedStatement ps = getPreparedStatement(SQLQueries.UPDATE_LAST_UPDATED);
		ps.setString(1, newLastUpdated.toString());
		ps.setString(2, url.toString());
		int numUpdated = ps.executeUpdate();
		return numUpdated > 0;
	}
	
	private PreparedStatement getPreparedStatement(String query) throws SQLException {
		Connection conn = dataSource.getConnection();
		return conn.prepareStatement(query);
	}
}