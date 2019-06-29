package io.github.patfromthe90s.http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.client.fluent.Request;

import io.github.patfromthe90s.exception.GenericHTTPException;
import io.github.patfromthe90s.exception.HeaderNotPresentException;
import io.github.patfromthe90s.util.Messages;

/**
 * Generic implementation of {@link Interactor}
 * @author Patrick
 *
 */
public abstract class GenericInteractor implements Interactor {
	
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8"); 
	
	@Override
	public ZonedDateTime getLastUpdated(String url) throws GenericHTTPException, HeaderNotPresentException {
		try {
			final Header[] lastModifiedHeader = Request.Head(url)
													.execute()
													.returnResponse()
													.getHeaders("last-modified");

			// expect only one value, but library returns array so just return the first value
			return Arrays.stream(lastModifiedHeader)
					.map(Header::getValue)
					.map( (String s) -> { return ZonedDateTime.parse(s, DateTimeFormatter.RFC_1123_DATE_TIME); })
					.findFirst()
					.orElseThrow(() -> new HeaderNotPresentException(Messages.HEADER_NO_LAST_MOD));	

		} catch (IOException e) {
			throw new GenericHTTPException(e);
		}
	}
	
	@Override
	public String get(String url) throws GenericHTTPException {
		try {
			return Request.Get(url)
						.execute()
						.returnContent()
						.asString(UTF8_CHARSET);
		} catch (IOException e) {
			throw new GenericHTTPException(e);
		}
	}

}
