package io.github.pattyler.grabber.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.github.pattyler.backend.model.Article;

/**
 * Implementation of {@link ArticleParser} for NHK Easy News (https://www3.nhk.or.jp/news/easy/).
 * 
 * @author Patrick
 *
 */
@Component
public class NHKEasyArticleParser implements ArticleParser {
	
	private final String ARTICLE_CONTENT_SELECTOR;
	private final String HTML_TAG_FURIGANA;
	
	public NHKEasyArticleParser(Environment environment) {
		this.ARTICLE_CONTENT_SELECTOR = environment.getProperty("selector.article.content");
		this.HTML_TAG_FURIGANA = environment.getProperty("nhk.tag.furigana");
	}

	@Override
	public Article parse(Article article, String html) {
		Document htmlDoc = Jsoup.parse(html);
		String data = extractData(htmlDoc);	
		article.setData(data);
		return article;
	}
	
	private String extractData(Document htmlDoc) {
		StringBuilder sb = new StringBuilder();

		htmlDoc.select(ARTICLE_CONTENT_SELECTOR)
				.get(0)
				.traverse(new NodeVisitor() {
					
					@Override
					public void tail(Node node, int depth) {
						// do nothing
					}
					
					@Override
					public void head(Node node, int depth) {
						// DFS of this node's and children's nodes' TextNodes. Seems to be only way
						// to get text in correct order. (e.g. <p> Some <b>big</b> text</p> using p.textNodes() would give
						// something like "some text big" [analogy not actually correct, but similar to this]) .
						if (node instanceof TextNode) {
							TextNode tn = (TextNode) node;
							if (tn.parent() instanceof Element) {
								Element e = (Element) tn.parent();
								if (!e.tagName().equals(HTML_TAG_FURIGANA) && !e.ownText().isEmpty())	// ignore furigana
									sb.append(tn.text());
							}
						}
					}
				});
		
		return sb.toString();
	}

}
