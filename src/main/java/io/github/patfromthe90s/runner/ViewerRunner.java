package io.github.patfromthe90s.runner;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.patfromthe90s.service.DaoService;
import io.github.patfromthe90s.viewer.ArticleViewer;

/**
 * A testing class just for viewing articles
 * @author Patrick
 *
 */
@Component
public class ViewerRunner implements Runner {
	
	private final ArticleViewer articleViewer;
	
	@Autowired
	public ViewerRunner(DaoService daoService) {
		articleViewer = new ArticleViewer(daoService);
	}
	
	public void run() {
		articleViewer.printArticles(ZonedDateTime.now().minusDays(2), ZonedDateTime.now());
	}

}
