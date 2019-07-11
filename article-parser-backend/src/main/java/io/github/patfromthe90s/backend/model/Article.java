package io.github.patfromthe90s.backend.model;

import java.time.ZonedDateTime;

/**
 * Model representing an article stored in the database. <br/>
 * Construction is done via the builder pattern, starting with {@link create()}, followed
 * by the various {@code setXYZ()} methods.
 * @author Patrick
 *
 */
public final class Article {
	
	private Long id;	// primary key
	private String siteId;	// foreign key
	private String url;
	private String title;
	private ZonedDateTime date;
	private String data;
		
	/*
	 * <b>Warning:</b> Only to be used when returning a value from the database. <br/>
	 * This is an auto-generated Primary Key.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	public Article setDate(ZonedDateTime date) {
		this.date = date;
		return this;
	}
	
	public Article setData(String data) {
		this.data = data;
		return this;
	}
	
	public Article setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public ZonedDateTime getDate() {
		return date;
	}

	public String getData() {
		return data;
	}
	
	/**
	 * Primary key
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Foreign key
	 * 
	 * @return
	 */
	public String getSiteId() {
		return siteId;
	}

	/**
	 * Foreign key
	 * 
	 * @return
	 */
	public Article setSiteId(String siteId) {
		this.siteId = siteId;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Article setTitle(String title) {
		this.title = title;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (siteId == null) {
			if (other.siteId != null)
				return false;
		} else if (!siteId.equals(other.siteId))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
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
		return "Article [id=" + id + ", siteId=" + siteId + ", url=" + url + ", title=" + title + ", date=" + date
				+ ", data=" + data + "]";
	}

	
	
	
	
}
