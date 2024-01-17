package com.example;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
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
    void register(ActionEvent event) throws Exception {
        Dialog<User> regDialog = new RegDialog(new User("", "", 0));
        Optional<User> result = regDialog.showAndWait();
        if (result.isPresent()) {
            User user = result.get();

            PreparedStatement ps = null;
            ps = App.DBConnection.prepareStatement("INSERT INTO users VALUES (?, ?, ?)");
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getType());
            ps.execute();

            App.writeLog("System", "Registered new user " + user.getName());
        }
    }

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
        rs.next();
        if (!rs.getBoolean(1)) {
            App.writeLog("System", "can't find user '" + login + "'");
            return false;
        }

        ps = App.DBConnection.prepareStatement("SELECT CASE WHEN password = '" + password + "' THEN true ELSE false END FROM users WHERE login = '" + login + "'");
		ps.execute();
		rs = ps.getResultSet();
        rs.next();
        if (rs.getBoolean(1)) {
            App.userLogin = login;

            ps = App.DBConnection.prepareStatement("SELECT type FROM users WHERE login = '" + login + "'");
            ps.execute();
            rs = ps.getResultSet();
            rs.next();
            switch (rs.getInt(1)) {
                case 1:
                    App.setRoot("staff");
                    break;
            
                default:
                    App.setRoot("secondary");
                    break;
            }
            App.writeLog(login, "logged in");
            return true;
        }

        App.writeLog(login, "Incorrect password");
        return false;
    }

    @FXML
    void initialize() {

    }

}
