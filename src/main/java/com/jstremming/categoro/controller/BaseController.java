package com.jstremming.categoro.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Provides basic functionality to all controllers
 */
public class BaseController implements Initializable {

	private final String fxmlResourcePath;
	private final String title;

	/**
	 * Provides basic functionality to all controllers
	 * @param fxml the resource path to the fxml design file
	 * @param title the title of the window
	 */
	BaseController(final String fxml, final String title) {
		fxmlResourcePath = fxml;
		this.title = title;
	}

	/**
	 * Loads the fxmlResourcePath
	 */
	private FXMLLoader getFXMLLoader() {
		return new FXMLLoader(getClass().getResource(fxmlResourcePath));
	}

	/**
	 * Creates a stage with Title
	 * Applies application.css
	 * @throws IOException thrown by {@link FXMLLoader#load()}
	 */
	private Stage createStageFromFXML(final FXMLLoader fxmlLoader) throws IOException {
		final Stage stage = new Stage();

		stage.setTitle(title);
		stage.setScene(new Scene(fxmlLoader.load()));
		stage.getScene().getStylesheets().add("css/application.css");

		return stage;
	}

	/**
	 * Refer to:
	 * {@link Stage#show()}
	 */
	public <T> T show() {
		try {
			final FXMLLoader loader = getFXMLLoader();
			final Stage stage = createStageFromFXML(loader);
			stage.show();
			return loader.getController();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Refer to:
	 * {@link Stage#showAndWait()}
	 */
	public <T> T showAndWait() {
		try {
			final FXMLLoader loader = getFXMLLoader();
			final Stage stage = createStageFromFXML(loader);
			stage.showAndWait();
			return loader.getController();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {

	}
}
