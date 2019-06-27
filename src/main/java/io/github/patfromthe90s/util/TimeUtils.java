package io.github.patfromthe90s.util;

import java.time.ZoneId;

/**
 * Utility class for working with Dates, Times, etc.
 * 
 * @author Patrick
 *
 */
public final class TimeUtils {
	
	public static final String UTC_ZONE_ID_STR = "UTC";
	public static final ZoneId UTC_ZONE_ID;
	
	static {
		UTC_ZONE_ID = ZoneId.of(UTC_ZONE_ID_STR);
	}
}
