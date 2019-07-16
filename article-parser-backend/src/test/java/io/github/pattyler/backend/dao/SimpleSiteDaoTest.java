package io.github.pattyler.backend.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import io.github.pattyler.backend.dao.SimpleSiteDao;
import io.github.pattyler.backend.dao.SiteDao;

@JdbcTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ActiveProfiles("test")
@Sql("classpath:schema.sql")
@Sql("classpath:data.sql")
public class SimpleSiteDaoTest {
		
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private Environment environment;
	
	private SiteDao siteDao;
	
	@BeforeEach
	public void setup() {
		siteDao = new SimpleSiteDao(dataSource, environment);
	}
	
	@DisplayName("When a record exists, return the last updated datetime")
	@Test
	public void whenRecordExists_thenReturnLastUpdatedDatetime() { 
		Optional<ZonedDateTime> zdt = siteDao.getLastUpdated("NHKEASY");
		assertNotNull(zdt.get());
	}
	
	@DisplayName("When a record doesn't exists, return empty optional")
	@Test
	public void whenRecordExists_thenReturnEmptyOptional() { 
		Optional<ZonedDateTime> zdt = siteDao.getLastUpdated("NOT_EXIST");
		assertEquals(Optional.empty(), zdt);
	}
	
	@DisplayName("When a record exists, then last_updated is updated")
	@Test
	public void whenRecordExists_thenLastUpdatedIsUpdated() {
		Optional<ZonedDateTime> originalZdt = siteDao.getLastUpdated("NHKEASY");
		siteDao.updateLastUpdated("NHKEASY");
		Optional<ZonedDateTime> updatedZdt = siteDao.getLastUpdated("NHKEASY");
		assertNotEquals(originalZdt, updatedZdt);
	}
	
}
