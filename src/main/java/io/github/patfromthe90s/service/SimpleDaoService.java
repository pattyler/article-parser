package io.github.patfromthe90s.service;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import io.github.patfromthe90s.dao.ArticleDao;
import io.github.patfromthe90s.dao.SiteDao;
import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.exception.RecordNotInDatabaseException;
import io.github.patfromthe90s.model.Article;

/**
 * Simple implementation of {@link DaoService}.
 * 
 * @author Patrick
 */
public final class SimpleDaoService implements DaoService {

	private final SiteDao siteDao;
	private final ArticleDao articleDao;
	
	public SimpleDaoService(SiteDao siteDao, ArticleDao articleDao) {
		this.siteDao = siteDao;
		this.articleDao = articleDao;
	}

	@Override
	public LocalDateTime getLastUpdated(URL url) throws DaoServiceException {
		try {
			return siteDao.getLastUpdated(url);
		} catch (RecordNotInDatabaseException | SQLException e) {
			// TODO handle this properly
			throw new DaoServiceException(e);
		}
	}

	@Override
	public void updateLastUpdated(URL url, LocalDateTime newLastUpdated) throws DaoServiceException {
		try {
			siteDao.updateLastUpdated(url, newLastUpdated);
		} catch (RecordNotInDatabaseException | SQLException e) {
			// TODO handle this properly
			throw new DaoServiceException(e);
		}
	}

	@Override
	public List<Article> getArticlesBetween(LocalDateTime from, LocalDateTime to) throws DaoServiceException {
		try {
			return articleDao.getArticlesBetween(from, to);
		} catch (SQLException e) {
			// TODO handle this properly
			throw new DaoServiceException(e);
		}
	}

	@Override
	public void insertArticle(Article article) throws DaoServiceException {
		try {
			articleDao.insertArticle(article);
		} catch (SQLException e) {
			// TODO handle this properly
			throw new DaoServiceException(e);
		}
	}

}
