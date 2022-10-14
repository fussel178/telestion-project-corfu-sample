package de.wuespace.telestion.project.corfu.sample.pkg.util;

import java.time.Instant;

public class TimeUtils {
	public static long toNanoSeconds(Instant timestamp) {
		return timestamp.getEpochSecond() * 1000000000L + timestamp.getNano();
	}
}
