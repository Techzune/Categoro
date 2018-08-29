package com.jstremming.categoro;

import com.jstremming.categoro.controller.ProjectController;
import com.jstremming.categoro.util.Console;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class MainApplication extends Application {

	@Override
	public void start(final Stage primaryStage) {
		new ProjectController().show();
	}

	public static void main(final String[] args) {
		if (args.length == 0 || !args[0].equalsIgnoreCase("nolog")) {
			try {
				// set the log file
				System.setOut(new PrintStream(new FileOutputStream("categoro.log")));
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			Console.warn("Logging to file has been disabled!");
		}
		launch(args);
	}
}
