package io.github.patfromthe90s.dao;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.List;

import io.github.patfromthe90s.model.Article;

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
	 * @throws SQLException if something went wrong.
	 */
	public List<Article> getArticlesBetween(ZonedDateTime from, ZonedDateTime to) throws SQLException;
	
	/**
	 * Attempts to insert the given {@code article} into the database. No guarantees are made.
	 * @param article The article to insert.
	 * @throws SQLException if something went wrong
	 */
	public void insertArticle(Article article) throws SQLException;
	
}
