package io.github.patfromthe90s.parser;

import io.github.patfromthe90s.model.Article;

/**
 * Interface for parsing HTML articles into a model object.
 * 
 * @author Patrick
 *
 */
public interface ArticleParser {
	
	public Article parse(String html);

}
