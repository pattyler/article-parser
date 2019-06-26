package io.github.patfromthe90s.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Static helper methods for DAO classes / objects
 * 
 * @author Patrick
 *
 */
public class DaoUtils {
	
	public static PreparedStatement getPreparedStatement(DataSource dataSource, String query) throws SQLException {
		Connection conn = dataSource.getConnection();
		return conn.prepareStatement(query);
	}

}
