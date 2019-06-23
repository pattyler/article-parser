package io.github.patfromthe90s.service;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;

import io.github.patfromthe90s.dao.SiteDao;
import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.exception.RecordNotInDatabaseException;

/**
 * Simple implementation of {@link DaoService}.
 * 
 * @author Patrick
 */
public final class SimpleDaoService implements DaoService {

	private final SiteDao siteDao;
	
	public SimpleDaoService(SiteDao siteDao) {
		this.siteDao = siteDao;
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

}
