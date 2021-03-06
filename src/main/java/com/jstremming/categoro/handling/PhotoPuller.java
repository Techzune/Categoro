package com.jstremming.categoro.handling;

import com.jstremming.categoro.handling.action.Action;
import com.jstremming.categoro.handling.action.ActionMove;
import com.jstremming.categoro.handling.action.ActionSkip;
import com.jstremming.categoro.util.Console;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class PhotoPuller {

	/** The File of the project's root path */
	private final File projectPath;

	/** The File of the unsorted folder */
	private File unsortedFolder;

	/** A map of the categories, Name -> File */
	private final HashMap<String, File> categories = new HashMap<>();

	/** Stack of Files to be sorted */
	private final Stack<File> unsortedFiles = new Stack<>();

	/** Stack of actions completed so far */
	private final Stack<Action> actions = new Stack<>();

	/**
	 * Initializes PhotoPuller with the project projectPath specified
	 * @param projectPath the projectPath to the working project
	 */
	public PhotoPuller(final File projectPath, final ProjectConfig config) throws IOException {
		// store the project path
		Console.debug("Pulling from: ", projectPath.getName());
		this.projectPath = projectPath;

		// pull all categories
		for (final String cat : config.getCategories().values()) {
			addCategory(cat);
		}

		// validate all directories
		validateDirectories();

		// add all the unsorted photos to the unsortedFiles
		Collections.addAll(unsortedFiles, getAllUnsortedPhotos());
	}

	/**
	 * Returns the opposite of {@link Stack#empty()}
	 */
	public boolean hasNext() {
		return !unsortedFiles.empty();
	}

	/**
	 * {@link Stack#peek()}
	 */
	public File peek() {
		return unsortedFiles.peek();
	}

	/**
	 * {@link Stack#pop()}
	 */
	public File pop() {
		if (unsortedFiles.empty()) return null;
		return unsortedFiles.pop();
	}

	/**
	 * Reverts the most recent action
	 */
	public void commitUndo() {
		// ignore commit if empty
		if (actions.empty()) return;

		final Action last = actions.pop();
		final File lastFile = last.undo();
		unsortedFiles.push(lastFile);
	}

	/**
	 * Moves the top of the stack to the target directory in the project path
	 * @param target the name of the target directory
	 */
	public void commitMove(final String target) {
		move(pop(), target);
	}

	/**
	 * Moves the file to the target directory in the project path
	 * Stores the action
	 * @param file the file to move
	 * @param target the name of the target directory
	 */
	public void move(final File file, final String target) {
		// ignore blank
		if (file == null) return;

		// make sure category exists
		if (!categories.containsKey(target)) {
			Console.warn("Invalid category name", target);
			commitSkip();
			return;
		}

		Console.debug("Moving", file.getName(), "to", target);
		final ActionMove move = new ActionMove(file, new File(categories.get(target), file.getName()));
		move.commit();
		actions.push(move);
	}

	/**
	 * Skips the top of the stack
	 * Stores the action
	 */
	public void commitSkip() {
		if (unsortedFiles.empty()) return;
		final ActionSkip skip = new ActionSkip(pop());
		skip.commit();
		actions.push(skip);
	}

	/**
	 * Adds a category to the project
	 */
	public void addCategory(final String catName) {
		Console.debug("Adding category:", catName);
		categories.put(catName, new File(projectPath, catName));
	}

	/**
	 * Returns array of files that end with png,jpg,jpeg
	 * from the unsorted folder
	 */
	public File[] getAllUnsortedPhotos() {
		Console.debug("Collecting unsorted photos");
		return unsortedFolder.listFiles((dir, name) -> name.toLowerCase().matches(".*(png|jpg|jpeg).*"));
	}

	/**
	 * Validates the directories in the PhotoPuller's project projectPath
	 */
	private void validateDirectories() throws IOException {
		Console.debug("Validating directories");

		Console.debug("Validating project path");
		validateDirectory(projectPath);

		Console.debug("Validating unsorted path");
		unsortedFolder = new File(projectPath, "unsorted");
		Console.debug("Unsorted folder path:", unsortedFolder.getAbsolutePath());
		validateDirectory(unsortedFolder);

		Console.debug("Validating file folders");
		for (final File folder : categories.values()) validateDirectory(folder);
	}

	/**
	 * Verifies path is a directory, creates it if it does not exist
	 * @throws IOException the path could not be a directory
	 */
	private void validateDirectory(final File pathFile) throws IOException {
		// checks if the path is not a file, or creates it if nonexistent
		if (!pathFile.isDirectory() && !pathFile.mkdir()) {
			throw new IOException("could not be a dir: " + projectPath);
		}
	}
}
