package com.example;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class RegDialog extends Dialog<User> {

    private User user;

    private TextField nameField;
    private TextField passwordField;
    private RadioButton clientRadio;
    private RadioButton staffRadio;

    public RegDialog(User user) {
        super();
        this.setTitle("Новый пользователь");
        this.user = user;
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
				if ((nameField.getText().isEmpty()) || (passwordField.getText().isEmpty())) {
					return false;
				}
				return true;
			}
		});
    }

    private void setPropertyBindings() {
        nameField.textProperty().bindBidirectional(user.nameProperty());
		passwordField.textProperty().bindBidirectional(user.passwordProperty());
    }

    private void setResultConverter() {
        Callback<ButtonType, User> userResultConverter = new Callback<ButtonType, User>() {
			@Override
			public User call(ButtonType param) {
				if (param == ButtonType.OK) {
					if (clientRadio.isSelected()) user.setType(0);
					else if (staffRadio.isSelected()) user.setType(1);
					return user;
				} else {
					return null;
				}
			}
		};
		setResultConverter(userResultConverter);
    }

    public Pane createGridPane() {
		VBox content = new VBox(10);
		Label nameLabel = new Label("Имя");
		Label passwordLabel = new Label("Пароль");
		this.nameField = new TextField();
		this.passwordField = new TextField();
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(5);
		grid.add(nameLabel, 0, 0);
		grid.add(passwordLabel, 0, 1);
		grid.add(nameField, 3, 0);
		GridPane.setHgrow(this.nameField, Priority.ALWAYS);
		grid.add(passwordField, 3, 1);
		GridPane.setHgrow(this.passwordField, Priority.ALWAYS);
		content.getChildren().add(grid);


        ToggleGroup tg = new ToggleGroup();
        Label who = new Label("Вы кто?");
        this.clientRadio = new RadioButton();
        this.clientRadio.setText("Я кто\n(можно будет спамить заявками,\nчтобы работники плакали)");
        this.staffRadio = new RadioButton();
        this.staffRadio.setText("Я теперь тут работаю\n(подписать контракт и продать душу,\nзато можно будет пакостить и отменять все заявки)");
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(5);
        grid.add(who, 0, 0);
        clientRadio.setSelected(true);
        clientRadio.setToggleGroup(tg);
        grid.add(clientRadio, 0, 1);
		GridPane.setHgrow(this.clientRadio, Priority.ALWAYS);
        staffRadio.setToggleGroup(tg);
        grid.add(staffRadio, 1, 1);
		GridPane.setHgrow(this.staffRadio, Priority.ALWAYS);
		content.getChildren().add(grid);

		return content;
	}

}
