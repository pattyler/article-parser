package io.github.patfromthe90s.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sqlite.SQLiteDataSource;

import io.github.patfromthe90s.http.Interactor;
import io.github.patfromthe90s.http.SimpleHTMLInteractor;
import io.github.patfromthe90s.http.SimpleJsonInteractor;

/**
 * Main configuration class for the application.
 * 
 * @author Patrick
 *
 */
@Configuration
public class ApplicationConfig {
	
	@Value("${db.url}")
	private String jdbcUrl;
	
	@Bean
	public DataSource dataSource() {
		SQLiteDataSource ds = new SQLiteDataSource();
		ds.setUrl(jdbcUrl);
		return ds;
	}
	
	@Bean
	public Interactor htmlInteractor() {
		return new SimpleHTMLInteractor();
	}
	
	@Bean
	public Interactor jsonInteractor() {
		return new SimpleJsonInteractor();
	}

}
