package io.github.patfromthe90s.http;

import java.net.URL;

import io.github.patfromthe90s.exception.GenericHTTPException;

/**
 * Simple and basic implementation of {@link Interactor} for JSON interaction.
 * 
 * @author Patrick
 *
 */
public final class SimpleJsonInteractor extends GenericInteractor {
	
	@Override
	public String get(URL url) throws GenericHTTPException {
		return super.get(url).substring(1);
	}

}
