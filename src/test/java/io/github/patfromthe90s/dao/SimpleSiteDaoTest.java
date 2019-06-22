package io.github.patfromthe90s.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SimpleSiteDaoTest {
	
	private static final String EXISTENT_URL = "http://www.google.com";
	private static final String NON_EXISTENT_URL = "http://www.i-dont-exist-in-the-database.com";
	private static final String DATE = "1990-06-23T14:54:23";
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
	
	@Test
	public void testGetLastUpdatedSuccess() throws MalformedURLException, SQLException {
		// begin mock setup
		Mockito.when(pStatement.executeQuery()).thenReturn(rs);
		Mockito.when(rs.getString(1)).thenReturn(DATE);
		// end mock setup
		
		URL url = new URL(EXISTENT_URL);
		LocalDateTime ldt = simpleSiteDao.getLastUpdated(url);
		Mockito.verify(pStatement).setString(1, EXISTENT_URL);
		Mockito.verify(pStatement).executeQuery();
		assertEquals(ldt, LocalDateTime.parse(DATE));
	}
	
	@Test
	public void testGetLastUpdatedEmpty() throws MalformedURLException, SQLException {
		// begin mock setup
		Mockito.when(pStatement.executeQuery()).thenReturn(rs);
		Mockito.when(rs.next()).thenReturn(false);
		Mockito.when(rs.getString(Mockito.anyInt())).thenThrow(new SQLException());
		// end mock setup
		
		
		URL url = new URL(NON_EXISTENT_URL);
		assertThrows(SQLException.class, () -> simpleSiteDao.getLastUpdated(url), "URL not existing in database should thrown an exception");
	}
	
	@Test
	public void testUpdateLastUpdatedSuccess() throws MalformedURLException, SQLException {
		// begin mock setup
		Mockito.when(pStatement.executeUpdate()).thenReturn(1);
		// end mock setup
		
		URL url = new URL(EXISTENT_URL);
		LocalDateTime newLastUpdated = LocalDateTime.parse(DATE);
		boolean updated = simpleSiteDao.updateLastUpdated(url, newLastUpdated);
		Mockito.verify(pStatement).setString(1, DATE);
		Mockito.verify(pStatement).setString(2, EXISTENT_URL);
		Mockito.verify(pStatement).executeUpdate();
		assertEquals(true, updated);
	}
	
	@Test
	public void testUpdateLastUpdatedFailWrongUrl() throws MalformedURLException, SQLException {
		// begin mock setup
		Mockito.when(pStatement.executeUpdate()).thenReturn(0);
		// end mock setup
		
		URL url = new URL(NON_EXISTENT_URL);
		LocalDateTime newLastUpdated = LocalDateTime.parse(DATE);
		boolean updated = simpleSiteDao.updateLastUpdated(url, newLastUpdated);
		assertEquals(false, updated);
	}

}
