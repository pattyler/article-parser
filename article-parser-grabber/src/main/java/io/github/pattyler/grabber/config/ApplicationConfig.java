package io.github.pattyler.grabber.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.github.pattyler.grabber.http.Interactor;
import io.github.pattyler.grabber.http.SimpleHTMLInteractor;
import io.github.pattyler.grabber.http.SimpleJsonInteractor;

/**
 * Main configuration class for the application.
 * 
 * @author Patrick
 *
 */
@Configuration
@EnableTransactionManagement
public class ApplicationConfig {
	
	@Bean
	public Interactor htmlInteractor() {
		return new SimpleHTMLInteractor();
	}
	
	@Bean
	public Interactor jsonInteractor() {
		return new SimpleJsonInteractor();
	}

	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}
