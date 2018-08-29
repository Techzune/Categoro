package com.jstremming.categoro.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ClassesController extends BaseController {
	@FXML ListView<String> list_classes;

	public ClassesController() {
		super("/fxml/WindowClasses.fxml", "Classes");
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		super.initialize(location, resources);
	}

	/**
	 * Returns the stage of the window
	 */
	public Stage getStage() {
		return (Stage) list_classes.getScene().getWindow();
	}

	public void setClasses(final Map<String, String> classes) {
		final ObservableList<String> list = list_classes.getItems();
		list.clear();
		list.add("X \u21a6 SKIP");
		list.add("Z \u21a6 UNDO");
		for (final Map.Entry<String, String> cla : classes.entrySet()) {
			list.add(cla.getKey() + " \u21a6 " + cla.getValue());
		}
	}
}
