package io.github.patfromthe90s.grabber.runner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.github.patfromthe90s.backend.model.Article;
import io.github.patfromthe90s.grabber.exception.GrabberServiceException;
import io.github.patfromthe90s.grabber.service.ArticleGrabberService;

/**
 * Using an injected {@link ArticleGrabberService}, grabs articles and updates the database accordingly. <br/>
 * Database persistence is done within a single transaction. Any failures during the persistence stage cause the entire transaction
 * to roll back, resulting in nothing new being persisted.
 * 
 * @author Patrick
 *
 */
@Component
public class GrabberRunner implements Runner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GrabberRunner.class);
	private final ArticleGrabberService articleGrabberService;
	
	@Autowired
	public GrabberRunner(ArticleGrabberService articleGrabberService) {
		this.articleGrabberService = articleGrabberService;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void run() {
		List<Article> articles = articleGrabberService.grabArticles();
		if (articles.size() > 0) {
			try {
				articleGrabberService.persist(articles);
				articleGrabberService.updateLastUpdated();
			} catch (GrabberServiceException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
	
	
}
