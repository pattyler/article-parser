package io.github.pattyler.backend.dao;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.sqlite.SQLiteErrorCode;

import io.github.pattyler.backend.dao.ArticleDao;
import io.github.pattyler.backend.dao.SimpleArticleDao;
import io.github.pattyler.backend.model.Article;

@JdbcTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ActiveProfiles("test")
@Sql("classpath:schema.sql")
@Sql("classpath:data.sql")
public class SimpleArticleDaoTest {
		
	private final String TEST_URL = "https://www3.nhk.or.jp/news/easy/k10011986521000/k10011986521000.html";
	private final String TEST_TITLE = "富士山　山梨県から頂上まで登ることができるようになる";
	private final String TEST_DATA = "富士山は山梨県と静岡県の間にあります。山梨県から登る道は１日にオープンしましたが、崩れた石で３４５０ｍより上に登ることができませんでした。９日の午後、簡単な工事が終わって、頂上まで登ることができるようになりました。工事がまだ全部終わっていないため、道はいつもより狭くなっています。山梨県は「道には案内する人がいるので、その人の言うとおりにして、安全に登ってください」と言っています。１０日に静岡県から登る道もオープンします。近くの旅館の人は「これから山に登るのにいい季節なので、たくさんの人に来てほしいです」と話しています";
	private final String TEST_SITE_ID = "NHKEASY";
	private final ZonedDateTime TEST_DATE = ZonedDateTime.of(LocalDateTime.of(2019, 7, 9, 7, 55), ZoneId.of("UTC"));
	private Article testArticle;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private Environment environment;
	
	private ArticleDao articleDao;
	
	@BeforeEach
	public void setup() throws SQLException {
		//MockitoAnnotations.initMocks(this);
		articleDao = new SimpleArticleDao(dataSource, environment);
		
		testArticle = new Article().setUrl(TEST_URL)
								.setSiteId(TEST_SITE_ID)
								.setTitle(TEST_TITLE)
								.setData(TEST_DATA)
								.setDate(TEST_DATE);
	}
	

	@DisplayName("When results from database, then populated list returned.")
	@Test
	public void whenResults_thenListReturned() throws MalformedURLException, SQLException {
		final ZonedDateTime FROM = ZonedDateTime.of(LocalDateTime.of(2019, 7, 6, 12, 0), ZoneId.of("UTC"));
		final ZonedDateTime TO = ZonedDateTime.of(LocalDateTime.of(2019, 7, 10, 12, 0), ZoneId.of("UTC"));
		
		List<Article> articles = articleDao.getArticlesBetween(FROM, TO);
		assertNotEquals(0, articles.size());
	}
	
	@DisplayName("When no results from database, then empty list returned.")
	@Test
	public void whenNoResults_thenEmptyListReturned() throws MalformedURLException, SQLException {
		final ZonedDateTime FROM = ZonedDateTime.of(LocalDateTime.of(1200, 1, 1, 12, 0), ZoneId.of("UTC"));
		final ZonedDateTime TO = ZonedDateTime.of(LocalDateTime.of(1200, 1, 2, 12, 0), ZoneId.of("UTC"));
		
		List<Article> articles = articleDao.getArticlesBetween(FROM, TO);
		assertEquals(0, articles.size());
	}


	@DisplayName("When article has no ID and is valid, then is inserted")
	@Test
	public void whenValidAndNoId_thenInserted() {
		int preInsertCount = countRowsInTable("article");
		articleDao.insertArticle(testArticle);
		int postInsertCount = countRowsInTable("article");
		assertEquals(preInsertCount + 1, postInsertCount);
	}
	
	@DisplayName("When siteId is not a valid foreign key, then foreign key constraint applies and fails")
	@Test
	public void whenSiteIdNotValid_thenFKConstraintFail() {
		testArticle.setSiteId("NOT_EXIST");
		try {
			articleDao.insertArticle(testArticle);
		} catch (UncategorizedSQLException e) {
			assertEquals(SQLiteErrorCode.SQLITE_CONSTRAINT.code, e.getSQLException().getErrorCode());
		}
	}
	
	private int countRowsInTable(String table) {
		return JdbcTestUtils.countRowsInTable(this.jdbcTemplate, table);
	}
	

}
