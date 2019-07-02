package io.github.patfromthe90s.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.patfromthe90s.dao.ArticleDao;
import io.github.patfromthe90s.dao.SiteDao;
import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.exception.RecordNotInDatabaseException;
import io.github.patfromthe90s.global.GlobalTest;
import io.github.patfromthe90s.model.Article;

/**
 * Test class for {@link SimpleDaoService}. <br/>
 * <b>Note</b>: Methods have many {@code throws}-clauses due to setting up mocks.
 * @author Patrick
 *
 */
public class SimpleDaoServiceTest extends GlobalTest {
	
	private static final String ASSERT_THROWS_FAIL_MSG = "All exceptions should be mapped to DaoServiceException";
	
	@Mock private SiteDao mSiteDao;
	@Mock private ArticleDao mArticleDao;
	@Mock private DaoService simpleDaoService;
	
	private final String STR_DATE = "Mon, 24 Jun 2019 16:00:00 GMT";
	private final ZonedDateTime VALID_DATE = ZonedDateTime.parse(STR_DATE, DateTimeFormatter.RFC_1123_DATE_TIME)
														.withZoneSameInstant(ZoneId.of("UTC"));
	private final String VALID_URL = "http://www.google.com";
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		simpleDaoService = new SimpleDaoService(mSiteDao, mArticleDao);
	}

	@Test
	@DisplayName("When calling getArticlesBetween() with non-UTC dates, then dates are mapped before calling DAO layer.")
	public void whenGetArticlesBetweenCalledWithNonUtc_thenDateMapped() throws SQLException, DaoServiceException {
		ZonedDateTime nonUtcFrom = ZonedDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(3))).minusDays(2);
		ZonedDateTime nonUtcTo = ZonedDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(3)));
		
		ZonedDateTime utcFrom = nonUtcFrom.withZoneSameInstant(ZoneId.of("UTC"));
		ZonedDateTime utcTo = nonUtcTo.withZoneSameInstant(ZoneId.of("UTC"));
		
		List<Article> articles = new ArrayList<>();
		articles.add(Article.create());
		doReturn(articles)
			.when(mArticleDao)
			.getArticlesBetween(utcFrom, utcTo);
		
		List<Article> returnedArticles = simpleDaoService.getArticlesBetween(nonUtcFrom, nonUtcTo);
		assertEquals(1, returnedArticles.size());
	}

	@Nested
	@DisplayName("When a checked exception if thrown one layer below")
	class WhenCheckedExceptionThrown {
		
		@Test
		@DisplayName("And is RecordNotInDatabae, then correctly mapped.")
		public void andIsRecordNotInDatabase_thenCorrectlyMapped() throws RecordNotInDatabaseException, SQLException {
			when(mSiteDao.getLastUpdated(anyString())).thenThrow(RecordNotInDatabaseException.class);
			doThrow(RecordNotInDatabaseException.class).when(mSiteDao).updateLastUpdated(VALID_URL, VALID_DATE);
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.getLastUpdated(VALID_URL),
					ASSERT_THROWS_FAIL_MSG);
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.updateLastUpdated(VALID_URL, VALID_DATE),
					ASSERT_THROWS_FAIL_MSG);
			
		}
		
		@Test
		@DisplayName("And is SQLException, then correctly mapped.")
		public void getInsertArticleException() throws SQLException, RecordNotInDatabaseException {
			when(mSiteDao.getLastUpdated(anyString())).thenThrow(SQLException.class);
			when(mArticleDao.getArticlesBetween(any(), any())).thenThrow(SQLException.class);
			doThrow(SQLException.class).when(mSiteDao).updateLastUpdated(any(), any());
			doThrow(SQLException.class).when(mArticleDao).insertArticle(any(Article.class));
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.getLastUpdated(VALID_URL),
					ASSERT_THROWS_FAIL_MSG);
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.updateLastUpdated(VALID_URL, ZonedDateTime.now()),
					ASSERT_THROWS_FAIL_MSG);
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.getArticlesBetween(ZonedDateTime.now().minusDays(2), ZonedDateTime.now()),
					ASSERT_THROWS_FAIL_MSG);
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.insertArticle(Article.create()),
					ASSERT_THROWS_FAIL_MSG);
			
		}
	}


}
