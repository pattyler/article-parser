package io.github.patfromthe90s.http;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.http.Header;
import org.apache.http.client.fluent.Request;

import io.github.patfromthe90s.exception.GenericHTTPException;
import io.github.patfromthe90s.exception.HeaderNotPresentException;
import io.github.patfromthe90s.util.Messages;

public class SimpleInteractor implements Interactor {

	@Override
	public LocalDateTime getLastUpdated(URL url) throws GenericHTTPException, HeaderNotPresentException {
		try {
			Header[] lastModifiedHeader = Request.Head(url.toString())
					.execute()
					.returnResponse()
					.getHeaders("last-modified");
			
			if (lastModifiedHeader.length < 1)
				throw new HeaderNotPresentException(Messages.HEADER_NO_LAST_MOD);
					
			final String strLastModified = lastModifiedHeader[0].getValue();
			// LocalDateTime lastModified = LocalDateTime.parse(strLastModified, DateTimeFormatter.RFC_1123_DATE_TIME);
			ZonedDateTime lastModified = ZonedDateTime.parse(strLastModified, DateTimeFormatter.RFC_1123_DATE_TIME);
			Instant instLastModified = lastModified.toInstant();
			ZonedDateTime japanLastModified = ZonedDateTime.ofInstant(instLastModified, ZoneId.of(ZoneId.SHORT_IDS.get("JST")));
			System.out.println(japanLastModified.toLocalDateTime().toString());
			return lastModified.toLocalDateTime();
		} catch (IOException e) {
			throw new GenericHTTPException(e);
		}
	}

	@Override
	public String getHtml(URL url) throws GenericHTTPException {
		// TODO Auto-generated method stub
		return null;
	}

}
