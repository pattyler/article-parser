package io.github.patfromthe90s.viewer;

import java.time.ZonedDateTime;
import java.util.Arrays;

import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.service.DaoService;

public class ArticleViewer {
	
	private final DaoService daoService;
	
	public ArticleViewer(DaoService daoService) {
		this.daoService = daoService;
	}
	
	public void printArticles(ZonedDateTime from, ZonedDateTime to) {
		try {
			daoService.getArticlesBetween(from, to)
					.stream()
					.map(a -> a.getData())
					.forEach(s -> { 
						System.out.println();
						Arrays.stream(s.split("。"))
								.forEach(System.out::println);
						System.out.println();
						
					});
		} catch (DaoServiceException e) {
			e.printStackTrace();
		}
	}

}
