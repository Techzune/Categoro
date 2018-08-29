package com.jstremming.categoro.handling.action;

import java.io.File;

public interface Action {
	boolean commit();
	File undo();
}
