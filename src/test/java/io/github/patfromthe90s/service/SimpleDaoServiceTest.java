package io.github.patfromthe90s.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.github.patfromthe90s.dao.ArticleDao;
import io.github.patfromthe90s.dao.SiteDao;
import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.exception.RecordNotInDatabaseException;
import io.github.patfromthe90s.model.Article;

/**
 * Test class for {@link SimpleDaoService}. <br/>
 * <b>Note</b>: Methods have many {@code throws}-clauses due to setting up mocks.
 * @author Patrick
 *
 */
@DisplayName("Test SimpleDAOServce")
public class SimpleDaoServiceTest {
	
	private SiteDao mSiteDao;
	private ArticleDao mArticleDao;
	
	private DaoService simpleDaoService;
	private URL url;
	
	private static final String ASSERT_THROWS_FAIL_MSG = "All exceptions should be mapped to DaoServiceException";
	private final String STR_DATE = "Mon, 24 Jun 2019 16:00:00 GMT";
	private final ZonedDateTime VALID_DATE = ZonedDateTime.parse(STR_DATE, DateTimeFormatter.RFC_1123_DATE_TIME)
														.withZoneSameInstant(ZoneId.of("UTC"));
	private final String VALID_URL = "http://www.google.com";
	
	@BeforeEach
	public void setup() throws MalformedURLException {
		// being mock setup
		mSiteDao = Mockito.mock(SiteDao.class);
		mArticleDao = Mockito.mock(ArticleDao.class);
		// end mock setup
		
		simpleDaoService = new SimpleDaoService(mSiteDao, mArticleDao);
		url = new URL(VALID_URL);
	}
	
	@Nested
	@DisplayName("A value is returned")
	class ValuesReturned {
		
		@Test
		@DisplayName("From getLastUpdated()")
		public void testLastUpdatedReturned() throws RecordNotInDatabaseException, SQLException, DaoServiceException {
			// being mock setup
			Mockito.when(mSiteDao.getLastUpdated(Mockito.any(URL.class))).thenReturn(VALID_DATE);
			// end mock setup
			
			ZonedDateTime returnedDate = simpleDaoService.getLastUpdated(url);
			assertEquals(VALID_DATE, returnedDate);
		}
		
		@Test
		@DisplayName("From getArticlesBetween()")
		public void testGetArticlesBetweenReturned() throws SQLException, DaoServiceException {
			// being mock setup
			final List<Article> ARTICLES = new ArrayList<>();
			ARTICLES.add(Article.create());
			Mockito.doReturn(ARTICLES)
				.when(mArticleDao)
				.getArticlesBetween(Mockito.any(ZonedDateTime.class), Mockito.any(ZonedDateTime.class));
			// end mock setup
			
			List<Article> returnedArticles = simpleDaoService.getArticlesBetween(VALID_DATE, VALID_DATE);
			assertEquals(1, returnedArticles.size());
		}
	}
	
	@Nested
	@DisplayName("Correct exceptions are thrown")
	class ExceptionsThrown {
		
		@Test
		@DisplayName("From getLastUpdated()")
		public void testExceptionGetLastUpdatedException() throws RecordNotInDatabaseException, SQLException {
			// being mock setup
			Mockito.when(mSiteDao.getLastUpdated(Mockito.any(URL.class))).thenThrow(RecordNotInDatabaseException.class);
			// end mock setup
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.getLastUpdated(url),
					ASSERT_THROWS_FAIL_MSG);
			
			// being mock setup
			Mockito.when(mSiteDao.getLastUpdated(Mockito.any(URL.class))).thenThrow(SQLException.class);
			// end mock setup
			
			assertThrows(DaoServiceException.class, 
					() ->  simpleDaoService.getLastUpdated(url),
					ASSERT_THROWS_FAIL_MSG);
		}
		
		@Test
		@DisplayName("From updateLastUpdated()")
		public void testUpdateLastUpdatedException() throws MalformedURLException, SQLException, RecordNotInDatabaseException {
			// being mock setup
			Mockito.doThrow(RecordNotInDatabaseException.class).when(mSiteDao).updateLastUpdated(url, VALID_DATE);
			// end mock setup
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.updateLastUpdated(url, VALID_DATE),
					ASSERT_THROWS_FAIL_MSG);
			
			// being mock setup
			Mockito.doThrow(SQLException.class).when(mSiteDao).updateLastUpdated(url, VALID_DATE);
			// end mock setup
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.updateLastUpdated(url, VALID_DATE),
					ASSERT_THROWS_FAIL_MSG);
		}
		
		@Test
		@DisplayName("From getAticlesBetween()")
		public void testGetArticlesBetweenException() throws SQLException {
			// being mock setup
			Mockito.doThrow(SQLException.class)
					.when(mArticleDao)
					.getArticlesBetween(VALID_DATE, VALID_DATE);
			// end mock setup
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.getArticlesBetween(VALID_DATE, VALID_DATE),
					ASSERT_THROWS_FAIL_MSG);
			
		}
		
		@Test
		@DisplayName("From insertArticle()")
		public void getInsertArticleException() throws SQLException {
			// being mock setup
			Mockito.doThrow(SQLException.class)
					.when(mArticleDao)
					.insertArticle(Mockito.any(Article.class));
			// end mock setup
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.insertArticle(Article.create()),
					ASSERT_THROWS_FAIL_MSG);
			
		}
	}


}
