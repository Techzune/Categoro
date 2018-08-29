package com.jstremming.categoro.handling.action;

import java.io.File;

public class ActionSkip implements Action {
	File targetFile;

	public ActionSkip(final File file) {
		targetFile = file;
	}

	@Override
	public boolean commit() {
		return true;
	}

	@Override
	public File undo() {
		return targetFile;
	}
}
