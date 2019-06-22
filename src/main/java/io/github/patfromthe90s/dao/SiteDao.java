package io.github.patfromthe90s.dao;

import java.net.URL;
import java.time.LocalDate;

public interface SiteDao {
	

	public LocalDate getLastUpdated(URL url);

}