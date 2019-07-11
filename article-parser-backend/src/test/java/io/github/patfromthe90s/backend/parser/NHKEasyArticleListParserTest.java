package io.github.patfromthe90s.backend.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.github.patfromthe90s.backend.model.Article;

@SpringBootTest
@ActiveProfiles("test")
public class NHKEasyArticleListParserTest {
	
	private static final String NEWS_ID_1_URL = "https://www3.nhk.or.jp/news/easy/k10011970611000/k10011970611000.html";
	private static final String NEWS_ID_2_URL = "https://www3.nhk.or.jp/news/easy/k10011970391000/k10011970391000.html";
	private static final ZonedDateTime NEWS_ID_1_DATE = ZonedDateTime.of(
																			LocalDateTime.parse("2019-06-28T11:30:00"), 
																			ZoneId.of("JST", ZoneId.SHORT_IDS))
																	.withZoneSameInstant(ZoneId.of("UTC"));
	private static final ZonedDateTime NEWS_ID_2_DATE = ZonedDateTime.of(
																			LocalDateTime.parse("2019-06-28T11:25:00"), 
																			ZoneId.of("JST", ZoneId.SHORT_IDS))
																	.withZoneSameInstant(ZoneId.of("UTC"));
	
	private static final String NEWS_ID_1_TITLE = "アメリカ国境の川で亡くなった父と娘の写真がニュースになる";
	private static final String NEWS_ID_2_TITLE = "東京オリンピックではペットボトルを持って会場に入れる";
	
	private static final String VALID_JSON = "[{\r\n" + 
			"	\"top_priority_number\": \"1\",\r\n" + 
			"	\"news_id\": \"k10011970611000\",\r\n" + 
			"	\"news_prearranged_time\": \"2019-06-28 11:30:00\",\r\n" + 
			"	\"title\": \"\\u30a2\\u30e1\\u30ea\\u30ab\\u56fd\\u5883\\u306e\\u5ddd\\u3067\\u4ea1\\u304f\\u306a\\u3063\\u305f\\u7236\\u3068\\u5a18\\u306e\\u5199\\u771f\\u304c\\u30cb\\u30e5\\u30fc\\u30b9\\u306b\\u306a\\u308b\"\r\n" + 
			"}, {\r\n" + 
			"	\"top_priority_number\": \"2\",\r\n" + 
			"	\"news_id\": \"k10011970391000\",\r\n" + 
			"	\"news_prearranged_time\": \"2019-06-28 11:25:00\",\r\n" + 
			"	\"title\": \"\\u6771\\u4eac\\u30aa\\u30ea\\u30f3\\u30d4\\u30c3\\u30af\\u3067\\u306f\\u30da\\u30c3\\u30c8\\u30dc\\u30c8\\u30eb\\u3092\\u6301\\u3063\\u3066\\u4f1a\\u5834\\u306b\\u5165\\u308c\\u308b\"\r\n" + 
			"}]";
	
	@Autowired
	private ArticleListParser articleListParser;
	
	@Test
	@DisplayName("When JSON array is empty, then returned List is empty")
	public void whenJsonEmpty_thenListEmpty() {
		List<Article> list = articleListParser.parse("[]");
		assertEquals(0, list.size());
	}
	
	@Test
	@DisplayName("When JSON array is not empty, then each Article is correctly initialised")
	public void whenJsonNonEmpty_thenCorrectList() {
		List<Article> list = articleListParser.parse(VALID_JSON);
		assertEquals(2, list.size());
		assertEquals(NEWS_ID_1_URL, list.get(0).getUrl());
		assertEquals(NEWS_ID_1_DATE, list.get(0).getDate());
		assertEquals(NEWS_ID_1_TITLE, list.get(0).getTitle());
		assertEquals(NEWS_ID_2_URL, list.get(1).getUrl());
		assertEquals(NEWS_ID_2_DATE, list.get(1).getDate());
		assertEquals(NEWS_ID_2_TITLE, list.get(1).getTitle());
	}
}
