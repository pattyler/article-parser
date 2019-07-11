package io.github.patfromthe90s.backend.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TimeUtilsTest {
	
	@Test
	@DisplayName("When non-UTC time given, then returned value is converted")
	public void whenNonUtcDateGiven_thenConverted() {
		ZonedDateTime nonUtc = ZonedDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(3)));
		ZonedDateTime utc = nonUtc.withZoneSameInstant(ZoneId.of("UTC"));
		ZonedDateTime convertedZdt = TimeUtils.toUtc(nonUtc);
		assertEquals(utc, convertedZdt);
	}
	
	@Test
	@DisplayName("When UTC time given, then returned value not changed")
	public void whenNonUtcDateGiven_thenSameValueReturned() {
		ZonedDateTime utc = ZonedDateTime.now(ZoneId.of("UTC"));
		ZonedDateTime convertedZdt = TimeUtils.toUtc(utc);
		assertEquals(utc, convertedZdt);
	}
	
	@Test
	@DisplayName("When non-UTC time given, then original value is not mutated")
	public void whenNonUtcDateGiven_thenOriginalNotMutated() {
		ZonedDateTime nonUtc = ZonedDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.ofHours(3)));
		ZonedDateTime nonUtcCopy = ZonedDateTime.ofInstant(nonUtc.toInstant(), ZoneId.ofOffset("UTC", ZoneOffset.ofHours(3)));
		TimeUtils.toUtc(nonUtc);
		assertEquals(nonUtcCopy, nonUtc);
	}


}
