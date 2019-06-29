package io.github.patfromthe90s.util;

/**
 * Temporary class for holding static values. Will be exported into a configurable properties file in the future.
 * TODO refactor this to Properties
 * 
 * @author Patrick
 *
 */
public class TempProperties {

	public static final String DB_URL = "jdbc:sqlite:C:/Program Files/sqlite3/db/test.db";
	
	public static final String NHK_BASE_URL = "https://www3.nhk.or.jp/news/easy/";
	public static final String NHK_JSON_TOP_LOC = "top-list.json";
	public static final String NHK_JSON_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String NHK_HTML_SELECTOR_TIME = "#js-article-date";
	public static final String NHK_HTML_SELECTOR_DATE = "body";
	public static final String NHK_HTML_DATE_PATTERN = "yyyyMMdd";
	public static final String NHK_HTML_TAG_FURIGANA = "rt";
	
	//CSS selectors for extracting content from HTML
	public static final String SELECTOR_ARTICLE_TITLE = "h1.article-main__title";
	public static final String SELECTOR_ARTICLE_CONTENT = "div.article-main__body";
	
}
