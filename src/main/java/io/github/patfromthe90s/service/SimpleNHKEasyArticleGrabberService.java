package io.github.patfromthe90s.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.exception.SiteServiceException;
import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.model.ArticleLinkDate;
import io.github.patfromthe90s.parser.ArticleParser;
import io.github.patfromthe90s.parser.ArticleListParser;
import io.github.patfromthe90s.util.TempProperties;

public class SimpleNHKEasyArticleGrabberService implements ArticleGrabberService {
	
	private static final String SITE_URL = TempProperties.NHK_BASE_URL;
	private static final String JSON_URL = TempProperties.NHK_JSON_URL;
	
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
	public List<ArticleLinkDate> articlesToGrab() {
		List<ArticleLinkDate> articlesToGrab = new ArrayList<>();
		
		try {
			ZonedDateTime currentLastUpdated = daoService.getLastUpdated(SITE_URL);
			ZonedDateTime siteLastUpdated = siteService.getLastUpdated(JSON_URL);
			if (siteLastUpdated.isAfter(currentLastUpdated)) {
				String json = siteService.getJson(JSON_URL);
				articlesToGrab = articleListParser.parse(json)
												.stream()
												.filter(ald -> ald.getDateTime().isAfter(currentLastUpdated))
												.collect(Collectors.toList());
			}
		} catch (SiteServiceException | DaoServiceException e) {
			// log this
			e.printStackTrace();
		}
		
		return articlesToGrab;
	}

	@Override
	public int grabAndPersist(List<ArticleLinkDate> articleLinkDates) {
		int numGrabbed = 0;
		for (ArticleLinkDate ald : articleLinkDates) {
			try {
				String articleHtml = siteService.getHtml(ald.getUrl());
				Article parsedArticle = articleParser.parse(articleHtml);
				parsedArticle.setUrl(ald.getUrl());
				daoService.insertArticle(parsedArticle);
				numGrabbed++;
			} catch (SiteServiceException | DaoServiceException e) {
				// log this
				e.printStackTrace();
			}
		}
		
		return numGrabbed;
	}

	@Override
	public void updateLastUpdated() {
		try {
			ZonedDateTime siteLastUpdated = siteService.getLastUpdated(JSON_URL);
			daoService.updateLastUpdated(SITE_URL, siteLastUpdated);
		} catch (SiteServiceException | DaoServiceException e) {
			// log this
			e.printStackTrace();
		}
	}

}
