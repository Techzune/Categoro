package com.jstremming.categoro.controller;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class CategoriesController extends BaseController {
	@FXML ListView<String> list_cats;

	public CategoriesController() {
		super("/fxml/WindowCategories.fxml", "Categories");
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		super.initialize(location, resources);
	}

	/**
	 * Returns the stage of the window
	 */
	public Stage getStage() {
		return (Stage) list_cats.getScene().getWindow();
	}

	/**
	 * Refer to:
	 * {@link ListView#setOnMouseClicked}
	 */
	public void setOnMouseClicked(final EventHandler<? super MouseEvent> eventHandler) {
		list_cats.setOnMouseClicked(eventHandler);
	}

	public void setCategories(final Map<String, String> classes) {
		final ObservableList<String> list = list_cats.getItems();
		list.clear();
		list.add("X \u21a6 SKIP");
		list.add("Z \u21a6 UNDO");
		for (final Map.Entry<String, String> cat : classes.entrySet()) {
			list.add(cat.getKey() + " \u21a6 " + cat.getValue());
		}
	}
}
