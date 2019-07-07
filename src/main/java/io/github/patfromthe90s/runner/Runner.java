package io.github.patfromthe90s.runner;

/**
 * Marker interface to mark a runnable class that may be called by {@link SpringApplicationRunner}.
 * 
 * @author Patrick
 *
 */
public interface Runner {

	/**
	 * The code to be called and run.
	 */
	public void run();
	
}
