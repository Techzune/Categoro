package com.jstremming.categoro.handling.action;

import com.jstremming.categoro.util.Console;

import java.io.File;

public class ActionMove implements Action {
	private final File from;
	private final File to;

	public ActionMove(final File from, final File to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * Renames the path "from" to the path "to"
	 */
	public boolean commit() {
		return from.renameTo(to);
	}

	/**
	 * Renames the path "to" to the path "from"
	 * (reverse of {@link ActionMove#commit()})
	 */
	public File undo() {
		to.renameTo(from);
		Console.debug("Moving", to.getName(), "to", from.getParentFile().getName());
		return from;
	}

}
