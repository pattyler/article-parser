package io.github.patfromthe90s.model;

import java.net.URL;
import java.time.LocalDateTime;

/**
 * Model representing an article stored in the database. <br/>
 * Construction is done via the builder pattern, starting with {@link create()}, followed
 * by the various {@code setXYZ()} methods.
 * @author Patrick
 *
 */
public final class Article {
	
	private URL url;
	private LocalDateTime date;
	private String data;
	
	// Builder pattern has been used to allow extensibility in the future, at the cost of 
	// each class member variable not being declared final.
	private Article() {
	}
	
	public static Article create() {
		return new Article();
	}
	
	public Article setDate(LocalDateTime date) {
		this.date = date;
		return this;
	}
	
	public Article setData(String data) {
		this.data = data;
		return this;
	}
	
	public Article setUrl(URL url) {
		this.url = url;
		return this;
	}

	public URL getUrl() {
		return url;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getData() {
		return data;
	}

	@Override
	public int hashCode() {
		// Generated method
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		// Generate method
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article other = (Article) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
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
		return "Article [url=" + url + ", date=" + date + ", data=" + data + "]";
	}
	
	
	
}
