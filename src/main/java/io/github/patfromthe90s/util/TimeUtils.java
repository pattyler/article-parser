package io.github.patfromthe90s.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Utility class for working with Dates, Times, etc.
 * 
 * @author Patrick
 *
 */
public final class TimeUtils {
	
	public static final String UTC_ZONE_ID_STR = "UTC";
	public static final String JST_ZONE_ID_STR_SHORT = "JST";
	public static final String JST_ZONE_ID_STR;
	public static final ZoneId UTC_ZONE_ID;
	public static final ZoneId JST_ZONE_ID;
	
	static {
		UTC_ZONE_ID = ZoneId.of(UTC_ZONE_ID_STR);
		JST_ZONE_ID_STR = ZoneId.SHORT_IDS.get(JST_ZONE_ID_STR_SHORT);
		JST_ZONE_ID = ZoneId.of(JST_ZONE_ID_STR);
		
	}

	/**
	 * Converts a {@link ZonedDateTime} into its <code>UTC</code> equivalent, if it is not already a <code>UTC</code> time.
	 * @param zdt The datetime to convert
	 * @return THe UTC representation of <code>zdt</code>
	 */
	public static ZonedDateTime toUtc(ZonedDateTime zdt) {
		if (!zdt.getZone().equals(UTC_ZONE_ID))
			zdt = zdt.withZoneSameInstant(UTC_ZONE_ID);
		
		return zdt;
	}
	

}
