package io.github.patfromthe90s.service;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.patfromthe90s.dao.ArticleDao;
import io.github.patfromthe90s.dao.SiteDao;
import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.exception.RecordNotInDatabaseException;
import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.util.TimeUtils;

/**
 * Simple implementation of {@link DaoService}.
 * 
 * @author Patrick
 */
@Service
public final class SimpleDaoService implements DaoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDaoService.class);
	
	private final SiteDao siteDao;
	private final ArticleDao articleDao;
	
	@Autowired
	public SimpleDaoService(SiteDao siteDao, ArticleDao articleDao) {
		this.siteDao = siteDao;
		this.articleDao = articleDao;
	}

	@Override
	public ZonedDateTime getLastUpdated(final String SITE_ID) throws DaoServiceException {
		try {
			LOGGER.info("Getting last updated date for {}", SITE_ID);
			return siteDao.getLastUpdated(SITE_ID);
		} catch (RecordNotInDatabaseException | SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new DaoServiceException(e);
		}
	}

	@Override
	public void updateLastUpdated(final String SITE_ID) throws DaoServiceException {
		try {
			LOGGER.info("Updating last updated using str_id {} and date {}", SITE_ID);
			siteDao.updateLastUpdated(SITE_ID);
		} catch (RecordNotInDatabaseException | SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new DaoServiceException(e);
		}
	}

	@Override
	public List<Article> getArticlesBetween(ZonedDateTime from, ZonedDateTime to) throws DaoServiceException {
		try {
			LOGGER.debug("Mapping dates from [{}] and [{}]", from, to);
			from = TimeUtils.toUtc(from);
			to = TimeUtils.toUtc(to);
			LOGGER.debug("Mapped dates to [{}] and [{}]", from, to);
			return articleDao.getArticlesBetween(from, to);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new DaoServiceException(e);
		}
	}

	@Override
	public void insertArticle(final Article article) throws DaoServiceException {
		try {
			articleDao.insertArticle(article);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw new DaoServiceException(e);
		}
	}

}
