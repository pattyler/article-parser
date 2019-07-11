package io.github.patfromthe90s.grabber.runner;

/**
 * Marker interface to mark a runnable class to be injected into, and maybe called by, {@link SpringApplicationRunner}.
 * 
 * @author Patrick
 *
 */
public interface Runner {

	/**
	 * The runnable code.
	 */
	public void run();
	
}
