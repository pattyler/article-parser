package io.github.patfromthe90s.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.util.DaoUtils;
import io.github.patfromthe90s.util.PropertiesUtil;
import io.github.patfromthe90s.util.PropertyKey;
import io.github.patfromthe90s.util.TimeUtils;

public final class SimpleArticleDao implements ArticleDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArticleDao.class);
	
	private final DataSource dataSource;
	
	public SimpleArticleDao(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<Article> getArticlesBetween(final ZonedDateTime from, final ZonedDateTime to) throws SQLException {
		List<Article> articles = new ArrayList<>();
		PreparedStatement ps = DaoUtils.getPreparedStatement(dataSource, PropertiesUtil.get(PropertyKey.SQL.GET_ARTICLE));
		ps.setString(1, from.toLocalDateTime().toString());
		ps.setString(2, to.toLocalDateTime().toString());
		LOGGER.info("Preapring to execute statement: [{}] using dates {} and {}", PropertiesUtil.get(PropertyKey.SQL.GET_ARTICLE), from, to);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Article article = Article.create()
									.setUrl(rs.getString(1))
									.setData(rs.getString(2))
									.setDate(ZonedDateTime.of(
												LocalDateTime.parse(rs.getString(3)),
												TimeUtils.UTC_ZONE_ID));
			articles.add(article);
		}
		
		return articles;
	}

	/**
	 * Simple insert that does not guarantee or alert if the insert failed.
	 */
	@Override
	public void insertArticle(final Article article) throws SQLException {
		PreparedStatement ps = DaoUtils.getPreparedStatement(dataSource, PropertiesUtil.get(PropertyKey.SQL.INSERT_ARTICLE));
		ps.setString(1, article.getUrl().toString());
		ps.setString(2, article.getData());
		ps.setString(3, article.getDate().toLocalDateTime().toString());
		LOGGER.info("Preapring to execute statement: [{}] using article {}", PropertiesUtil.get(PropertyKey.SQL.GET_ARTICLE), article);
		ps.executeUpdate();
	}

}
