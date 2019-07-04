package io.github.patfromthe90s.parser;

import java.util.List;

import io.github.patfromthe90s.model.Article;

/**
 * Interface for parsing data into a <code>List</code> of semi-populated articles<br/>
 * The data may be in various forms, suchs as HTML or JSON. <br />
 * how detailed each {@link Article} is populated is implementation-dependent, and it's expected 
 * that an {@link ArticleParser} will be used afterwards.
 * 
 * @author Patrick
 *
 */
public interface ArticleListParser { 
	
	/**
	 * Parse the data and extract some basic information for each article (e.g. link, date added, etc.) <br/>
	 * This method <i>must</i> return the link to each article at a minimum.
	 * 
	 * @param data The data (e.g. HTML, JSON) to be parsed.
	 * @return A list of all discovered articles.
	 */
	public List<Article> parse(String data);

}
