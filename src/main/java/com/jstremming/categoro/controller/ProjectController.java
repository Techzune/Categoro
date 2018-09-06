package com.jstremming.categoro.controller;

import com.jstremming.categoro.handling.ProjectConfig;
import com.jstremming.categoro.util.Console;
import com.jstremming.categoro.util.MessageBox;
import com.jstremming.categoro.util.Updater;
import com.jstremming.categoro.util.XListCell;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;

public class ProjectController extends BaseController {

	@FXML private TextField txt_projectDir;
	@FXML private TextField txt_projectName;
	@FXML private TextField txt_addCat;
	@FXML private TextField txt_addCatKey;
	@FXML private VBox vbox_project;
	@FXML private ListView<String> list_cats;
	@FXML private Button btn_sort;

	private File projectPath;
	private ProjectConfig loadedConfig;
	private Map<String, String> categories = new HashMap<>();


	public ProjectController() {
		super("/fxml/WindowProject.fxml", "Project Selection");
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		super.initialize(location, resources);

		// check for updates
		if (Updater.isNewVersion()) {
			// ask the user to download
			final Optional<ButtonType> result = MessageBox.generateYesNo(Alert.AlertType.INFORMATION,
					"Categoro has an update available!\n"
							+ "Do you want to download it?").showAndWait();

			// if the user responds yes
			if (result.isPresent() && result.get() == ButtonType.YES) {
				// open a browser to the latest release then exit
				try {
					Desktop.getDesktop().browse(new URL("http://github.com/Techzune/Categoro/releases/latest").toURI());
					Platform.exit();
				} catch (final Exception e) {
					// oh noes
					Console.severe("An error occurred opening the browser.");
					e.printStackTrace();
				}
			}
		}

		// make cells removable
		list_cats.setCellFactory(param -> new XListCell());
		list_cats.getItems().addListener((ListChangeListener<String>) change -> {
			while (change.next()) {
				if (change.wasRemoved()) {
					final List<? extends String> list = change.getRemoved();

					for (final String item : list) {
						categories.remove(item.split(" \u21a6 ")[0]);
					}
				}
			}
		});
	}

	/**
	 * Triggered when "browse..." is clicked for picking project directory
	 */
	@FXML
	public void btnProjectDirBrowse() {
		// prompt for a directory
		final DirectoryChooser chooser = new DirectoryChooser();
		projectPath = chooser.showDialog(txt_projectDir.getScene().getWindow());
		if (projectPath == null) return;

		// change the TextField to the directory
		txt_projectDir.setText(projectPath.getAbsolutePath());

		// load configuration
		loadedConfig = new ProjectConfig(projectPath);
		if (!loadedConfig.load()) {
			MessageBox.generate(AlertType.WARNING, "Failed to load project configuration.");
		}
		categories = loadedConfig.getCategories();

		// populate boxes
		txt_projectName.setText(loadedConfig.getProjectName());
		final ObservableList<String> items = list_cats.getItems();
		items.clear();
		for (final Map.Entry<String, String> entry : categories.entrySet()) {
			items.add(entry.getKey() + " \u21a6 " + entry.getValue());
		}

		// prevent multiple keys
		txt_addCatKey.textProperty().addListener((o, oldV, newV) -> {
			if (newV.length() > 1 && oldV.length() <= 1) txt_addCatKey.setText(oldV);
		});

		// enable the project options
		vbox_project.setDisable(false);
		btn_sort.setDisable(false);
	}

	/**
	 * Triggered when "add category" is clicked for adding a category
	 */
	@FXML
	public void btnAddCat() {
		// get the text and trim any whitespace
		final String newCat = txt_addCat.getText().trim();
		final String newCatKey = txt_addCatKey.getText().trim().toUpperCase();

		//region Input Validation

		// ignore blanks
		if (newCat.isEmpty()) return;
		if (newCatKey.isEmpty()) return;

		// reject "unsorted"
		if (newCat.equalsIgnoreCase("unsorted")) {
			final Alert alert = MessageBox.generate(AlertType.ERROR,
					"INVALID CATEGORY",
					"\"unsorted\" is a reserved folder for unsorted pictures to be stored in!");
			alert.showAndWait();
			return;
		}

		// reject invalid folder name characters
		if (newCat.matches(".*[^\\w][,\\\\/:*?\"<>|]*.*")) {
			final Alert alert = MessageBox.generate(AlertType.ERROR,
					"INVALID CHARACTER",
					"Sorry, you can't use: , \\ / : * ? \" < > |");
			alert.showAndWait();
			return;
		}

		// reject duplicates
		if (categories.containsValue(newCat)) {
			final Alert alert = MessageBox.generate(AlertType.ERROR,
					"DUPLICATE CATEGORY",
					"That category already exists!");
			alert.showAndWait();
			return;
		}
		if (categories.containsKey(newCatKey)) {
			final Alert alert = MessageBox.generate(AlertType.ERROR,
					"DUPLICATE KEY",
					"That key is already being used!");
			alert.showAndWait();
			return;
		}

		// reject multiple key characters
		if (newCatKey.length() > 1) {
			final Alert alert = MessageBox.generate(AlertType.ERROR,
					"MULTIPLE KEYS",
					"You can only use ONE key per category!");
			alert.showAndWait();
			return;
		}

		// reject reserved keys
		if (newCatKey.equals("Z") || newCat.equals("X")) {
			final Alert alert = MessageBox.generate(AlertType.ERROR,
					"RESERVED KEYS",
					"You cannot use Z or X!");
			alert.showAndWait();
			return;
		}
		//endregion

		// add the new category
		categories.put(newCatKey, newCat);
		list_cats.getItems().add(newCatKey + " \u21a6 " + newCat);
	}

	/**
	 * Triggered when "Start Sorting" is clicked
	 */
	@FXML
	public void btnSort() {
		// store the project name
		loadedConfig.setProjectName(txt_projectName.getText());

		// store the project categories
		loadedConfig.setCategories(categories);

		// save the configuration to file
		loadedConfig.save();

		// close project window (self)
		final Stage stage = (Stage) btn_sort.getScene().getWindow();
		stage.hide();

		// open main window and load project
		final MainController controller = new MainController().show();
		controller.loadProject(projectPath, loadedConfig);
	}
}
