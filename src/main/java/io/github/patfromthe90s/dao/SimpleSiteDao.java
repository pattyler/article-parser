package io.github.patfromthe90s.dao;

import java.net.URL;
import java.time.LocalDate;

import javax.sql.DataSource;

public class SimpleSiteDao implements SiteDao {
	
	private final DataSource dataSource;
	
	public SimpleSiteDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public LocalDate getLastUpdated(URL url) {
		return null;
	}
}