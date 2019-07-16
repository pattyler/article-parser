package io.github.pattyler.grabber.parser;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.github.pattyler.backend.model.Article;
import io.github.pattyler.backend.util.TimeUtils;

/**
 * Implementation of {@link JsonParser} for JSON response from NHK Easy News ({@link https://www3.nhk.or.jp/news/easy/???.json})
 * 
 * @author Patrick
 *
 */
@Component
public class NHKEasyArticleListParser implements ArticleListParser {
	
	private final String NHK_BASE_URL;
	private final String JSON_DATE_PATTERN;
	
	public NHKEasyArticleListParser(Environment environment) {
		this.NHK_BASE_URL = environment.getProperty("nhk.url.base");
		this.JSON_DATE_PATTERN = environment.getProperty("nhk.datepattern.json");
	}
	
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
		String url = new StringBuilder(NHK_BASE_URL)
							.append(articleId)
							.append("/")
							.append(articleId)
							.append(".html")
							.toString();
		
		// Convert given JST time to UTC.
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(JSON_DATE_PATTERN);
		LocalDateTime ldt = LocalDateTime.parse(strDate, formatter);
		ZonedDateTime utcTime = ZonedDateTime.of(ldt, TimeUtils.ZONE_JST) // Datetimes returned in JSON always JST.
										.withZoneSameInstant(TimeUtils.ZONE_UTC);
		return new Article().setDate(utcTime)
							.setUrl(url)
							.setTitle(title);
	}

}
