package io.github.patfromthe90s.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.patfromthe90s.exception.RecordNotInDatabaseException;
import io.github.patfromthe90s.global.GlobalTest;

public class SimpleSiteDaoTest extends GlobalTest {
	
	private static final String EXISTENT_URL = "http://www.google.com";
	private static final String NON_EXISTENT_URL = "http://www.i-dont-exist-in-the-database.com";
	private static final String DATE = "1990-06-23T14:54:23";
	private static final String THROWS_EXCEPTION_FAIL_MSG = "URL not existing in database should thrown an exception";
	private SiteDao simpleSiteDao;
	
	@Mock private DataSource mDataSource;
	@Mock private Connection mConn;
	@Mock private PreparedStatement mPStatement;
	@Mock private ResultSet mRs;
	
	@BeforeEach
	public void setup() throws SQLException {
		MockitoAnnotations.initMocks(this);
		when(mDataSource.getConnection()).thenReturn(mConn);
		when(mConn.prepareStatement(anyString())).thenReturn(mPStatement);
		simpleSiteDao = new SimpleSiteDao(mDataSource);
	}
	
	@Nested
	@DisplayName("When valid parametemRs passed")
	class WhenValidParametemRsGiven {
		
		@Test
		@DisplayName("When matching record exists in database, then DateTime returned")
		public void whenRecordExists_thenLastUpdatedDatetimeReturned() throws SQLException {
			when(mPStatement.executeQuery()).thenReturn(mRs);
			when(mRs.next()).thenReturn(true);
			when(mRs.getString(1)).thenReturn(DATE);
			
			try {
				ZonedDateTime zdt = simpleSiteDao.getLastUpdated(EXISTENT_URL);
				/** TODO if verify() fails, test still succeeds. Need to research why.
				verify(mPStatement).setString(1, EXISTENT_URL);
				verify(mPStatement).executeQuery();
				*/
				assertEquals(zdt, ZonedDateTime.of(LocalDateTime.parse(DATE), ZoneId.of("UTC")));
			} catch (RecordNotInDatabaseException e) {
				fail();
			}
		}
		
		@Test
		@DisplayName("When matching record exists in database, then lastUpdated column updated")
		public void whenRecordExists_thenDatabaseUpdated() throws SQLException {
			when(mPStatement.executeUpdate()).thenReturn(1);
			ZonedDateTime newLastUpdated = ZonedDateTime.of(LocalDateTime.parse(DATE), ZoneId.of("UTC"));
			
			try {
				simpleSiteDao.updateLastUpdated(EXISTENT_URL, newLastUpdated);
				/*	TODO if verify() fails, test still succeeds. Need to research why.
				verify(mPStatement).setString(1, DATE);
				verify(mPStatement).setString(2, EXISTENT_URL);
				verify(mPStatement).executeUpdate();
				*/
			} catch (RecordNotInDatabaseException e) {
				fail();
			}
		}
		
	}
	
	@Nested
	@DisplayName("When no record found in database")
	class WhenNoRecordInDatabase {
		
		@Test
		@DisplayName("When record matching URL requested, then RecordNotInDatabaseException thrown")
		public void whenUrlRequested_thenExceptionThrown() throws SQLException {
			// thrown in getLastUpdated()
			when(mPStatement.executeQuery()).thenReturn(mRs);
			when(mRs.next()).thenReturn(false);
			when(mRs.getString(anyInt())).thenThrow(new SQLException());
			assertThrows(RecordNotInDatabaseException.class, 
							() -> simpleSiteDao.getLastUpdated(NON_EXISTENT_URL),
							THROWS_EXCEPTION_FAIL_MSG);
		
			// thrown in updateLastUpdated()
			when(mPStatement.executeUpdate()).thenReturn(0);
			ZonedDateTime newLastUpdated = ZonedDateTime.of(LocalDateTime.parse(DATE), ZoneId.of("UTC"));
			assertThrows(RecordNotInDatabaseException.class, 
							() -> simpleSiteDao.updateLastUpdated(NON_EXISTENT_URL, newLastUpdated),
							THROWS_EXCEPTION_FAIL_MSG);
		}
	}
}
