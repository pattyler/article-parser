package io.github.patfromthe90s.dao;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;

public interface SiteDao {
	

	public LocalDateTime getLastUpdated(URL url) throws SQLException;

}