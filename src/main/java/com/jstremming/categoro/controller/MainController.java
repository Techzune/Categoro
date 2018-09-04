package com.jstremming.categoro.controller;

import com.jstremming.categoro.handling.PhotoPuller;
import com.jstremming.categoro.handling.ProjectConfig;
import com.jstremming.categoro.util.MessageBox;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController extends BaseController {

	@FXML ImageView img_main;
	@FXML Label lbl_imgname;

	private PhotoPuller photoPuller;
	private File projectPath;
	private ProjectConfig config;

	private Map<String, String> categories;

	public MainController() {
		super("/fxml/WindowMain.fxml", "Categoro");
	}


	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		super.initialize(location, resources);
		lbl_imgname.setText("Loading project...");
	}

	/**
	 * Returns the stage of the window
	 */
	public Stage getStage() {
		return (Stage) img_main.getScene().getWindow();
	}

	/**
	 * Loads a project into the Main Window
	 */
	public void loadProject(final File projectPath, final ProjectConfig config) {
		// store the project
		this.projectPath = projectPath;
		this.config = config;

		// open a PhotoPuller
		try {
			photoPuller = new PhotoPuller(projectPath, config);
		} catch (final Exception e) {
			// throw an error through the MessageBox
			MessageBox.throwError(e);
			e.printStackTrace();
		}

		// pull config keys
		categories = config.getCategories();

		// prepare key-press event
		lbl_imgname.getScene().setOnKeyPressed(e -> {
			final KeyCode key = e.getCode();
			keyPress(key.toString());
		});

		// load classes window
		final ClassesController classesController = (new ClassesController()).show();
		classesController.setClasses(categories);
		classesController.setOnMouseClicked((event) -> {
			// get the selected item
			final String selected = classesController.list_classes.getSelectionModel().getSelectedItem();
			if (selected == null || selected.isEmpty()) return;
			// clear the selection
			classesController.list_classes.getSelectionModel().clearSelection();
			// pass the first character of the selected item (should be a letter)
			keyPress(selected.split(" ")[0]);
		});

		// position classes window
		final Stage stage = getStage();
		final Stage classesStage = classesController.getStage();
		classesStage.setX(stage.getX() + stage.getWidth() + 5);
		classesStage.setY(stage.getY());

		// load the first image
		loadImage();
	}

	/**
	 * Loads the current image, if it exists
	 * If not, informs the user
	 */
	public void loadImage() {
		// check if images remain
		if (photoPuller.hasNext()) {
			// get the current image
			final File imgFile = photoPuller.peek();
			// set the image
			img_main.setImage(new Image(imgFile.toURI().toString()));
			// set the text for image
			lbl_imgname.setText(imgFile.getName());
		} else {
			// clear the image
			img_main.setImage(null);
			// change the text
			lbl_imgname.setText("NO MORE IMAGES REMAIN");
			// notify the user
			MessageBox.generate(Alert.AlertType.INFORMATION, "No more images to sort!").show();
		}
	}

	/**
	 * Handle a category/key press from the user
	 */
	public void keyPress(final String key) {
		// switch based on the key pressed
		switch (key) {
			case "Z":
				// undo key
				photoPuller.commitUndo();
				loadImage();
				break;
			case "X":
				// skip key
				photoPuller.commitSkip();
				loadImage();
				break;
		}

		// check if the key is a category
		if (categories.containsKey(key.toUpperCase())) {
			// commit a move
			photoPuller.commitMove(categories.get(key.toUpperCase()));
			loadImage();
		}
	}

}
