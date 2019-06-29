package io.github.patfromthe90s.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.patfromthe90s.model.Article;

public class NHKEasyArticleParserTest {
	
	private static final String EXAMPLE_ARTICLE_HTML;
	private static final ZonedDateTime EXAMPLE_ARTICLE_DATE = ZonedDateTime.of(
																LocalDateTime.of(2019, 6, 27, 7, 25), 
																ZoneId.of("UTC"));
	
	// Load test HTML from file. Not modified, so only needs to be done once.
	static {
		StringBuilder sb = new StringBuilder();
		InputStream is = NHKEasyArticleParserTest.class.getClassLoader().getResourceAsStream("example-article.html");
		try (Scanner scanner = new Scanner(is)) {
			while (scanner.hasNextLine())
				sb.append(scanner.nextLine());
		}
		
		EXAMPLE_ARTICLE_HTML = sb.toString();
	}
	
	private ArticleParser parser;
	
	@BeforeEach
	public void setup() {
		parser = new NHKEasyArticleParser();
	}
	
	@Test
	@DisplayName("Whne given a HTML page, then an Article object is correctly returned")
	public void whenGivenHtml_thenArticleReturnedCorrect() {
		Article parsedArticle = parser.parse(EXAMPLE_ARTICLE_HTML);
		assertEquals(parsedArticle.getDate(), EXAMPLE_ARTICLE_DATE);
	}

}
