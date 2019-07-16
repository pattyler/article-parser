package io.github.pattyler.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import io.github.pattyler.backend.dao.ArticleDao;
import io.github.pattyler.backend.dao.SiteDao;
import io.github.pattyler.backend.exception.DaoServiceException;
import io.github.pattyler.backend.model.Article;
import io.github.pattyler.backend.service.DaoService;
import io.github.pattyler.backend.service.SimpleDaoService;

/**
 * Test class for {@link SimpleDaoService}. <br/>
 * <b>Note</b>: Methods have many {@code throws}-clauses due to setting up mocks.
 * @author Patrick
 *
 */
public class SimpleDaoServiceTest {

	@Mock private SiteDao mSiteDao;
	@Mock private ArticleDao mArticleDao;
	@Mock private Environment mEnvironment;
	
	private DaoService simpleDaoService;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		simpleDaoService = new SimpleDaoService(mSiteDao, mArticleDao, mEnvironment);
	}

	@Test
	@DisplayName("When calling getArticlesBetween() with non-UTC dates, then dates are mapped before calling DAO layer.")
	public void whenGetArticlesBetweenCalledWithNonUtc_thenDateMapped() throws SQLException, DaoServiceException {
		ZonedDateTime nonUtcFrom = ZonedDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(3))).minusDays(2);
		ZonedDateTime nonUtcTo = ZonedDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(3)));
		
		ZonedDateTime utcFrom = nonUtcFrom.withZoneSameInstant(ZoneId.of("UTC"));
		ZonedDateTime utcTo = nonUtcTo.withZoneSameInstant(ZoneId.of("UTC"));
		
		List<Article> articles = new ArrayList<>();
		articles.add(new Article());
		doReturn(articles)
			.when(mArticleDao)
			.getArticlesBetween(utcFrom, utcTo);
		
		List<Article> returnedArticles = simpleDaoService.getArticlesBetween(nonUtcFrom, nonUtcTo);
		assertEquals(1, returnedArticles.size());
	}
	
	@DisplayName("When getLastUpdated() returns empty date, then DaoServiceException is thrown")
	public void whenGetLastUpdatedEmpty_thenExceptionThrown() {
		when(mSiteDao.getLastUpdated(anyString())).thenReturn(Optional.empty());
		assertThrows(DaoServiceException.class, () -> simpleDaoService.getLastUpdated("NHKEASY"));
	}

}
