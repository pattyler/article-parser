package io.github.patfromthe90s.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.github.patfromthe90s.model.Article;

@DisplayName("Test logic of SimpleArticleDao")
public class SimpleArticleDaoTest {
	
	private DataSource mDataSource;
	private Connection mConnection;
	private PreparedStatement mPreparedStatement;
	private ResultSet mResultSet;
	
	private ArticleDao articleDao;
	
	@BeforeEach
	public void setup() throws SQLException {
		// begin mock setup
		mDataSource = Mockito.mock(DataSource.class);
		mConnection = Mockito.mock(Connection.class);
		mPreparedStatement = Mockito.mock(PreparedStatement.class);
		mResultSet = Mockito.mock(ResultSet.class);
		Mockito.when(mDataSource.getConnection()).thenReturn(mConnection);
		Mockito.when(mConnection.prepareStatement(Mockito.anyString())).thenReturn(mPreparedStatement);
		// end mock setup
		
		articleDao = new SimpleArticleDao(mDataSource);
	}
	
	@Nested
	@DisplayName("Test for success")
	class success {
		
		@DisplayName("When a result is returned from the database in getArticlesBetween()")
		@Test
		public void testGetArticlesBetween() throws MalformedURLException, SQLException {
			// begin data setup
			final String TEST_DATA = "test";
			final URL URL = new URL("http://test.com");
			final LocalDateTime ARTICLE_DATE = LocalDateTime.of(1995, 8, 24, 23, 40);
			final ZonedDateTime FROM = ZonedDateTime.of(LocalDateTime.of(1990, 8, 24, 23, 40), ZoneId.of("UTC"));
			final ZonedDateTime TO = ZonedDateTime.of(LocalDateTime.of(2015, 06, 24, 23, 15), ZoneId.of("UTC"));
			final Article EXPECTED_ARTICLE = Article.create()
											.setData(TEST_DATA)
											.setDate(ARTICLE_DATE)
											.setUrl(URL);
			// end data setup
			
			// begin mock setup
			Mockito.when(mPreparedStatement.executeQuery()).thenReturn(mResultSet);
			Mockito.when(mResultSet.next()).thenReturn(true).thenReturn(false); // make sure to avoid infinite loop
			Mockito.when(mResultSet.getString(1)).thenReturn(URL.toString());
			Mockito.when(mResultSet.getString(2)).thenReturn(TEST_DATA);
			Mockito.when(mResultSet.getString(3)).thenReturn(ARTICLE_DATE.toString());
			// end mock setup
			
			List<Article> articles = articleDao.getArticlesBetween(FROM, TO);
			assertEquals(1, articles.size());
			assertTrue(EXPECTED_ARTICLE.equals(articles.get(0)));
		}
		
		@DisplayName("When no result is returned from the database in getArticlesBetween()")
		@Test
		public void testGetArticlesBetweenEmpty() throws MalformedURLException, SQLException {
			// begin data setup
			final ZonedDateTime FROM = ZonedDateTime.of(LocalDateTime.of(11990, 8, 24, 23, 40), ZoneId.of("UTC"));
			final ZonedDateTime TO = ZonedDateTime.of(LocalDateTime.of(12015, 06, 24, 23, 15), ZoneId.of("UTC"));
			// end data setup
			
			// begin mock setup
			Mockito.when(mPreparedStatement.executeQuery()).thenReturn(mResultSet);
			Mockito.when(mResultSet.next()).thenReturn(false);
			// end mock setup
			
			List<Article> articles = articleDao.getArticlesBetween(FROM, TO);
			assertEquals(0, articles.size());
		}
		
		@DisplayName("by checking no exception thrown from insertArticle()")
		@Test
		public void testInsertArticleNoException() throws SQLException, MalformedURLException {
			// begin data setup
			final Article article = Article.create()
										.setData("test")
										.setDate(LocalDateTime.now())
										.setUrl(new URL("http://www.google.com"));
			// end data setup
			
			// being mock setup
			Mockito.when(mPreparedStatement.executeUpdate()).thenReturn(1);
			// end mock setup
			
			try {
				articleDao.insertArticle(article);
			} catch (SQLException e) {
				fail("Unexpected exception thrown");
				throw e;
			}
		}
		
	}

}
