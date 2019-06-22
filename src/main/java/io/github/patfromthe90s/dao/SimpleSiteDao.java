package io.github.patfromthe90s.dao;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import io.github.patfromthe90s.util.SQLQueries;

public class SimpleSiteDao implements SiteDao {
	
	private final DataSource dataSource;
	
	public SimpleSiteDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public LocalDateTime getLastUpdated(URL url) throws SQLException {
		
		Connection conn = dataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement(SQLQueries.GET_LAST_UPDATED);
		ps.setString(1, url.toString());
		ResultSet rs = ps.executeQuery();
		rs.next();
		return LocalDateTime.parse(rs.getString(1));
	}
}