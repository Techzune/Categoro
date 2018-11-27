package com.jstremming.categoro.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Updater {

	public static boolean isNewVersion() {
		try {
			// get the latest VERSION from web
			final String latest = getLatestRelease("https://api.github.com/repos/techzune/categoro/releases/latest");
			if (latest == null || latest.isEmpty()) return false;

			// get the current build version
			final String version = getBuildVersion();
			if (version == null || version.isEmpty()) return false;

			// return true, if there is a new version
			return versionCompare(version, latest) == -1;

		} catch (final Exception e) {
			Console.warn("An error occurred while checking for updates...");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Uses HTTP GET to retrieve latest release version from GitHub API
	 * @param url the GitHub API link to latest version
	 */
	public static String getLatestRelease(String url) {
		try {
			// read from the URL
			String data = readURL(url);
			JSONObject jObj = (JSONObject) new JSONParser().parse(data);

			// read tag_name and get version number
			String versionString = (String) jObj.get("tag_name");
			Pattern p = Pattern.compile("(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)");
			Matcher m = p.matcher(versionString);

			// if version number exists, return it
			if (m.find()) return m.group();

		} catch (ParseException e) {
			Console.warn("An error occurred while checking for updates...");
			e.printStackTrace();
		}
		return null;
	}

	private static String readURL(String url) {
		// build a response string
		StringBuilder stringBuilder = new StringBuilder();
		String curLine;

		// append the URL data to the string
		try (final BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
			while ((curLine = br.readLine()) != null) stringBuilder.append(curLine);

		} catch (final IOException e) {
			Console.warn("An error occurred while checking for updates...");
			e.printStackTrace();
			return null;
		}

		// return the string
		return stringBuilder.toString();
	}

	private static int versionCompare(final String str1, final String str2) {
		// split the strings by .
		final String[] ver1 = str1.split("\\.");
		final String[] ver2 = str2.split("\\.");

		// move to next num while versions are equal to each other
		int i = 0;
		while (i < ver1.length && i < ver2.length && ver1[i].equals(ver2[i])) {
			i++;
		}

		// return the comparison of the next number
		if (i < ver1.length && i < ver2.length) {
			final int diff = Integer.valueOf(ver1[i]).compareTo(Integer.valueOf(ver2[i]));
			return Integer.signum(diff);
		}

		// the strings are equal or one string is a substring of the other
		// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
		return Integer.signum(ver1.length - ver2.length);
	}

	private static String getBuildVersion(){
		InputStream is = Updater.class.getClassLoader().getResourceAsStream("version.txt");
		if (is != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			return reader.lines().collect(Collectors.joining(System.lineSeparator()));
		}
		return "";
	}
}
