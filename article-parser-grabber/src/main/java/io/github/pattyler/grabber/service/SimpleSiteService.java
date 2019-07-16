package io.github.pattyler.grabber.service;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.pattyler.grabber.exception.GenericHTTPException;
import io.github.pattyler.grabber.exception.HeaderNotPresentException;
import io.github.pattyler.grabber.exception.SiteServiceException;
import io.github.pattyler.grabber.http.Interactor;

/**
 * Simple, single threaded implementation that blocks on IO.
 * 
 * @author Patrick
 *
 */
@Service
public class SimpleSiteService implements SiteService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSiteService.class);
	
	private final Interactor htmlInteractor;
	private final Interactor jsonInteractor;
	
	public SimpleSiteService(Interactor htmlInteractor, Interactor jsonInteractor) {
		this.htmlInteractor = htmlInteractor;
		this.jsonInteractor = jsonInteractor;
	}

	@Override
	public ZonedDateTime getLastUpdated(String url) throws SiteServiceException {
		try {
			LOGGER.info("getting last updated for url {}", url);
			return htmlInteractor.getLastUpdated(url);
		} catch (GenericHTTPException | HeaderNotPresentException e) {
			LOGGER.error(e.getMessage(), e);
			throw new SiteServiceException(e);
		}
	}

	@Override
	public String getHtml(String url) throws SiteServiceException {
		try {
			LOGGER.info("getting HTML for url {}", url);
			return htmlInteractor.get(url);
		} catch (GenericHTTPException e) {
			LOGGER.error(e.getMessage(), e);
			throw new SiteServiceException(e);
		}
	}

	@Override
	public String getJson(String url) throws SiteServiceException {
		try {
			LOGGER.info("getting JSON for url {}", url);
			return jsonInteractor.get(url);
		} catch (GenericHTTPException e) {
			LOGGER.error(e.getMessage(), e);
			throw new SiteServiceException(e);
		}
	}

}
