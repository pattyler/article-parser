package io.github.patfromthe90s.backend.parser;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.github.patfromthe90s.backend.model.Article;
import io.github.patfromthe90s.backend.util.TimeUtils;

/**
 * Implementation of {@link JsonParser} for JSON response from NHK Easy News ({@link https://www3.nhk.or.jp/news/easy/???.json})
 * 
 * @author Patrick
 *
 */
@Component
public class NHKEasyArticleListParser implements ArticleListParser {
	
	@Value("${nhk.url.base}")
	private String nhkBaseUrl;
	
	@Value("${nhk.datepattern.json}")
	private String jsonDatePattern;
	
	/**
	 * Expects a well-formed JSON array formatted as a <code>String</code>
	 */
	@Override
	public List<Article> parse(String json) {
		List<Article> articles = new ArrayList<>();
		
		JsonArray ja = new JsonParser().parse(json).getAsJsonArray();
		StreamSupport.stream(ja.spliterator(), false)
					.map(JsonElement::getAsJsonObject)
					.forEach(o -> articles.add(
									createFrom(o.get("news_id").getAsString(), 
											   o.get("news_prearranged_time").getAsString(),
											   o.get("title").getAsString())
							));
						
		return articles;
	}
	
	private Article createFrom(String articleId, String strDate, String title) {
		// create the full URL using the given article Id.
		String url = new StringBuilder(nhkBaseUrl)
							.append(articleId)
							.append("/")
							.append(articleId)
							.append(".html")
							.toString();
		
		// Convert given JST time to UTC.
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(jsonDatePattern);
		LocalDateTime ldt = LocalDateTime.parse(strDate, formatter);
		ZonedDateTime utcTime = ZonedDateTime.of(ldt, TimeUtils.ZONE_JST) // Datetimes returned in JSON always JST.
										.withZoneSameInstant(TimeUtils.ZONE_UTC);
		return new Article().setDate(utcTime)
							.setUrl(url)
							.setTitle(title);
	}

}
