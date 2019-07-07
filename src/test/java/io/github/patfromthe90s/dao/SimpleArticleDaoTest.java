package io.github.patfromthe90s.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import io.github.patfromthe90s.model.Article;

@SpringBootTest
@ActiveProfiles("test")
public class SimpleArticleDaoTest {
	
	@MockBean private DataSource mDataSource;
	@Mock private Connection mConnection;
	@Mock private PreparedStatement mPreparedStatement;
	@Mock private ResultSet mResultSet;
	
	@Autowired
	private ArticleDao articleDao;
	
	@BeforeEach
	public void setup() throws SQLException {
		MockitoAnnotations.initMocks(this);
		when(mDataSource.getConnection()).thenReturn(mConnection);
		when(mConnection.prepareStatement(anyString())).thenReturn(mPreparedStatement);
	}
	
	@Nested
	@DisplayName("When two dates are given")
	class whenValidDatesGiven {
		
		@DisplayName("When results from database, then populated list returned.")
		@Test
		public void whenResults_thenListReturned() throws MalformedURLException, SQLException {
			final String TEST_DATA = "test";
			final String STR_URL = "http://test.com";
			final URL URL = new URL(STR_URL);
			final LocalDateTime LOCAL_ARTICLE_DATE = LocalDateTime.of(1995, 8, 24, 23, 40);
			final ZonedDateTime ARTICLE_DATE = ZonedDateTime.of(LOCAL_ARTICLE_DATE, ZoneId.of("UTC"));
			final ZonedDateTime FROM = ZonedDateTime.of(LocalDateTime.of(1990, 8, 24, 23, 40), ZoneId.of("UTC"));
			final ZonedDateTime TO = ZonedDateTime.of(LocalDateTime.of(2015, 06, 24, 23, 15), ZoneId.of("UTC"));
			final Article EXPECTED_ARTICLE = new Article().setData(TEST_DATA)
														.setDate(ARTICLE_DATE)
														.setUrl(STR_URL);
			
			when(mPreparedStatement.executeQuery()).thenReturn(mResultSet);
			when(mResultSet.next()).thenReturn(true).thenReturn(false); // make sure to avoid infinite loop
			when(mResultSet.getString(1)).thenReturn(URL.toString());
			when(mResultSet.getString(2)).thenReturn(TEST_DATA);
			when(mResultSet.getString(3)).thenReturn(LOCAL_ARTICLE_DATE.toString());
			
			List<Article> articles = articleDao.getArticlesBetween(FROM, TO);
			assertEquals(1, articles.size());
			assertTrue(EXPECTED_ARTICLE.equals(articles.get(0)));
		}
		
		@DisplayName("When no results from database, then empty list returned. ")
		@Test
		public void whenNoResults_thenEmptyListReturned() throws MalformedURLException, SQLException {
			final ZonedDateTime FROM = ZonedDateTime.of(LocalDateTime.of(11990, 8, 24, 23, 40), ZoneId.of("UTC"));
			final ZonedDateTime TO = ZonedDateTime.of(LocalDateTime.of(12015, 06, 24, 23, 15), ZoneId.of("UTC"));
			when(mPreparedStatement.executeQuery()).thenReturn(mResultSet);
			when(mResultSet.next()).thenReturn(false);
			
			List<Article> articles = articleDao.getArticlesBetween(FROM, TO);
			assertEquals(0, articles.size());
		}
		
	}

}
