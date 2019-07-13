package io.github.patfromthe90s.grabber.runner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * The single {@link CommandLineRunner} for this application. This should call and run the injected {@link Runner}s.
 * @author Patrick
 *
 */
@Component
@Profile("!test")
public class SpringApplicationRunner implements CommandLineRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringApplicationRunner.class);
	
	private final List<Runner> runners;

	@Autowired
	public SpringApplicationRunner(List<Runner> runners) {
		this.runners = runners;
	}
	
	@Override
	public void run(String... args) throws Exception {
		runners.stream()
				.forEach( runner -> {
					LOGGER.info("Starting runner [" + runner.getClass().getName() + "]");
					runner.run();
					LOGGER.info("Finished runner [" + runner.getClass().getName() + "]");
				});
	}

}
