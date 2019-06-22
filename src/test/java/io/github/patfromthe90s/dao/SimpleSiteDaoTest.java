package io.github.patfromthe90s.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	
	private static final String VALID_URL = "http://www.google.com";
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
		Mockito.when(pStatement.executeQuery()).thenReturn(rs);
		Mockito.when(rs.getString(1)).thenReturn(DATE);
		// end mock setup
		
		simpleSiteDao = new SimpleSiteDao(dataSource);
	}
	
	@Test
	public void testGetLastUpdated() throws MalformedURLException, SQLException {
		URL url = new URL(VALID_URL);
		LocalDateTime ldt = simpleSiteDao.getLastUpdated(url);
		
		Mockito.verify(pStatement).setString(1, VALID_URL);
		Mockito.verify(pStatement).executeQuery();
		assertEquals(ldt, LocalDateTime.parse(DATE));
		
	}

}
