package io.github.patfromthe90s.service;

import java.net.URL;
import java.time.LocalDateTime;

import io.github.patfromthe90s.exception.DaoServiceException;

/**
 * Interface for a service layer for mediating between a higher-level client class and lower level DAOs.
 * 
 * @author Patrick
 */
public interface DaoService {
	
	/**
	 * Retrieves the last known time the given site was updated. <br/>
	 * Assumes {@code url} is a valid database entry.
	 * 
	 * @param url {@link URL} representing site to retrieve the information from.
	 * @return Last known time the given site was updated.
	 * @throws DaoServiceException if the {@code url} doesn't exist in the database, or if something else
	 * went wrong related to the database.
	 */
	public LocalDateTime getLastUpdated(URL url) throws DaoServiceException;
	
	
	/**
	 * Associate the given {@code url} with {@code newLastUpdated}.<br/>
	 * Assumes {@code newLastUpdated} is more recent than the current value in the database.
	 * 
	 * @param url {@link URL} representing site to update.
	 * @param newLastUpdated New {@code lastUpdated} time to insert.
	 * @throws DaoServiceException if the {@code url} doesn't exist in the database, or if something else
	 * went wrong related to the database.
	 */
	public void updateLastUpdated(URL url, LocalDateTime newLastUpdated) throws DaoServiceException;

}
