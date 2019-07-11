package io.github.patfromthe90s.backend.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Utility class for working with Dates, Times, etc.
 * 
 * @author Patrick
 *
 */
public final class TimeUtils {
	
	public static final ZoneId ZONE_UTC;
	public static final ZoneId ZONE_JST;
	
	static {
		ZONE_UTC = ZoneId.of("UTC");
		ZONE_JST = ZoneId.of("JST", ZoneId.SHORT_IDS);
		
	}

	/**
	 * Converts a {@link ZonedDateTime} into its <code>UTC</code> equivalent, if it is not already a <code>UTC</code> time.
	 * @param zdt The datetime to convert
	 * @return THe UTC representation of <code>zdt</code>
	 */
	public static ZonedDateTime toUtc(ZonedDateTime zdt) {
		if (!zdt.getZone().equals(ZONE_UTC))
			zdt = zdt.withZoneSameInstant(ZONE_UTC);
		
		return zdt;
	}
	

}
