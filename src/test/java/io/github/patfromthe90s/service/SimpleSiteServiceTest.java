package io.github.patfromthe90s.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.github.patfromthe90s.exception.GenericHTTPException;
import io.github.patfromthe90s.exception.HeaderNotPresentException;
import io.github.patfromthe90s.exception.SiteServiceException;
import io.github.patfromthe90s.http.Interactor;

public class SimpleSiteServiceTest {
	
	private Interactor mHtmlInteractor;
	private Interactor mJsonInteractor;
	
	private SiteService siteService;
	
	private static final String MAPS_EXCEPTION_FAIL_MSG = "Exception should have been mapped to a different exception";

	@BeforeEach
	public void setupLocal() {
		// mock setup
		mHtmlInteractor = Mockito.mock(Interactor.class);
		mJsonInteractor = Mockito.mock(Interactor.class);
		
		siteService = new SimpleSiteService(mHtmlInteractor, mJsonInteractor);
	}
	
	@Nested
	@DisplayName("Check correct exceptions thrown")
	class Exceptions {
		
		@Test
		@DisplayName("When HTTP exceptions are thrown")
		public void whenHttpExceptionThrown_thenServiceExceptionThrown() throws GenericHTTPException, HeaderNotPresentException {
			Mockito.when(mHtmlInteractor.getLastUpdated(Mockito.anyString())).thenThrow(GenericHTTPException.class);
			Mockito.when(mHtmlInteractor.get(Mockito.anyString())).thenThrow(GenericHTTPException.class);
			Mockito.when(mJsonInteractor.get(Mockito.anyString())).thenThrow(GenericHTTPException.class);
			
			assertThrows(SiteServiceException.class, () -> siteService.getLastUpdated("http://www.test.com"), MAPS_EXCEPTION_FAIL_MSG);
			assertThrows(SiteServiceException.class, () -> siteService.getHtml("http://www.test.com"), MAPS_EXCEPTION_FAIL_MSG);
			assertThrows(SiteServiceException.class, () -> siteService.getJson("http://www.test.com"), MAPS_EXCEPTION_FAIL_MSG);
		}
		
	}
	
	

}
