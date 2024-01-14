package com.example;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PrimaryController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button enterButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void buttonPressed(ActionEvent event) throws Exception {
        login(loginField.getText(), passwordField.getText());
    }

    @FXML
    void keyPressed(KeyEvent event) throws Exception {
        if (event.getCode() == KeyCode.ENTER) {
            login(loginField.getText(), passwordField.getText());
        }
    }

    boolean login(String login, String password) throws Exception {
        PreparedStatement ps = null;

        ps = App.DBConnection.prepareStatement("SELECT EXISTS(SELECT login FROM users WHERE login = '" + login + "')");
		ps.execute();
		ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            if (!rs.getBoolean(1)) return false;
        }

        ps = App.DBConnection.prepareStatement("SELECT CASE WHEN password = '" + password + "' THEN true ELSE false END FROM users WHERE login = '" + login + "'");
		ps.execute();
		rs = ps.getResultSet();
        while (rs.next()) {
            if (rs.getBoolean(1)) {
                App.userLogin = login;
                App.setRoot("secondary");
                return true;
            }
        }

        return false;
    }

    @FXML
    void initialize() {

    }

}
