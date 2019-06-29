package io.github.patfromthe90s.parser;

import java.util.List;

import io.github.patfromthe90s.model.ArticleLinkDate;

/**
 * Interface for parsing data into a <code>List</code> containing links to articles and the dates they were added. <br/>
 * The data may be in various forms, suchs as HTML or JSON.
 * 
 * @author Patrick
 *
 */
public interface ArticleListParser { // IJsonParser, and not JsonParser, to avoid name conflict with GSON library
	
	/**
	 * Parse the data and extract any links that point to articles, along with the 
	 * date the article was added.
	 * 
	 * @param data The data (e.g. HTML, JSON) to be parsed.
	 * @return A list of all discovered article links / dates added.
	 */
	public List<ArticleLinkDate> parse(String data);

}
