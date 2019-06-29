package io.github.patfromthe90s.parser;

import io.github.patfromthe90s.model.Article;

/**
 * Interface for parsing HTML articles into a model object.
 * 
 * @author Patrick
 *
 */
public interface ArticleParser {
	
	/**
	 * Given a HTML page represented as a <code>String</code>, parse it and return the corresponding
	 * {@link Article}.
	 * @param html The HTML to parse.
	 * @return The {@link Article} denoted by the given HTML.
	 */
	public Article parse(String html);

}
