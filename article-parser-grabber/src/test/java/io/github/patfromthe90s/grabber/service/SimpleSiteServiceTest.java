package io.github.patfromthe90s.grabber.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.patfromthe90s.grabber.exception.GenericHTTPException;
import io.github.patfromthe90s.grabber.exception.HeaderNotPresentException;
import io.github.patfromthe90s.grabber.exception.SiteServiceException;
import io.github.patfromthe90s.grabber.http.Interactor;
import io.github.patfromthe90s.grabber.service.SimpleSiteService;
import io.github.patfromthe90s.grabber.service.SiteService;

public class SimpleSiteServiceTest {
	
	@Mock private Interactor mHtmlInteractor;
	@Mock private Interactor mJsonInteractor;
	
	private SiteService siteService;
	
	private static final String MAPS_EXCEPTION_FAIL_MSG = "Exception should have been mapped to a different exception";

	@BeforeEach
	public void setupLocal() {
		MockitoAnnotations.initMocks(this);
		siteService = new SimpleSiteService(mHtmlInteractor, mJsonInteractor);
	}
	
	@Nested
	@DisplayName("When checked exception thrown")
	class WhenCheckedExceptionThrown {
		
		@Test
		@DisplayName("When GenericHTTPException, then mapped correctly.")
		public void whenHttpExceptionThrown_thenServiceExceptionThrown() throws GenericHTTPException, HeaderNotPresentException {
			when(mHtmlInteractor.getLastUpdated(anyString())).thenThrow(GenericHTTPException.class);
			when(mHtmlInteractor.get(anyString())).thenThrow(GenericHTTPException.class);
			when(mJsonInteractor.get(anyString())).thenThrow(GenericHTTPException.class);
			
			assertThrows(SiteServiceException.class, () -> siteService.getLastUpdated("http://www.test.com"), MAPS_EXCEPTION_FAIL_MSG);
			assertThrows(SiteServiceException.class, () -> siteService.getHtml("http://www.test.com"), MAPS_EXCEPTION_FAIL_MSG);
			assertThrows(SiteServiceException.class, () -> siteService.getJson("http://www.test.com"), MAPS_EXCEPTION_FAIL_MSG);
		}
		
		@Test
		@DisplayName("When HeaderNotPresentException, then mapped correctly.")
		public void whenHeaderNotPresentThrown_thenServiceExceptionThrown() throws GenericHTTPException, HeaderNotPresentException {
			when(mHtmlInteractor.getLastUpdated(anyString())).thenThrow(HeaderNotPresentException.class);
			assertThrows(SiteServiceException.class, () -> siteService.getLastUpdated("http://www.test.com"), MAPS_EXCEPTION_FAIL_MSG);
		}

		
	}
	
}
