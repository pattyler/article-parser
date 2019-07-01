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
	
	/**
	 * Create a {@link PreparedStatement}.
	 * @param dataSource The data source for getting a {@link Connection}.
	 * @param query The statement to prepare.
	 * @return A prepared statement.
	 * @throws SQLException
	 */
	public static PreparedStatement getPreparedStatement(DataSource dataSource, String query) throws SQLException {
		Connection conn = dataSource.getConnection();
		return conn.prepareStatement(query);
	}

}
