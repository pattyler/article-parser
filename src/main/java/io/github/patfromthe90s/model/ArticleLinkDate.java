package io.github.patfromthe90s.model;

import java.time.ZonedDateTime;

/**
 * Represents a link to an article and the date the article was uploaded.
 * 
 * @author Patrick
 *
 */
public class ArticleLinkDate {
	
	private final String url;
	private final ZonedDateTime dateTime;
	
	/**
	 * @param url Link to an article.
	 * @param dateTime The UTC date and time the article was uploaded.
	 */
	public ArticleLinkDate(String url, ZonedDateTime dateTime) {
		this.url = url;
		this.dateTime = dateTime;
	}

	public String getUrl() {
		return url;
	}

	public ZonedDateTime getDateTime() {
		return dateTime;
	}
	
	@Override
	public int hashCode() {
		// Generated method
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		// Generated method
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArticleLinkDate other = (ArticleLinkDate) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		// Generated method
		return "ArticleLinkDate [url=" + url + ", dateTime=" + dateTime + "]";
	}
	
	

}
