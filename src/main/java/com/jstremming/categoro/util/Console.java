package com.jstremming.categoro.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {
	@SuppressWarnings("FieldCanBeLocal")
	private static final boolean DEBUG_ENABLED = true;

	/**
	 * Outputs with a prefix of [DEBUG]
	 * (Will not print without debug enabled)
	 */
	public static void debug(final Object... objs) {
		if (DEBUG_ENABLED) printObjs("[debug]", objs);
	}

	/**
	 * Outputs with a prefix of [INFO]
	 */
	public static void info(final Object... objs) {
		printObjs("[INFO]", objs);
	}

	/**
	 * Outputs with a prefix of [WARN]
	 */
	public static void warn(final Object... objs) {
		printObjs("[WARN]", objs);
	}

	/**
	 * Outputs with a prefix of [SEVERE]
	 */
	public static void severe(final Object... objs) {
		printObjs("[SEVERE]", objs);
	}

	/**
	 * Prints objects separated by spaces in a string with specified prefix
	 */
	private static void printObjs(final String prefix, final Object... objs) {
		// build the string from the objects
		final StringBuilder sb = new StringBuilder(prefix);
		for (final Object o : objs) {
			sb.append(" ").append(o);
		}

		// print the string with timestamp
		final String timeStamp = new SimpleDateFormat("<yyyy-MM-dd HH:mm:ss> ").format(new Date());
		System.out.println(timeStamp + sb.toString());
	}
}
