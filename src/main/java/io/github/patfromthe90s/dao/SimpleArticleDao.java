package io.github.patfromthe90s.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.util.DaoUtils;
import io.github.patfromthe90s.util.TimeUtils;

@Component
public final class SimpleArticleDao implements ArticleDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArticleDao.class);
	
	private final DataSource dataSource;
	
	@Value("${sql.get.article}")
	private String getArticleStmt;
	
	@Value("${sql.insert.article}")
	private String insertArticleStmt;
	
	@Autowired
	public SimpleArticleDao(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public List<Article> getArticlesBetween(final ZonedDateTime from, final ZonedDateTime to) throws SQLException {
		List<Article> articles = new ArrayList<>();
		String strFrom = from.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		String strTo = to.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		//PreparedStatement ps = DaoUtils.getPreparedStatement(dataSource, PropertiesUtil.get(PropertiesKey.SQL.GET_ARTICLE));
		PreparedStatement ps = DaoUtils.getPreparedStatement(dataSource, getArticleStmt);
		ps.setString(1, strFrom);
		ps.setString(2, strTo);
		//LOGGER.info("Preapring to execute statement: [{}] using dates {} and {}", PropertiesUtil.get(PropertiesKey.SQL.GET_ARTICLE), strFrom, strTo);
		LOGGER.info("Preapring to execute statement: [{}] using dates {} and {}", getArticleStmt, strFrom, strTo);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Article article = new Article().setUrl(rs.getString(1))
									.setData(rs.getString(2))
									.setDate(ZonedDateTime.of(
												LocalDateTime.parse(rs.getString(3)),
												TimeUtils.ZONE_UTC));
			articles.add(article);
		}
		
		return articles;
	}

	/**
	 * Simple insert that does not guarantee or alert if the insert failed.
	 */
	@Override
	public void insertArticle(final Article article) throws SQLException {
		PreparedStatement ps = DaoUtils.getPreparedStatement(dataSource, insertArticleStmt);
		ps.setString(1, article.getUrl().toString());
		ps.setString(2, article.getData());
		ps.setString(3, article.getDate().toLocalDateTime().toString());
		LOGGER.info("Preparing to execute statement: [{}] using article {}", insertArticleStmt, article);
		ps.executeUpdate();
	}

}
