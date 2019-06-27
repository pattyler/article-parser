package io.github.patfromthe90s.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;
import java.net.URL;
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
import org.mockito.Mockito;

import io.github.patfromthe90s.exception.RecordNotInDatabaseException;

@DisplayName("Test the logic in SimpleSiteDao")
public class SimpleSiteDaoTest {
	
	private static final String EXISTENT_URL = "http://www.google.com";
	private static final String NON_EXISTENT_URL = "http://www.i-dont-exist-in-the-database.com";
	private static final String DATE = "1990-06-23T14:54:23";
	private static final String THROWS_EXCEPTION_FAIL_MSG = "URL not existing in database should thrown an exception";
	private SiteDao simpleSiteDao;
	
	// Mocked variables
	private DataSource dataSource;
	private Connection conn;
	private PreparedStatement pStatement;
	private ResultSet rs;
	
	@BeforeEach
	public void setup() throws SQLException {
		// begin mock setup
		dataSource = Mockito.mock(DataSource.class);
		conn = Mockito.mock(Connection.class);
		pStatement = Mockito.mock(PreparedStatement.class);
		rs = Mockito.mock(ResultSet.class);
		
		Mockito.when(dataSource.getConnection()).thenReturn(conn);
		Mockito.when(conn.prepareStatement(Mockito.anyString())).thenReturn(pStatement);
		// end mock setup
		
		simpleSiteDao = new SimpleSiteDao(dataSource);
	}
	
	@Nested
	@DisplayName("Success when the parameters are valid")
	class Success {
		
		@Test
		@DisplayName("For getLastUpdated()")
		public void testGetLastUpdatedSuccess() throws MalformedURLException, SQLException {
			// begin mock setup
			Mockito.when(pStatement.executeQuery()).thenReturn(rs);
			Mockito.when(rs.next()).thenReturn(true);
			Mockito.when(rs.getString(1)).thenReturn(DATE);
			// end mock setup
			
			URL url = new URL(EXISTENT_URL);
			try {
				ZonedDateTime zdt = simpleSiteDao.getLastUpdated(url);
				Mockito.verify(pStatement).setString(1, EXISTENT_URL);
				Mockito.verify(pStatement).executeQuery();
				assertEquals(zdt, ZonedDateTime.of(LocalDateTime.parse(DATE), ZoneId.of("UTC")));
			} catch (RecordNotInDatabaseException e) {
				fail();
			}
		}
		
		@Test
		@DisplayName("For updateLastUpdated()")
		public void testUpdateLastUpdatedSuccess() throws MalformedURLException, SQLException {
			// begin mock setup
			Mockito.when(pStatement.executeUpdate()).thenReturn(1);
			// end mock setup
			
			URL url = new URL(EXISTENT_URL);
			ZonedDateTime newLastUpdated = ZonedDateTime.of(LocalDateTime.parse(DATE), ZoneId.of("UTC"));
			try {
				simpleSiteDao.updateLastUpdated(url, newLastUpdated);
				Mockito.verify(pStatement).setString(1, DATE);
				Mockito.verify(pStatement).setString(2, EXISTENT_URL);
				Mockito.verify(pStatement).executeUpdate();
			} catch (RecordNotInDatabaseException e) {
				fail();
			}
		}
		
	}
	
	@Nested
	@DisplayName("Exceptions are thrown correctly")
	class ExceptionsThrown {
		@Nested
		@DisplayName("When URL is not in the database")
		class URLNotInDb {
			@Test
			@DisplayName("in getLastUpdated()")
			public void testGetLastUpdatedEmpty() throws MalformedURLException, SQLException {
				// begin mock setup
				Mockito.when(pStatement.executeQuery()).thenReturn(rs);
				Mockito.when(rs.next()).thenReturn(false);
				Mockito.when(rs.getString(Mockito.anyInt())).thenThrow(new SQLException());
				// end mock setup
				
				
				URL url = new URL(NON_EXISTENT_URL);
				assertThrows(RecordNotInDatabaseException.class, 
								() -> simpleSiteDao.getLastUpdated(url),
								THROWS_EXCEPTION_FAIL_MSG);
			}
			
			@Test
			@DisplayName("in updateLastUpdated()")
			public void testUpdateLastUpdatedFailWrongUrl() throws MalformedURLException, SQLException {
				// begin mock setup
				Mockito.when(pStatement.executeUpdate()).thenReturn(0);
				// end mock setup
				
				URL url = new URL(NON_EXISTENT_URL);
				ZonedDateTime newLastUpdated = ZonedDateTime.of(LocalDateTime.parse(DATE), ZoneId.of("UTC"));
				assertThrows(RecordNotInDatabaseException.class, 
								() -> simpleSiteDao.updateLastUpdated(url, newLastUpdated),
								THROWS_EXCEPTION_FAIL_MSG);
			}
		}
	}

}
