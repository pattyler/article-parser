package io.github.patfromthe90s.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

import io.github.patfromthe90s.model.Article;
import io.github.patfromthe90s.util.PropertiesUtil;
import io.github.patfromthe90s.util.PropertyKey;
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
		//String title = extractTitle(htmlDoc);
		
		return Article.create()
						.setData(data)
						.setDate(date);
	}
	
	private String extractData(Document htmlDoc) {
		StringBuilder sb = new StringBuilder();

		htmlDoc.select(PropertiesUtil.get(PropertyKey.Selector.ARTICLE_CONTENT))
				.get(0)
				.traverse(new NodeVisitor() {
					
					@Override
					public void tail(Node node, int depth) {
						// do nothing
					}
					
					@Override
					public void head(Node node, int depth) {
						// DFS of this node's and children's nodes' TextNodes. Seems to be only way
						// to get text in correct order. (e.g. <p> Some <b>big</b> text</p> using p.textNodes() would give
						// something like "some text big" [analogy not actually correct, but similar to this]) .
						if (node instanceof TextNode) {
							TextNode tn = (TextNode) node;
							if (tn.parent() instanceof Element) {
								Element e = (Element) tn.parent();
								if (!e.tagName().equals(PropertiesUtil.get(PropertyKey.NHK.HTML_TAG_FURIGANA)) && !e.ownText().isEmpty())	// ignore furigana
									sb.append(tn.text());
							}
						}
					}
				});
		
		return sb.toString();
	}
	
	// This method relies on parsing tags and text elements from the HMTL page.
	// Consider doing this in a different, more stable way.
	private ZonedDateTime extractDateTime(Document htmlDoc) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PropertiesUtil.get(PropertyKey.NHK.HTML_DATE_PATTERN));
		// Date is part of "id" attribute of <body>. Format is newsXXX_XXX, so strip news
		// and parse the section before the underscore.
		String rawDate = htmlDoc.select(PropertiesUtil.get(PropertyKey.NHK.HTML_SELECTOR_DATE))
								.attr("id")
								.substring(4)
								.split("_")[0]; 
		LocalDate localDate = LocalDate.parse(rawDate, formatter);
		
		String rawTime = htmlDoc.select(PropertiesUtil.get(PropertyKey.NHK.HTML_SELECTOR_TIME))
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


}
