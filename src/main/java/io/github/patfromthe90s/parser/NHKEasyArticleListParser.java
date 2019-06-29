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

import io.github.patfromthe90s.model.ArticleLinkDate;
import io.github.patfromthe90s.util.TempProperties;
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
	public List<ArticleLinkDate> parse(String json) {
		List<ArticleLinkDate> articleLinkDates = new ArrayList<>();
		
		JsonArray ja = new JsonParser().parse(json).getAsJsonArray();
		StreamSupport.stream(ja.spliterator(), false)
				.map(JsonElement::getAsJsonObject)
				.forEach(o -> articleLinkDates.add(
								createFrom(o.get("news_id").getAsString(), 
										o.get("news_prearranged_time").getAsString())
						));
					
		return articleLinkDates;
	}
	
	private ArticleLinkDate createFrom(String articleId, String strDate) {
		// create the full URL using the given article Id.
		String url = new StringBuilder(TempProperties.NHK_BASE_URL)
							.append(articleId)
							.append("/")
							.append(articleId)
							.append(".html")
							.toString();
		
		// Convert given JST time to UTC.
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TempProperties.NHK_JSON_DATE_PATTERN);
		LocalDateTime ldt = LocalDateTime.parse(strDate, formatter);
		ZonedDateTime utcTime = ZonedDateTime.of(ldt, TimeUtils.JST_ZONE_ID)
										.withZoneSameInstant(TimeUtils.UTC_ZONE_ID);
		return new ArticleLinkDate(url, utcTime);
	}

}
