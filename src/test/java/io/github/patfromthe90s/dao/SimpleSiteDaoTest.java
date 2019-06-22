package io.github.patfromthe90s.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SimpleSiteDaoTest {
	
	private static final String VALID_URL = "http://www.google.com";
	private SiteDao simpleSiteDao;
	
	// Mocked variables
	private DataSource dataSource;
	private Connection conn;
	private Statement statement;
	private ResultSet rs;
	
	@BeforeEach
	public void setup() throws SQLException {
		// begin mock setup
		dataSource = Mockito.mock(DataSource.class);
		conn = Mockito.mock(Connection.class);
		statement = Mockito.mock(Statement.class);
		rs = Mockito.mock(ResultSet.class);
		Mockito.when(dataSource.getConnection()).thenReturn(conn);
		Mockito.when(conn.createStatement()).thenReturn(statement);
		Mockito.when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
		Mockito.when(rs.next()).thenReturn(false);
		// end mock setup
		
		simpleSiteDao = new SimpleSiteDao(dataSource);
	}
	
	@Test
	public void testGetLastUpdated() throws MalformedURLException, SQLException {
		URL url = new URL(VALID_URL);
		simpleSiteDao.getLastUpdated(url);
		
		//Mockito.verify(statement).executeQuery(Mockito.anyString());
	}

}
