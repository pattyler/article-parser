package io.github.patfromthe90s.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.exception.SiteServiceException;
import io.github.patfromthe90s.global.GlobalTest;
import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.model.ArticleLinkDate;
import io.github.patfromthe90s.parser.ArticleListParser;
import io.github.patfromthe90s.parser.ArticleParser;

public class SimpleNHKEasyArticleGrabberServiceTest extends GlobalTest {
	
	@Mock private DaoService mDaoService;
	@Mock private SiteService mSiteService;
	@Mock private ArticleListParser mArticleListParser;
	@Mock private ArticleParser mArticleParser;
	
	private ArticleGrabberService articleGrabberService;
	
	@BeforeEach
	public void setup() throws DaoServiceException {
		MockitoAnnotations.initMocks(this);
		doNothing().when(mDaoService).insertArticle(any(Article.class));
		articleGrabberService = new SimpleNHKEasyArticleGrabberService(mSiteService, mDaoService, mArticleListParser, mArticleParser);
	}
	
	@Nested
	@DisplayName("When there are no articles to grab")
	class WhenNoArticles {
		
			@Test
			@DisplayName("When SiteLastUpdated earlier than or same as CurrentLastUpdated, then empty list returned")
			public void thenEmptyList() throws SiteServiceException, DaoServiceException {
				ZonedDateTime mCurrentLastUpdated = ZonedDateTime.now().minusDays(1);
				ZonedDateTime mSiteLastUpdated = ZonedDateTime.now().minusDays(2);	
				when(mDaoService.getLastUpdated(anyString())).thenReturn(mCurrentLastUpdated);
				when(mSiteService.getLastUpdated(anyString())).thenReturn(mSiteLastUpdated);
				assertEquals(0, articleGrabberService.articlesToGrab().size());
				
				mSiteLastUpdated = mCurrentLastUpdated;
				assertEquals(0, articleGrabberService.articlesToGrab().size());
			}
			
			@Test
			@DisplayName("Then nothing is grabbed or persisted")
			public void thenNothingPersisted() throws SiteServiceException, DaoServiceException {
				ZonedDateTime mCurrentLastUpdated = ZonedDateTime.now().minusDays(1);
				ZonedDateTime mSiteLastUpdated = ZonedDateTime.now().minusDays(1);	
				when(mDaoService.getLastUpdated(anyString())).thenReturn(mCurrentLastUpdated);
				when(mSiteService.getLastUpdated(anyString())).thenReturn(mSiteLastUpdated);
				List<ArticleLinkDate> list = new ArrayList<>();
				assertEquals(0, articleGrabberService.grabAndPersist(list));
			}
				
	}
	
	@Nested
	@DisplayName("When there are articles to grab")
	class WhenArticles {
		
		@Test
		@DisplayName("When SiteLastUpdated later than CurrentLastUpdated, then a populated list is returned")
		public void thenListReturned() throws SiteServiceException, DaoServiceException {
			ZonedDateTime mCurrentLastUpdated = ZonedDateTime.now().minusDays(10);
			ZonedDateTime mSiteLastUpdated = ZonedDateTime.now().minusDays(1);	
			ArticleLinkDate mArticleLinkDate = mock(ArticleLinkDate.class);
			when(mArticleLinkDate.getDateTime()).thenReturn(ZonedDateTime.now());
			List<ArticleLinkDate> articleLinkDates = new ArrayList<ArticleLinkDate>();
			articleLinkDates.add(mArticleLinkDate);
			articleLinkDates.add(mArticleLinkDate);
			when(mDaoService.getLastUpdated(anyString())).thenReturn(mCurrentLastUpdated);
			when(mSiteService.getLastUpdated(anyString())).thenReturn(mSiteLastUpdated);
			when(mSiteService.getJson(anyString())).thenReturn("");
			when(mArticleListParser.parse(anyString())).thenReturn(articleLinkDates);
			
			assertEquals(2, articleGrabberService.articlesToGrab().size());
		}
		
		@Test
		@DisplayName("When not all articles are recent, then only recent ones are returned")
		public void whenNotAllNew_thenOnlyNewReturned() throws SiteServiceException, DaoServiceException {
			ZonedDateTime mCurrentLastUpdated = ZonedDateTime.now().minusDays(10);
			ZonedDateTime mSiteLastUpdated = ZonedDateTime.now().minusDays(1);	
			ArticleLinkDate mArticleLinkDate = mock(ArticleLinkDate.class);
			when(mArticleLinkDate.getDateTime()) // one article new, one article older than mCurrentLastUpdated
					.thenReturn(ZonedDateTime.now())
					.thenReturn(ZonedDateTime.now().minusDays(11));
			List<ArticleLinkDate> articleLinkDates = new ArrayList<ArticleLinkDate>();
			articleLinkDates.add(mArticleLinkDate);
			articleLinkDates.add(mArticleLinkDate);
			when(mDaoService.getLastUpdated(anyString())).thenReturn(mCurrentLastUpdated);
			when(mSiteService.getLastUpdated(anyString())).thenReturn(mSiteLastUpdated);
			when(mSiteService.getJson(anyString())).thenReturn("");
			when(mArticleListParser.parse(anyString())).thenReturn(articleLinkDates);
			
			assertEquals(1, articleGrabberService.articlesToGrab().size());
		}
		
		@Test
		@DisplayName("Then list is fully traversed and correct number returned")
		public void thenListFullyTraversed() throws SiteServiceException, DaoServiceException {
			List<ArticleLinkDate> articleLinkDates = new ArrayList<ArticleLinkDate>();
			articleLinkDates.add(new ArticleLinkDate("test1", ZonedDateTime.now().minusDays(4)));
			articleLinkDates.add(new ArticleLinkDate("test2", ZonedDateTime.now().minusDays(3)));
			final Article TEST_ARTICLE = Article.create().setData("test").setDate(ZonedDateTime.now().minusDays(4));
			when(mSiteService.getHtml(anyString())).thenReturn("<html>test</html>");
			when(mArticleParser.parse(anyString())).thenReturn(TEST_ARTICLE);
			
			assertEquals(2, articleGrabberService.grabAndPersist(articleLinkDates));
		}
		
	}
	
	@Nested
	@DisplayName("When checked exception thrown")
	class WhenCheckedExceptionThrown {
		
		@Test
		@DisplayName("Then empty list returned")
		public void thenEmptyList()  throws SiteServiceException, DaoServiceException {
			when(mDaoService.getLastUpdated(anyString()))
					.thenThrow(DaoServiceException.class)
					.thenReturn(ZonedDateTime.now().minusDays(3));
			when(mSiteService.getLastUpdated(anyString()))
					.thenThrow(SiteServiceException.class);
			
			assertEquals(0, articleGrabberService.articlesToGrab().size());
			assertEquals(0, articleGrabberService.articlesToGrab().size());
		}
		
		@Test
		@DisplayName("And is a SiteServiceException, then parsing of list not affected")
		public void isSiteServiceException_thenListStillParsed() throws SiteServiceException, DaoServiceException {
			List<ArticleLinkDate> articleLinkDates = new ArrayList<ArticleLinkDate>();
			articleLinkDates.add(new ArticleLinkDate("test1", ZonedDateTime.now().minusDays(4)));
			articleLinkDates.add(new ArticleLinkDate("test2", ZonedDateTime.now().minusDays(3)));
			articleLinkDates.add(new ArticleLinkDate("test3", ZonedDateTime.now().minusDays(2)));
			final Article TEST_ARTICLE_1 = Article.create().setData("test1").setDate(ZonedDateTime.now().minusDays(4));
			final Article TEST_ARTICLE_3 = Article.create().setData("test3").setDate(ZonedDateTime.now().minusDays(2));
			when(mSiteService.getHtml(anyString()))
					.thenReturn("<html>test</html>")
					.thenThrow(SiteServiceException.class)
					.thenReturn("<html>test</html>");
			when(mArticleParser.parse(anyString()))
					.thenReturn(TEST_ARTICLE_1)
					.thenReturn(TEST_ARTICLE_3);
			
			assertEquals(2, articleGrabberService.grabAndPersist(articleLinkDates));
		}
		
	}
	
}
