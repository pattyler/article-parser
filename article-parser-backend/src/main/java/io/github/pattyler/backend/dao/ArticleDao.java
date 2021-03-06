package io.github.pattyler.backend.dao;

import java.time.ZonedDateTime;
import java.util.List;

import io.github.pattyler.backend.model.Article;

/**
 * DAO interface for accessing the Article table in the database.
 * 
 * @author Patrick
 *
 */
public interface ArticleDao {

	/**
	 * Returns any articles that were written between {@code from} (inclusive) and {@code to} (inclusive).
	 * @param from The date to search from (inclusive).
	 * @param to The date to search until (inclusive).
	 * @return	Any articles written between {@code from} and {@code to}.
	 */
	public List<Article> getArticlesBetween(ZonedDateTime from, ZonedDateTime to);
	
	/**
	 * Attempts to insert the given {@code article} into the database. No guarantees are made.
	 * @param article The article to insert.
	 */
	public void insertArticle(Article article);
	
}
