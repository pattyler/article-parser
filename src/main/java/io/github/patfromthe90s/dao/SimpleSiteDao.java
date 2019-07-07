package io.github.patfromthe90s.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.patfromthe90s.exception.RecordNotInDatabaseException;
import io.github.patfromthe90s.util.DaoUtils;
import io.github.patfromthe90s.util.TimeUtils;

/**
 * Simple implementation of {@link SiteDao}
 * 
 * @author Patrick
 *
 */
@Component
public final class SimpleSiteDao implements SiteDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSiteDao.class);
	
	private final DataSource dataSource;
	
	@Value("${sql.get.lastUpdated}")
	private String getLastUpdatedStmt;
	
	@Value("${sql.update.lastUpdated}")
	private String updateLastUpdatedStmt;
	
	@Value("${messages.db.noRecord}")
	private String dbNoRecordMsg;
	
	/**
	 * @param dataSource The {@link DataSource} for retrieving the database {@link Connection}.
	 */
	@Autowired
	public SimpleSiteDao(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public ZonedDateTime getLastUpdated(final String url) throws RecordNotInDatabaseException, SQLException {
		PreparedStatement ps = DaoUtils.getPreparedStatement(dataSource, getLastUpdatedStmt);
		ps.setString(1, url);
		LOGGER.info("Preapring to execute statement: [{}] using url {}", getLastUpdatedStmt, url);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			LocalDateTime ldt = LocalDateTime.parse(rs.getString(1));
			return ZonedDateTime.of(ldt, TimeUtils.ZONE_UTC);
		} else {
			throw new RecordNotInDatabaseException(dbNoRecordMsg);
		}
	}
	
	@Override
	public void updateLastUpdated(final String url) throws RecordNotInDatabaseException, SQLException {
		PreparedStatement ps = DaoUtils.getPreparedStatement(dataSource, updateLastUpdatedStmt);
		ps.setString(1, url.toString());
		LOGGER.info("Preapring to execute statement: [{}] using url {} and date {}", updateLastUpdatedStmt, url);
		final int numUpdated = ps.executeUpdate();
		if (numUpdated < 1)
			throw new RecordNotInDatabaseException(dbNoRecordMsg);
	}
	
}