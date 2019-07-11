package io.github.patfromthe90s.grabber.runner;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.patfromthe90s.backend.runner.Runner;
import io.github.patfromthe90s.backend.service.DaoService;
import io.github.patfromthe90s.backend.viewer.ArticleViewer;

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
		articleViewer.printArticles(ZonedDateTime.now().minusDays(5), ZonedDateTime.now());
	}

}
