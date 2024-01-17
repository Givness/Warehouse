package com.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class SecondaryController {

    @FXML
    private AnchorPane pane;

    @FXML
    private TableView<Thing> table;

    @FXML
    private TableColumn<Thing, String> nameCol;

    @FXML
    private TableColumn<Thing, String> descriptionCol;

    @FXML
    private TableColumn<Thing, String> statusCol;

    @FXML
    private Button cancelButton;

    @FXML
    private Text username;

    @FXML
    void tableClick(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY) & table.getSelectionModel().getSelectedItem() != null) {
            switch (table.getSelectionModel().getSelectedItem().getStatus()) {
                case 0:
                    cancelButton.setText("Отменить");
                    break;
        
                case 1:
                    cancelButton.setText("Отменить");
                    break;
                
                case 2:
                    cancelButton.setText("Забрать");
                    break;
                    
                case 3:
                    cancelButton.setText("Отменить");
                    break;

                case 4:
                    cancelButton.setText("Отменить");
                    break;

                case 5:
                    cancelButton.setText("Удалить");
                    break;

                default:
                    cancelButton.setText("Что?");
                    break;
            }
        }
    }

    @FXML
    void cancelClick(ActionEvent event) throws Exception {
        Thing thing = table.getSelectionModel().getSelectedItem();
        if (thing == null) return;
        PreparedStatement ps = null;
        switch (thing.getStatus()) {
            case 0:
                ps = App.DBConnection.prepareStatement("DELETE FROM things WHERE name ='" + thing.getName() + "'");
                App.writeLog(App.userLogin, "Cancel bring request for thing " + thing.getName());
                break;
        
            case 1:
                ps = App.DBConnection.prepareStatement("DELETE FROM things WHERE name ='" + thing.getName() + "'");
                App.writeLog(App.userLogin, "Cancel deliver request for thing " + thing.getName());
                break;

            case 2:
                ps = App.DBConnection.prepareStatement("UPDATE things SET status = 3 WHERE name ='" + thing.getName() + "'");
                App.writeLog(App.userLogin, "Create take request for thing " + thing.getName());
                break;

            case 3:
                ps = App.DBConnection.prepareStatement("UPDATE things SET status = 2 WHERE name ='" + thing.getName() + "'");
                App.writeLog(App.userLogin, "Cancel take request for thing " + thing.getName());
                break;

            case 4:
                ps = App.DBConnection.prepareStatement("UPDATE things SET status = 2 WHERE name ='" + thing.getName() + "'");
                App.writeLog(App.userLogin, "Cancel take request for thing " + thing.getName());
                break;

            case 5:   
                ps = App.DBConnection.prepareStatement("DELETE FROM things WHERE name ='" + thing.getName() + "'");
                App.writeLog(App.userLogin, "Delete canceled thing " + thing.getName());
                break;
            default:
                return;
        }
		ps.execute();
        refreshTable();
    }

    @FXML
    void addClick(ActionEvent event) throws Exception {
        Dialog<Thing> addDialog = new AddDialog(new Thing("", "", 0, ""));
        Optional<Thing> result = addDialog.showAndWait();
        if (result.isPresent()) {
            Thing thing = result.get();

            PreparedStatement ps = null;
            ps = App.DBConnection.prepareStatement("INSERT INTO things VALUES (?, ?, ?, ?)");
            ps.setString(1, thing.getName());
            ps.setString(2, thing.getDescription());
            ps.setInt(3, thing.getStatus());
            ps.setString(4, thing.getOwner());
            ps.execute();

            refreshTable();
            App.writeLog(App.userLogin, "Add request for thing " + thing.getName());
        }
    }

    @FXML
    void quitClick(ActionEvent event) throws Exception {
        App.setRoot("primary");
        App.writeLog(App.userLogin, "Logged out");
    }

    @FXML
    void initialize() throws Exception {
        nameCol.setCellValueFactory(new PropertyValueFactory<Thing, String>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Thing, String>("description"));
        statusCol.setCellValueFactory(new PropertyValueFactory<Thing, String>("stringStatus"));
        refreshTable();  
        username.setText(App.userLogin);
    }

    void refreshTable() throws Exception {
        table.getItems().clear();

        ObservableList<Thing> thing = FXCollections.observableArrayList();
        PreparedStatement ps = null;

        ps = App.DBConnection.prepareStatement("SELECT * FROM things WHERE owner = '" + App.userLogin + "'");
		ps.execute();
		ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            thing.add(new Thing(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4)));
        }

        table.getItems().addAll(thing);
    }
}