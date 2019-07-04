package io.github.patfromthe90s.runner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import io.github.patfromthe90s.dao.ArticleDao;
import io.github.patfromthe90s.dao.SimpleArticleDao;
import io.github.patfromthe90s.dao.SimpleSiteDao;
import io.github.patfromthe90s.dao.SiteDao;
import io.github.patfromthe90s.http.Interactor;
import io.github.patfromthe90s.http.SimpleHTMLInteractor;
import io.github.patfromthe90s.http.SimpleJsonInteractor;
import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.parser.ArticleListParser;
import io.github.patfromthe90s.parser.ArticleParser;
import io.github.patfromthe90s.parser.NHKEasyArticleListParser;
import io.github.patfromthe90s.parser.NHKEasyArticleParser;
import io.github.patfromthe90s.service.ArticleGrabberService;
import io.github.patfromthe90s.service.DaoService;
import io.github.patfromthe90s.service.SimpleDaoService;
import io.github.patfromthe90s.service.SimpleNHKEasyArticleGrabberService;
import io.github.patfromthe90s.service.SimpleSiteService;
import io.github.patfromthe90s.service.SiteService;
import io.github.patfromthe90s.util.PropertiesUtil;
import io.github.patfromthe90s.util.PropertyKey;

/**
 * Main runner for the program.
 * 
 * @author Patrick
 *
 */
public class GrabberRunner implements ApplicationRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GrabberRunner.class);
	
	private final ArticleGrabberService articleGrabberService;
	
	public GrabberRunner() {
		// TODO switch to Dependency Injection framework in the future
		PropertiesUtil.load("src\\main\\resources\\app.properties");
		
		SQLiteDataSource ds = new SQLiteDataSource();
		ds.setUrl(PropertiesUtil.get(PropertyKey.DB.URL));
		
		SiteDao siteDao = new SimpleSiteDao(ds);
		ArticleDao articleDao = new SimpleArticleDao(ds);
		
		Interactor htmlInteractor = new SimpleHTMLInteractor();
		Interactor jsonInteractor = new SimpleJsonInteractor();
		
		DaoService daoService = new SimpleDaoService(siteDao, articleDao);
		SiteService siteService = new SimpleSiteService(htmlInteractor, jsonInteractor);
		
		ArticleListParser nhkJsonParser = new NHKEasyArticleListParser(); 
		ArticleParser nhkArticleParser = new NHKEasyArticleParser();
		
		articleGrabberService = new SimpleNHKEasyArticleGrabberService(siteService, daoService, nhkJsonParser, nhkArticleParser);
	}

	public void run() {
		// TODO currently very basic runner. Plan to switch to Dependency Injection framework at the very minimum.
		LOGGER.info("Starting application");
		List<Article> articles = articleGrabberService.grabArticles();
		articleGrabberService.persist(articles);
		articleGrabberService.updateLastUpdated();
		LOGGER.info("Finishing application");
	}
	
	
}
