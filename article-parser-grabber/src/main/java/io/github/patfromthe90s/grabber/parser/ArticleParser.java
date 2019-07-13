package io.github.patfromthe90s.grabber.parser;

import io.github.patfromthe90s.backend.model.Article;

/**
 * Interface for parsing HTML and populating {@link Article} objects.
 * 
 * @author Patrick
 *
 */
public interface ArticleParser {
	
	/**
	 * Given a HTML page represented as a <code>String</code>, parse it and add any required data
	 * to the given {@link Article}.
	 * 
	 * {@link Article}.
	 * @param article A semi-populated article. 
	 * @param html The HTML to parse.
	 * @return <code>article</code>, with more data added.
	 */
	public Article parse(Article article, String html);

}
