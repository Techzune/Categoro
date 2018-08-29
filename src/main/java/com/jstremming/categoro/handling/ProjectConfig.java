package com.jstremming.categoro.handling;

import com.jstremming.categoro.util.Console;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ProjectConfig {
	private final File configFile;
	private final Properties props = new Properties();

	public ProjectConfig(final File projectDirectory) {
		configFile = new File(projectDirectory, "categoro.properties");
	}

	/**
	 * Loads the configuration from the configFile, if it exists
	 * @return if the load succeeds
	 */
	public boolean load() {
		// don't bother loading if non-existent
		if (!configFile.isFile()) return true;

		// load the file
		try (final FileReader reader = new FileReader(configFile)) {
			props.load(reader);
		} catch (final Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Writes the configuration to the configFile
	 * @return if the save succeeds
	 */
	public boolean save() {
		Console.debug("Saving configuration...");
		try (final FileWriter writer = new FileWriter(configFile)) {
			props.store(writer, "Categoro Configuration");
		} catch (final Exception ex) {
			ex.printStackTrace();
			Console.warn("Config save error");
			return false;
		}
		Console.debug("Project config saved");
		return true;
	}

	public String getProjectName() {
		return props.getProperty("projectName", "");
	}

	public void setProjectName(final String newVal) {
		props.setProperty("projectName", newVal);
	}

	public Map<String, String> getCategories() {
		// get the categories property, split by the comma
		Console.debug("Getting categories property...");
		final String[] result = props.getProperty("categories", "").split(", ");

		// check if the resulting split is blank
		if (result.length == 1 && result[0].equals("")) {
			// return empty list
			Console.debug("No categories to load");
			return new HashMap<>();
		} else {
			// convert to array and clean up blanks
			Console.debug("Converting categories to array");

			final Map<String, String> resultMap = new HashMap<>();
			String[] split;
			for (final String cat : result) {
				split = cat.split("::");

				// ignore any broken values
				if (split.length < 2 || split[0].trim().isEmpty() || split[1].trim().isEmpty()) {
					Console.warn("Broken category:", cat);
					continue;
				}

				resultMap.put(split[0], split[1]);
			}

			// clear any blanks (just in case!)
			resultMap.remove("");
			resultMap.remove(null);
			resultMap.remove(" ");

			return resultMap;
		}
	}

	public void setCategories(final Map<String, String> categories) {
		// build a string for storage
		final StringBuilder cats = new StringBuilder();

		// iterate through categories
		for (final Map.Entry<String, String> entry : categories.entrySet()) {
			// build the string KEY::VALUE
			cats.append(entry.getKey()).append("::").append(entry.getValue()).append(", ");
		}

		// trim off comma on end
		String catString = cats.toString();
		catString = catString.substring(0, cats.length() - 2);

		// store the property
		Console.debug("Storing categories:", catString);
		props.setProperty("categories", catString);
	}


}
