package io.github.patfromthe90s.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javax.sql.DataSource;

import io.github.patfromthe90s.exception.RecordNotInDatabaseException;
import io.github.patfromthe90s.util.DaoUtils;
import io.github.patfromthe90s.util.Messages;
import io.github.patfromthe90s.util.SQLQueries;
import io.github.patfromthe90s.util.TimeUtils;

/**
 * Simple implementation of {@link SiteDao}
 * 
 * @author Patrick
 *
 */
public final class SimpleSiteDao implements SiteDao {
	
	private final DataSource dataSource;
	
	/**
	 * @param dataSource The {@link DataSource} for retrieving the database {@link Connection}.
	 */
	public SimpleSiteDao(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public ZonedDateTime getLastUpdated(final String url) throws RecordNotInDatabaseException, SQLException {
		PreparedStatement ps = DaoUtils.getPreparedStatement(dataSource, SQLQueries.GET_LAST_UPDATED);
		ps.setString(1, url.toString()); 
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			LocalDateTime ldt = LocalDateTime.parse(rs.getString(1));
			return ZonedDateTime.of(ldt, TimeUtils.UTC_ZONE_ID);
		} else {
			throw new RecordNotInDatabaseException(Messages.DB_NO_RECORD);
		}
	}
	
	@Override
	public void updateLastUpdated(final String url, final ZonedDateTime newLastUpdated) throws RecordNotInDatabaseException, SQLException {
		PreparedStatement ps = DaoUtils.getPreparedStatement(dataSource, SQLQueries.UPDATE_LAST_UPDATED);
		ps.setString(1, newLastUpdated.toLocalDateTime().toString());
		ps.setString(2, url.toString());
		final int numUpdated = ps.executeUpdate();
		if (numUpdated < 1)
			throw new RecordNotInDatabaseException(Messages.DB_NO_RECORD);
	}
	
}