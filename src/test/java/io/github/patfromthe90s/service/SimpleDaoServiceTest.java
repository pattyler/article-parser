package io.github.patfromthe90s.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.github.patfromthe90s.dao.SiteDao;
import io.github.patfromthe90s.exception.DaoServiceException;
import io.github.patfromthe90s.exception.RecordNotInDatabaseException;

/**
 * Test class for {@link SimpleDaoService}. <br/>
 * <b>Note</b>: Methods have many {@code throws}-clauses due to setting up mocks.
 * @author Patrick
 *
 */
@DisplayName("Test SimpleDAOServce")
public class SimpleDaoServiceTest {
	
	private SiteDao mSiteDao;
	private DaoService simpleDaoService;
	private URL url;
	
	private static final String ASSERT_THROWS_FAIL_MSG = "All exceptions should be mapped to DaoServiceException";
	private final String STR_DATE = "2004-05-24T14:14:00";
	private final LocalDateTime VALID_DATE = LocalDateTime.parse(STR_DATE);
	private final String VALID_URL = "http://www.google.com";
	
	@BeforeEach
	public void setup() throws MalformedURLException {
		// being mock setup
		mSiteDao = Mockito.mock(SiteDao.class);
		// end mock setup
		
		simpleDaoService = new SimpleDaoService(mSiteDao);
		url = new URL(VALID_URL);
	}
	
	@Nested
	@DisplayName("A value is returned")
	class ValuesReturned {
		
		@Test
		@DisplayName("From getLastUpdated()")
		public void testLastUpdatedReturned() throws RecordNotInDatabaseException, SQLException, DaoServiceException {
			// being mock setup
			Mockito.when(mSiteDao.getLastUpdated(Mockito.any(URL.class))).thenReturn(VALID_DATE);
			// end mock setup
			
			LocalDateTime returnedDate = simpleDaoService.getLastUpdated(url);
			assertEquals(VALID_DATE, returnedDate);
		}
	}
	
	@Nested
	@DisplayName("Correct exceptions are thrown")
	class ExceptionsThrown {
		
		@Test
		@DisplayName("From getLastUpdated()")
		public void testExceptionGetLastUpdatedException() throws RecordNotInDatabaseException, SQLException {
			// being mock setup
			Mockito.when(mSiteDao.getLastUpdated(Mockito.any(URL.class))).thenThrow(RecordNotInDatabaseException.class);
			// end mock setup
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.getLastUpdated(url),
					ASSERT_THROWS_FAIL_MSG);
			
			// being mock setup
			Mockito.when(mSiteDao.getLastUpdated(Mockito.any(URL.class))).thenThrow(SQLException.class);
			// end mock setup
			
			assertThrows(DaoServiceException.class, 
					() ->  simpleDaoService.getLastUpdated(url),
					ASSERT_THROWS_FAIL_MSG);
		}
		
		@Test
		@DisplayName("From updateLastUpdated()")
		public void testUpdateLastUpdatedException() throws MalformedURLException, SQLException, RecordNotInDatabaseException {
			// being mock setup
			Mockito.doThrow(RecordNotInDatabaseException.class).when(mSiteDao).updateLastUpdated(url, VALID_DATE);
			// end mock setup
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.updateLastUpdated(url, VALID_DATE),
					ASSERT_THROWS_FAIL_MSG);
			
			// being mock setup
			Mockito.doThrow(SQLException.class).when(mSiteDao).updateLastUpdated(url, VALID_DATE);
			// end mock setup
			
			assertThrows(DaoServiceException.class, 
					() -> simpleDaoService.updateLastUpdated(url, VALID_DATE),
					ASSERT_THROWS_FAIL_MSG);
		}
	}


}
