package io.github.patfromthe90s.util;

public class PropertyKey {
	
	public static class DB {
		public static final String URL = "db.url";
	}

	public static class NHK {
		public static final String BASE_URL = "nhk.url.base";
		public static final String JSON_URL = "nhk.url.json";
		public static final String JSON_DATE_PATTERN = "nhk.datepattern.json";
		public static final String HTML_DATE_PATTERN = "nhk.datepattern.html";
		public static final String HTML_SELECTOR_TIME = "nhk.selector.time";
		public static final String HTML_SELECTOR_DATE = "nhk.selector.date";
		public static final String HTML_TAG_FURIGANA = "nhk.tag.furigana";
	}
	
	public static class Selector {
		public static final String ARTICLE_TITLE = "selector.article.title";
		public static final String ARTICLE_CONTENT = "selector.article.content";
	}

	public static class SQL {
		public static final String GET_LAST_UPDATED = "sql.get.LastUpdated";
		public static final String UPDATE_LAST_UPDATED = "sql.update.lastUpdated";
		public static final String GET_ARTICLE = "sql.get.article";
		public static final String INSERT_ARTICLE = "sql.insert.article";
	}
	
	public static class Message {
		public static final String DB_NO_RECORD = "messages.db.noRecord";
		public static final String HEADER_NO_LAST_MOD = "messages.http.noHeader";
	}
}
