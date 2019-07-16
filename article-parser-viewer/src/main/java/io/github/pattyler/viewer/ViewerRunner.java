package io.github.pattyler.viewer;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.pattyler.backend.service.DaoService;
import io.github.pattyler.backend.util.TimeUtils;

/**
 * A testing class just for viewing articles on the command line. <br/>
 * Usage: <br/>
 * <code>java ViewerRunner [-from &lt;integer&gt;] [-to &lt;integer&gt;]</code>
 * 
 * @author Patrick
 *
 */
@SpringBootApplication(scanBasePackages = "io.github.pattyler")
public class ViewerRunner implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewerRunner.class);

	private static final int DEFAULT_FROM = 2;
	private static final int DEFAULT_TO = 0;

	private static final String FLAG_FROM = "-from";
	private static final String FLAG_TO = "-to";
	private static final String FLAG_HELP = "-help";

	private final ArticleViewer articleViewer;

	@Autowired
	public ViewerRunner(DaoService daoService) {
		articleViewer = new ArticleViewer(daoService);
	}

	@Override
	public void run(String... args) throws Exception {
		final int FROM_MINUS = parseNum(args, FLAG_FROM, DEFAULT_FROM);
		final int TO_MINUS = parseNum(args, FLAG_TO, DEFAULT_TO);

		final ZonedDateTime FROM = TimeUtils.toUtc(ZonedDateTime.now()).minusDays(FROM_MINUS);
		final ZonedDateTime TO = TimeUtils.toUtc(ZonedDateTime.now()).minusDays(TO_MINUS);

		articleViewer.printArticles(FROM, TO);
	}

	/**
	 * Finds <code>flag</code> in <code>args</code> and, if present, attempts to
	 * return the associated value. If no flag was found or there was a problem
	 * parsing the value, <code>defaultVal</code> is returned instead. <br/>
	 * E.g. <code>"java ViewerRunner -from 4 -to 2"</code> returns a value of
	 * <code>4</code> when <code>"parseNum(args, "from", 
	 * DEFAULT_FROM)"</code> is called.
	 * 
	 * @param args       The command-line arguments provided during application
	 *                   invocation.
	 * @param flag       The argument flag to search for.
	 * @param defaultVal Default value if argument not present, or problem parsing
	 *                   the value.
	 */
	private int parseNum(String[] args, String flag, int defaultVal) {
		int flagIndex = -1;
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++)
				if (args[i].equals(flag)) {
					flagIndex = i;
					break;
				}

			if (flagIndex != -1) {
				try {
					return Integer.parseInt(args[flagIndex + 1]);
				} catch (IndexOutOfBoundsException | NumberFormatException e) {
					// Print usage message, then continue application using default value
					LOGGER.warn(String.format("Could not parse: %s %s", flag, args[flagIndex + 1]));
					LOGGER.warn(String.format("Usage of flag %s: %n %s <integer>%n", flag, flag));
					LOGGER.warn(String.format("Falling back to default value <%d>%n", defaultVal));
				}
			}
		}

		return defaultVal;
	}

	private static void displayHelpAndExit() {
		System.out.printf("Usage: %n java ViewerRunner [-from <integer>] [-to <integer>]");
		System.exit(1);
	}

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals(FLAG_HELP))
			displayHelpAndExit();
		SpringApplication.run(ViewerRunner.class, args);
	}

}
