package io.github.patfromthe90s.runner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.service.ArticleGrabberService;

/**
 * Main runner for the program.
 * 
 * @author Patrick
 *
 */
@Component
public class GrabberRunner implements Runner {
	
	private final ArticleGrabberService articleGrabberService;
	
	@Autowired
	public GrabberRunner(ArticleGrabberService articleGrabberService) {
		this.articleGrabberService = articleGrabberService;
	}

	public void run() {
		List<Article> articles = articleGrabberService.grabArticles();
		if (articles.size() > 0) {
			articleGrabberService.persist(articles);
			articleGrabberService.updateLastUpdated();
		}
	}
	
	
}
