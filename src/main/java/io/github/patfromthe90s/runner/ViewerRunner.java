package io.github.patfromthe90s.runner;

import java.time.ZonedDateTime;

import org.sqlite.SQLiteDataSource;

import io.github.patfromthe90s.dao.ArticleDao;
import io.github.patfromthe90s.dao.SimpleArticleDao;
import io.github.patfromthe90s.dao.SimpleSiteDao;
import io.github.patfromthe90s.dao.SiteDao;
import io.github.patfromthe90s.service.DaoService;
import io.github.patfromthe90s.service.SimpleDaoService;
import io.github.patfromthe90s.util.PropertiesUtil;
import io.github.patfromthe90s.util.PropertyKey;
import io.github.patfromthe90s.viewer.ArticleViewer;

public class ViewerRunner implements ApplicationRunner {
	
	private final ArticleViewer articleViewer;
	
	public ViewerRunner() {
		// TODO switch to Dependency Injection framework in the future
		PropertiesUtil.load("src\\main\\resources\\app.properties");
				
		SQLiteDataSource ds = new SQLiteDataSource();
		ds.setUrl(PropertiesUtil.get(PropertyKey.DB.URL));
		
		SiteDao siteDao = new SimpleSiteDao(ds);
		ArticleDao articleDao = new SimpleArticleDao(ds);
		DaoService daoService = new SimpleDaoService(siteDao, articleDao);

		articleViewer = new ArticleViewer(daoService);
	}
	
	public void run() {
		articleViewer.printArticles(ZonedDateTime.now().minusDays(7), ZonedDateTime.now());
	}

}
