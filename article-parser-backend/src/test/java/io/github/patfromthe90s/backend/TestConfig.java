package io.github.patfromthe90s.backend;

import org.springframework.boot.SpringBootConfiguration;

/**
 * Marker configuration class so that the the src/test/resources directory is picked up by Spring, and the files
 * autowired into / picked-up by the tests.
 * 
 * @author Patrick
 *
 */
@SpringBootConfiguration
public class TestConfig {
}
