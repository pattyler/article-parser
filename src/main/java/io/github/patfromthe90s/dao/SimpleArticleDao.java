package io.github.patfromthe90s.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.util.TimeUtils;

/**
 * Simple implementation of {@link ArticleDao}. Delegates mostly to {@link JdbcTemplate}, including exception handling.
 * @author Patrick
 *
 */
@Repository
public class SimpleArticleDao implements ArticleDao {
	
	private final JdbcTemplate jdbcTemplate;
	private final String GET_ARTICLES_STMT;
	private final String INSERT_ARTICLES_STMT;
	
	@Autowired
	public SimpleArticleDao(DataSource dataSource, Environment environment) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.GET_ARTICLES_STMT = environment.getProperty("sql.get.article");
		this.INSERT_ARTICLES_STMT = environment.getProperty("sql.insert.article");
	}

	@Override
	public List<Article> getArticlesBetween(final ZonedDateTime from, final ZonedDateTime to) {
		final String STR_FROM = from.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		final String STR_TO = to.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		
		List<Article> articles =  jdbcTemplate.query(GET_ARTICLES_STMT,
									new Object[] { STR_FROM, STR_TO },
									new RowMapper<Article>() {
										@Override
										public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
											Article article = new Article().setSiteId(rs.getString(1))
													.setUrl(rs.getString(2))
													.setTitle(rs.getString(3))
													.setData(rs.getString(4))
													.setDate(ZonedDateTime.of(
																LocalDateTime.parse(rs.getString(5)),
																TimeUtils.ZONE_UTC));
											return article;
										}
								
							});
		return articles;
	}

	/**
	 * Simple insert that does not guarantee or alert if the insert failed.
	 */
	@Override
	public void insertArticle(final Article article) {
		jdbcTemplate.update(INSERT_ARTICLES_STMT, article.getSiteId(),
												article.getUrl(),
												article.getTitle(),
												article.getData(),
												article.getDate().toLocalDateTime().toString()); // LocalDateTime for formatting purposes, don't need
																								 // time-zone information.
	}

}
