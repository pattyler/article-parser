package io.github.patfromthe90s.viewer;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.service.DaoService;

public class ArticleViewer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleViewer.class);
	private final DaoService daoService;
	
	public ArticleViewer(DaoService daoService) {
		this.daoService = daoService;
	}
	
	public void printArticles(ZonedDateTime from, ZonedDateTime to) {
		try {
			daoService.getArticlesBetween(from, to)
					.stream()
					.map(Article::toString)
					.forEach(LOGGER::info);
		} catch (DaoServiceException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
