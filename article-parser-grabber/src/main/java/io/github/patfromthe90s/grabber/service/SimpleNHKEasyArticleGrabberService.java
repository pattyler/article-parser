package io.github.patfromthe90s.grabber.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.github.patfromthe90s.backend.exception.DaoServiceException;
import io.github.patfromthe90s.backend.exception.GrabberServiceException;
import io.github.patfromthe90s.backend.exception.SiteServiceException;
import io.github.patfromthe90s.backend.model.Article;
import io.github.patfromthe90s.backend.parser.ArticleListParser;
import io.github.patfromthe90s.backend.parser.ArticleParser;
import io.github.patfromthe90s.backend.service.DaoService;
import io.github.patfromthe90s.backend.service.SiteService;
import io.github.patfromthe90s.grabber.service.ArticleGrabberService;
import io.github.patfromthe90s.grabber.service.SimpleNHKEasyArticleGrabberService;

@Service
public class SimpleNHKEasyArticleGrabberService implements ArticleGrabberService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNHKEasyArticleGrabberService.class);
	
	private final String JSON_URL;
	private final String SITE_ID;
	
	private final DaoService daoService;
	private final SiteService siteService;
	private final ArticleListParser articleListParser;
	private final ArticleParser articleParser;
	
	@Autowired
	public SimpleNHKEasyArticleGrabberService(SiteService siteService, DaoService daoService, 
												ArticleListParser articleListParser, ArticleParser articleParser,
												Environment environment) {
		this.JSON_URL = environment.getProperty("nhk.url.json");
		this.SITE_ID = environment.getProperty("sql.site.id.nhk");
		this.daoService = daoService;
		this.siteService = siteService;
		this.articleListParser = articleListParser;
		this.articleParser = articleParser;
	}

	@Override
	public List<Article> grabArticles() {
		List<Article> articles = new ArrayList<>();
		
		try {
			ZonedDateTime currentLastUpdated = daoService.getLastUpdated(SITE_ID);
			ZonedDateTime siteLastUpdated = siteService.getLastUpdated(JSON_URL);
			LOGGER.debug("Checking for new articles using current date {} and site date {}", currentLastUpdated, siteLastUpdated);
			if (siteLastUpdated.isAfter(currentLastUpdated)) {	// only request JSON if potentially new articles to grab
				String json = siteService.getJson(JSON_URL);
				articles = articleListParser.parse(json)
												.stream()
												.filter(article -> article.getDate().isAfter(currentLastUpdated))
												.map(article -> {
													try {
														String html = siteService.getHtml(article.getUrl());
														article = articleParser.parse(article, html);
														article.setSiteId(SITE_ID);
													} catch (SiteServiceException e) {
														// can't throw exception in Lambda, so log message, and let filter() stage deal with 
														// null article.getData()
														LOGGER.error(e.getMessage(), e);
													}
													
													return article;
												})
												.filter(article -> article.getData() != null)	// skip articles where siteService.getHtml failed
												.collect(Collectors.toList());
			}
		} catch (SiteServiceException | DaoServiceException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		LOGGER.info("Grabbed {} articles", articles.size());
		return articles;
	}

	@Transactional(propagation=Propagation.MANDATORY, rollbackFor=GrabberServiceException.class)
	@Override
	public void persist(List<Article> articles) throws GrabberServiceException {
		try {
			// Cannot throw checked exception from Streams, so use iteration.
			for (Article article : articles)
				daoService.insertArticle(article);
		} catch (DaoServiceException e) {
			throw new GrabberServiceException(e);
		}
	}

	@Transactional(propagation=Propagation.MANDATORY, rollbackFor=GrabberServiceException.class)
	@Override
	public void updateLastUpdated() throws GrabberServiceException{
		try {
			daoService.updateLastUpdated(SITE_ID);
		} catch (DaoServiceException e) {
			throw new GrabberServiceException(e);
		}
	}

}
