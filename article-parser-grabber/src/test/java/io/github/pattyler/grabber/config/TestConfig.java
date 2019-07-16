package io.github.pattyler.grabber.config;

import javax.sql.DataSource;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.PlatformTransactionManager;

import io.github.pattyler.backend.dao.ArticleDao;
import io.github.pattyler.backend.dao.SiteDao;
import io.github.pattyler.backend.service.DaoService;
import io.github.pattyler.grabber.service.SiteService;

@TestConfiguration
public class TestConfig {
	
	@MockBean private SiteService siteService;
	@MockBean private DaoService daoService;
	@MockBean private ArticleDao articleDao;
	@MockBean private SiteDao siteDao;
	@MockBean private DataSource dataSource;
	@MockBean private PlatformTransactionManager txManager;
	
}
