package io.github.patfromthe90s.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.exception.SiteServiceException;
import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.parser.ArticleListParser;
import io.github.patfromthe90s.parser.ArticleParser;
import io.github.patfromthe90s.util.PropertiesUtil;
import io.github.patfromthe90s.util.PropertyKey;

public class SimpleNHKEasyArticleGrabberService implements ArticleGrabberService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNHKEasyArticleGrabberService.class);
	private static final String SITE_URL = PropertiesUtil.get(PropertyKey.NHK.BASE_URL);
	private static final String JSON_URL = PropertiesUtil.get(PropertyKey.NHK.JSON_URL);
	
	private final DaoService daoService;
	private final SiteService siteService;
	private final ArticleListParser articleListParser;
	private final ArticleParser articleParser;
	
	public SimpleNHKEasyArticleGrabberService(SiteService siteService, DaoService daoService, ArticleListParser articleListParser, ArticleParser articleParser) {
		this.daoService = daoService;
		this.siteService = siteService;
		this.articleListParser = articleListParser;
		this.articleParser = articleParser;
	}

	@Override
	public List<Article> grabArticles() {
		List<Article> articles = new ArrayList<>();
		
		try {
			ZonedDateTime currentLastUpdated = daoService.getLastUpdated(SITE_URL);
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
													} catch (SiteServiceException e) {
														LOGGER.error(e.getMessage(), e);
													}
													
													return article;
												})
												.filter(article -> article.getData() != null)	// skip articles where siteService.getHtml failed
												.collect(Collectors.toList());
			}
		} catch (SiteServiceException | DaoServiceException e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		}
		
		LOGGER.info("Grabbed {} articles", articles.size());
		LOGGER.debug("Grabbed articles are: {}", articles);
		return articles;
	}

	@Override
	public void persist(List<Article> articles) {
		
		articles.stream()
				.forEach(article -> {
					try {
						daoService.insertArticle(article);
					} catch (DaoServiceException e) {
						LOGGER.error(e.getMessage(), e);
					}
				});
	}

	@Override
	public void updateLastUpdated() {
		// TODO this should be done using the Articles, rather than another HTTP request? Maybe an SQL statement in the database
		// Reason for using HTTP request is original plan was to have a notification if there are new articles (without grabbing them)
		try {
			ZonedDateTime siteLastUpdated = siteService.getLastUpdated(JSON_URL);
			daoService.updateLastUpdated(SITE_URL, siteLastUpdated);
		} catch (SiteServiceException | DaoServiceException e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

}
