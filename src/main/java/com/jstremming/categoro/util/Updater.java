package com.jstremming.categoro.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Updater {
	private static final String VERSION = "1.1";

	public static boolean isNewVersion() {
		// get the latest VERSION from web
		final String latest = getLatestVersion();

		// return false if the check failed
		if (latest.isEmpty()) return false;

		// return true, if there is a new version
		return versionCompare(VERSION, latest) == -1;
	}

	public static String getLatestVersion() {
		try (final BufferedReader br = new BufferedReader(new InputStreamReader(new URL(
				"\\TODO").openStream()))) {
			return br.readLine();
		} catch (final IOException e) {
			Console.warn("An error occurred while checking for updates...");
			e.printStackTrace();
		}
		return "";
	}

	public static int versionCompare(final String str1, final String str2) {
		// split the strings by .
		final String[] ver1 = str1.split("\\.");
		final String[] ver2 = str2.split("\\.");

		int i = 0;
		// set index to first non-equal ordinal or length of shortest VERSION string
		while (i < ver1.length && i < ver2.length && ver1[i].equals(ver2[i])) {
			i++;
		}

		// compare first non-equal ordinal number
		if (i < ver1.length && i < ver2.length) {
			final int diff = Integer.valueOf(ver1[i]).compareTo(Integer.valueOf(ver2[i]));
			return Integer.signum(diff);
		}

		// the strings are equal or one string is a substring of the other
		// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
		return Integer.signum(ver1.length - ver2.length);
	}
}
