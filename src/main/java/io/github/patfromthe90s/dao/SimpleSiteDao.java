package io.github.patfromthe90s.dao;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import io.github.patfromthe90s.util.TimeUtils;

/**
 * Simple implementation of {@link SiteDao}. Delegates mostly to {@link JdbcTemplate}, including exception handling.
 * 
 * @author Patrick
 *
 */
@Component
public final class SimpleSiteDao implements SiteDao {
	
	private final JdbcTemplate JDBC_TEMPLATE;
	private final String GET_LAST_UPDATED_STMT;
	private final String UPDATE_LAST_UPDATED_STMT;
	
	/**
	 * @param dataSource The {@link DataSource} for retrieving the database {@link Connection}.
	 */
	@Autowired
	public SimpleSiteDao(DataSource dataSource, Environment environment) {
		this.JDBC_TEMPLATE = new JdbcTemplate(dataSource);
		this.GET_LAST_UPDATED_STMT = environment.getProperty("sql.get.lastUpdated");
		this.UPDATE_LAST_UPDATED_STMT = environment.getProperty("sql.update.lastUpdated");
	}

	@Override
	public Optional<ZonedDateTime> getLastUpdated(String siteId) {
		List<ZonedDateTime> zdt = JDBC_TEMPLATE.query(GET_LAST_UPDATED_STMT, 
														new Object[] { siteId },
														(rs, rowNum) -> {
															LocalDateTime ldt = LocalDateTime.parse(rs.getString(1));
															return ZonedDateTime.of(ldt, TimeUtils.ZONE_UTC);
														});
		
		return zdt.isEmpty() ? Optional.empty() : Optional.of(zdt.get(0));
	}
	
	@Override
	public int updateLastUpdated(String siteId) {
		final int NUM_UPDATED = JDBC_TEMPLATE.update(UPDATE_LAST_UPDATED_STMT, siteId);	
		return NUM_UPDATED;
	}
	
}