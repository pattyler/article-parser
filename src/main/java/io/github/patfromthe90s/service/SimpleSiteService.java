package io.github.patfromthe90s.service;

import java.time.ZonedDateTime;

import io.github.patfromthe90s.exception.GenericHTTPException;
import io.github.patfromthe90s.exception.HeaderNotPresentException;
import io.github.patfromthe90s.exception.SiteServiceException;
import io.github.patfromthe90s.http.Interactor;

/**
 * Simple, single threaded implementation that blocks on IO.
 * 
 * @author Patrick
 *
 */
public class SimpleSiteService implements SiteService {
	
	private final Interactor htmlInteractor;
	private final Interactor jsonInteractor;
	
	public SimpleSiteService(Interactor htmlInteractor, Interactor jsonInteractor) {
		this.htmlInteractor = htmlInteractor;
		this.jsonInteractor = jsonInteractor;
	}

	@Override
	public ZonedDateTime getLastUpdated(String url) throws SiteServiceException {
		try {
			return htmlInteractor.getLastUpdated(url);
		} catch (GenericHTTPException | HeaderNotPresentException e) {
			// TODO log this
			throw new SiteServiceException(e);
		}
	}

	@Override
	public String getHtml(String url) throws SiteServiceException {
		try {
			return htmlInteractor.get(url);
		} catch (GenericHTTPException e) {
			// TODO log this
			throw new SiteServiceException(e);
		}
	}

	@Override
	public String getJson(String url) throws SiteServiceException {
		try {
			return jsonInteractor.get(url);
		} catch (GenericHTTPException e) {
			// TODO log this
			throw new SiteServiceException(e);
		}
	}

}
