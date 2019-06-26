package io.github.patfromthe90s.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.util.DaoUtils;
import io.github.patfromthe90s.util.SQLQueries;

public final class SimpleArticleDao implements ArticleDao {
	
	private final DataSource dataSource;
	
	public SimpleArticleDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<Article> getArticlesBetween(LocalDateTime from, LocalDateTime to) throws SQLException {
		List<Article> articles = new ArrayList<>();
		Connection conn = dataSource.getConnection();
		PreparedStatement ps = conn.prepareStatement(SQLQueries.GET_ARTICLE);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			try {
				Article article = Article.create()
										.setUrl(new URL(rs.getString(1)))
										.setData(rs.getString(2))
										.setDate(LocalDateTime.parse(rs.getString(3)));
				articles.add(article);
			} catch (MalformedURLException e) {
				// re-throw? log?
			}
		}
		
		return articles;
	}

	/**
	 * Simple insert that does not guarantee or alert if the insert failed.
	 */
	@Override
	public void insertArticle(Article article) throws SQLException {
		PreparedStatement ps = DaoUtils.getPreparedStatement(dataSource, SQLQueries.INSERT_ARTICLE);
		ps.setString(1, article.getUrl().toString());
		ps.setString(2, article.getData());
		ps.setString(2, article.getDate().toString());
		ps.executeUpdate();
	}

}
