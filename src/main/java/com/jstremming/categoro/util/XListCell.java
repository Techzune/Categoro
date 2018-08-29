package com.jstremming.categoro.util;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class XListCell extends ListCell<String> {
	private final HBox hbox = new HBox();
	private final Label lbl_text = new Label("");

	public XListCell() {
		super();
		final Pane spacer = new Pane();
		final Button btn_remove = new Button("delete");

		hbox.setAlignment(Pos.CENTER_LEFT);
		hbox.getChildren().addAll(lbl_text, spacer, btn_remove);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		btn_remove.setOnAction(event -> getListView().getItems().remove(getItem()));
	}

	@Override
	protected void updateItem(final String item, final boolean empty) {
		super.updateItem(item, empty);
		setText(null);
		setGraphic(null);

		if (item != null && !empty) {
			lbl_text.setText(item);
			setGraphic(hbox);
		}
	}
}
