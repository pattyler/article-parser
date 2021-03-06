package io.github.pattyler.grabber.http;

import io.github.pattyler.grabber.exception.GenericHTTPException;

/**
 * Simple and basic implementation of {@link Interactor} for JSON interaction.
 * 
 * @author Patrick
 *
 */
public final class SimpleJsonInteractor extends GenericInteractor {
	
	@Override
	public String get(String url) throws GenericHTTPException {
		return super.get(url).substring(1);
	}

}
