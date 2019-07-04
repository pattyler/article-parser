package io.github.patfromthe90s.parser;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.util.PropertiesUtil;
import io.github.patfromthe90s.util.PropertyKey;
import io.github.patfromthe90s.util.TimeUtils;

/**
 * Implementation of {@link JsonParser} for JSON response from NHK Easy News ({@link https://www3.nhk.or.jp/news/easy/???.json})
 * 
 * @author Patrick
 *
 */
public class NHKEasyArticleListParser implements ArticleListParser {
	
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
											   o.get("news_prearranged_time").getAsString())
							));
						
		return articles;
	}
	
	private Article createFrom(String articleId, String strDate) {
		// create the full URL using the given article Id.
		String url = new StringBuilder(PropertiesUtil.get(PropertyKey.NHK.BASE_URL))
							.append(articleId)
							.append("/")
							.append(articleId)
							.append(".html")
							.toString();
		
		// Convert given JST time to UTC.
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PropertiesUtil.get(PropertyKey.NHK.JSON_DATE_PATTERN));
		LocalDateTime ldt = LocalDateTime.parse(strDate, formatter);
		ZonedDateTime utcTime = ZonedDateTime.of(ldt, TimeUtils.JST_ZONE_ID) // Datetimes returned in JSON always JST.
										.withZoneSameInstant(TimeUtils.UTC_ZONE_ID);
		return Article.create()
						.setDate(utcTime)
						.setUrl(url);
	}

}
