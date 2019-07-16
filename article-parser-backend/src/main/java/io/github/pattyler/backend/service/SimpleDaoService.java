package io.github.pattyler.backend.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import io.github.pattyler.backend.dao.ArticleDao;
import io.github.pattyler.backend.dao.SiteDao;
import io.github.pattyler.backend.exception.DaoServiceException;
import io.github.pattyler.backend.model.Article;
import io.github.pattyler.backend.util.TimeUtils;

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
	private final String DB_NO_RECORD_MSG;
	
	@Autowired
	public SimpleDaoService(SiteDao siteDao, ArticleDao articleDao, Environment environment) {
		this.siteDao = siteDao;
		this.articleDao = articleDao;
		this.DB_NO_RECORD_MSG = environment.getProperty("messages.db.noRecord");
	}

	@Override
	public ZonedDateTime getLastUpdated(String siteId) throws DaoServiceException {
		LOGGER.info("Getting last updated date for {}", siteId);
		return siteDao.getLastUpdated(siteId)
					.orElseThrow(() -> new DaoServiceException(DB_NO_RECORD_MSG));
	}

	@Override
	public void updateLastUpdated(String siteId) throws DaoServiceException {
		LOGGER.info("Updating last updated using str_id {} and date {}", siteId);
		final int NUM_UPDATED = siteDao.updateLastUpdated(siteId);
		if (NUM_UPDATED < 1)	// expect a single row to be updated
				throw new DaoServiceException(DB_NO_RECORD_MSG);
	}

	@Override
	public List<Article> getArticlesBetween(ZonedDateTime from, ZonedDateTime to) throws DaoServiceException {
		LOGGER.debug("Mapping dates from [{}] and [{}]", from, to);
		from = TimeUtils.toUtc(from);
		to = TimeUtils.toUtc(to);
		LOGGER.debug("Mapped dates to [{}] and [{}]", from, to);
		return articleDao.getArticlesBetween(from, to);
	}

	@Override
	public void insertArticle(final Article article) throws DaoServiceException {
		articleDao.insertArticle(article);
	}

}
