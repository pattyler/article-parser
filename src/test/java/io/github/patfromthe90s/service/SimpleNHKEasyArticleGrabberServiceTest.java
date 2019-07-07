package io.github.patfromthe90s.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.exception.SiteServiceException;
import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.parser.ArticleListParser;
import io.github.patfromthe90s.parser.ArticleParser;

@SpringBootTest
public class SimpleNHKEasyArticleGrabberServiceTest {
	
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
				assertEquals(0, articleGrabberService.grabArticles().size());
				
				mSiteLastUpdated = mCurrentLastUpdated;
				assertEquals(0, articleGrabberService.grabArticles().size());
			}
							
	}
	
	@Nested
	@DisplayName("When there are articles to grab")
	class WhenArticles {
		
		@Test
		@DisplayName("When SiteLastUpdated later than CurrentLastUpdated, then a populated list is returned")
		public void thenListReturned() throws SiteServiceException, DaoServiceException {
			ZonedDateTime currentLastUpdated = ZonedDateTime.now().minusDays(10);
			ZonedDateTime siteLastUpdated = ZonedDateTime.now().minusDays(1);
			Article testArticle = new Article().setDate(ZonedDateTime.now().minusDays(2))
												.setData("test data")
												.setUrl("http://test.com");
			List<Article> articles = new ArrayList<>();
			articles.add(testArticle);
			articles.add(testArticle);
			when(mDaoService.getLastUpdated(anyString())).thenReturn(currentLastUpdated);
			when(mSiteService.getLastUpdated(anyString())).thenReturn(siteLastUpdated);
			when(mSiteService.getJson(anyString())).thenReturn("[{'val':'test'}]");
			when(mSiteService.getHtml(anyString())).thenReturn("<html>test</html>");
			when(mArticleListParser.parse(anyString())).thenReturn(articles);
			when(mArticleParser.parse(any(Article.class), anyString())).thenReturn(testArticle);
			
			assertEquals(2, articleGrabberService.grabArticles().size());
		}
		
		@Test
		@DisplayName("When not all articles are recent, then only recent ones are returned")
		public void whenNotAllNew_thenOnlyNewReturned() throws SiteServiceException, DaoServiceException {
			ZonedDateTime mCurrentLastUpdated = ZonedDateTime.now().minusDays(10);
			ZonedDateTime mSiteLastUpdated = ZonedDateTime.now().minusDays(1);	
			List<Article> articles = new ArrayList<>();
			articles.add(new Article().setData("test data 1").setUrl("http://test.com").setDate(mCurrentLastUpdated.plusDays(1)));
			articles.add(new Article().setData("test data 2").setUrl("http://test.com").setDate(mCurrentLastUpdated.minusDays(1)));
			when(mDaoService.getLastUpdated(anyString())).thenReturn(mCurrentLastUpdated);
			when(mSiteService.getLastUpdated(anyString())).thenReturn(mSiteLastUpdated);
			when(mSiteService.getJson(anyString())).thenReturn("[{'val':'test'}]");
			when(mSiteService.getHtml(anyString())).thenReturn("<html>test</html>");
			when(mArticleListParser.parse(anyString())).thenReturn(articles);
			when(mArticleParser.parse(any(Article.class), anyString())).thenReturn(articles.get(0))
																		.thenReturn(articles.get(0));
			
			assertEquals(1, articleGrabberService.grabArticles().size());
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
			
			assertEquals(0, articleGrabberService.grabArticles().size());
			assertEquals(0, articleGrabberService.grabArticles().size());
		}
		
		@Test
		@DisplayName("When parsing articles list, then parsing not affected")
		
		public void isSiteServiceException_thenListStillParsed() throws SiteServiceException, DaoServiceException {
			ZonedDateTime currentLastUpdated = ZonedDateTime.now().minusDays(10);
			ZonedDateTime siteLastUpdated = ZonedDateTime.now().minusDays(1);
			// 2nd call throws an exception, so article2 should have null data and only article1 and article3 returned
			Article article1 = new Article().setData("test data 1")
											.setUrl("http://test.com")
											.setDate(currentLastUpdated.plusDays(2));
			Article article2 = new Article().setUrl("http://test.com")
											.setDate(currentLastUpdated.plusDays(3));
			Article article3 = new Article().setData("test data 3")
											.setUrl("http://test.com")
											.setDate(currentLastUpdated.plusDays(4));
			List<Article> articles = new ArrayList<>();
			articles.add(article1);
			articles.add(article2);
			articles.add(article3);
			when(mDaoService.getLastUpdated(anyString())).thenReturn(currentLastUpdated);
			when(mSiteService.getLastUpdated(anyString())).thenReturn(siteLastUpdated);
			when(mSiteService.getJson(anyString())).thenReturn("[{'val':'test'}]");
			when(mArticleListParser.parse(anyString())).thenReturn(articles);
			
			
			when(mSiteService.getHtml(anyString()))
					.thenReturn("<html>test</html>")
					.thenThrow(SiteServiceException.class)
					.thenReturn("<html>test</html>");
			when(mArticleParser.parse(any(Article.class), anyString()))
					.thenAnswer(invocation -> invocation.getArgument(0));
			
			List<Article> returnedArticles = articleGrabberService.grabArticles();
			assertEquals(2, returnedArticles.size());
			assertEquals(article1, returnedArticles.get(0));
			assertEquals(article3, returnedArticles.get(1));
		}
		
	}
	
}
