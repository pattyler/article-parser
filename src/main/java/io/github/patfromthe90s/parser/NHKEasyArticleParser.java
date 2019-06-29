package io.github.patfromthe90s.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.util.TempProperties;
import io.github.patfromthe90s.util.TimeUtils;

/**
 * Implementation of {@link ArticleParser} for NHK Easy News (https://www3.nhk.or.jp/news/easy/).
 * 
 * @author Patrick
 *
 */
public class NHKEasyArticleParser implements ArticleParser {

	@Override
	public Article parse(String html) {
		Document htmlDoc = Jsoup.parse(html);
		String data = extractData(htmlDoc);
		ZonedDateTime date = extractDateTime(htmlDoc);
		String title = extractTitle(htmlDoc);
		
		return Article.create()
						.setData(data)
						.setDate(date);
	}
	
	private String extractData(Document htmlDoc) {
		List<String> data = new ArrayList<>();
		
		Elements els = htmlDoc.select(TempProperties.SELECTOR_ARTICLE_CONTENT)
								.get(0)
								.getAllElements();
		
		StringBuilder sb = new StringBuilder();
		/**
		StreamSupport.stream(els.spliterator(), false)
					.filter(e -> !e.ownText().isEmpty())
					.filter(e -> !e.tagName().equals("rt"))
					.map(Element::ownText)
					.skip(1)
					.forEach(sb::append);
					*/
		
		System.out.println(sb.toString());
		return null;
	}
	
	// This method relies on parsing tags and text elements from the HMTL page.
	// Consider doing this in a different, more stable way.
	private ZonedDateTime extractDateTime(Document htmlDoc) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		// Date is part of "id" attribute of <body>. Format is newsXXX_XXX, so strip news
		// and parse the section before the underscore.
		String rawDate = htmlDoc.select("body")
								.attr("id")
								.substring(4)
								.split("_")[0]; 
		LocalDate localDate = LocalDate.parse(rawDate, formatter);
		
		String rawTime = htmlDoc.select("#js-article-date")
								.eachText()
								.stream()
								.findFirst()
								.get()
								.split(" ")[1];
		int hour = Integer.valueOf(rawTime.substring(0, 2));
		int minute = Integer.valueOf(rawTime.substring(3, 5));
		LocalTime localTime = LocalTime.of(hour, minute);
		
		return ZonedDateTime.of(LocalDateTime.of(localDate, localTime), 
								TimeUtils.JST_ZONE_ID)
							.withZoneSameInstant(TimeUtils.UTC_ZONE_ID);
	}
	
	private String extractTitle(Document htmlDoc) {
		return null;
	}

}
