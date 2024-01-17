package com.example;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class AddDialog extends Dialog<Thing> {

    private Thing thing;

    private TextField nameField;
    private TextField descriptionField;

    public AddDialog(Thing thing) {
        super();
        this.setTitle("Добавить вещь");
        this.thing = thing;
        buildUI();
        setPropertyBindings();
        setResultConverter();
    }

    private void buildUI() {
        Pane pane = createGridPane();
		getDialogPane().setContent(pane);

		getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		Button button = (Button) getDialogPane().lookupButton(ButtonType.OK);
		button.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (!validateDialog()) {
					event.consume();
				}
			}

			private boolean validateDialog() {
				if ((nameField.getText().isEmpty()) || (descriptionField.getText().isEmpty())) {
					return false;
				}
				return true;
			}
		});
    }

    private void setPropertyBindings() {
        nameField.textProperty().bindBidirectional(thing.nameProperty());
		descriptionField.textProperty().bindBidirectional(thing.descriptionProperty());
		thing.setOwner(App.userLogin);
    }

    private void setResultConverter() {
        Callback<ButtonType, Thing> thingResultConverter = new Callback<ButtonType, Thing>() {
			@Override
			public Thing call(ButtonType param) {
				if (param == ButtonType.OK) {
					return thing;
				} else {
					return null;
				}
			}
		};
		setResultConverter(thingResultConverter);
    }

    public Pane createGridPane() {
		VBox content = new VBox(10);

		Label nameLabel = new Label("Имя");
		Label descriptionLabel = new Label("Описание");
		this.nameField = new TextField();
		this.descriptionField = new TextField();
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(5);
		grid.add(nameLabel, 0, 0);
		grid.add(descriptionLabel, 0, 1);
		grid.add(nameField, 1, 0);
		GridPane.setHgrow(this.nameField, Priority.ALWAYS);
		grid.add(descriptionField, 1, 1);
		GridPane.setHgrow(this.descriptionField, Priority.ALWAYS);

		content.getChildren().add(grid);

		return content;
	}

}
