package io.github.patfromthe90s.global;

import org.junit.jupiter.api.BeforeAll;

import io.github.patfromthe90s.util.PropertiesUtil;

/**
 * Test class for initialising the properties file. <br/>
 * <b>Note</b>: This is a quick / temporary solution.
 * 
 * @author Patrick
 *
 */
public class GlobalTest {
	
	@BeforeAll
	public static void setupGlobal() {
		PropertiesUtil.load("src\\main\\resources\\app.properties");
	}

}
