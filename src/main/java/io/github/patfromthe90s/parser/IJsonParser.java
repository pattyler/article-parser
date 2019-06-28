package io.github.patfromthe90s.parser;

import java.util.List;

import io.github.patfromthe90s.model.ArticleLinkDate;

/**
 * Interface for parsing a JSON resposne.
 * 
 * @author Patrick
 *
 */
public interface IJsonParser { // IJsonParser, and not JsonParser, to avoid name conflict with GSON library
	
	/**
	 * Parse the JSON and extract any links that point to articles, along with the 
	 * date the article was added.
	 * 
	 * @param html The JSON to parse
	 * @return A list of all discovered article links.
	 */
	public List<ArticleLinkDate> getArticleLinksAndDates(String json);

}
