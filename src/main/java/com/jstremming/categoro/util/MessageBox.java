package com.jstremming.categoro.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MessageBox {
	/**
	 * Generates a standard YES, NO Alert
	 * @param type the AlertType for the alert
	 * @param content the text content of the alert
	 * @return the Alert object
	 */
	public static Alert generateYesNo(final Alert.AlertType type, final String... content) {
		final Alert alert = new Alert(type, String.join("\n", content), ButtonType.YES, ButtonType.NO);
		alert.setHeaderText(null);
		return alert;
	}

	/**
	 * Generates a standard Alert
	 * @param type the AlertType for the alert
	 * @param content the text content of the alert
	 * @return the Alert object
	 */
	public static Alert generate(final Alert.AlertType type, final String... content) {
		final Alert alert = new Alert(type, String.join("\n", content));
		alert.setHeaderText(null);
		return alert;
	}

	/**
	 * Generates an Alert with the exception's error message
	 * Shows and waits for the Alert to be cleared
	 */
	public static void throwError(final Exception ex) {
		final Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Something went wrong");
		alert.setHeaderText("An exception has occurred");
		alert.setContentText(ex.getMessage());
		alert.showAndWait();
	}
}
