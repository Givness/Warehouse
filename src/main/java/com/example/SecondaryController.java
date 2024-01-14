package com.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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
    void tableClick(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            System.out.println("qq");
            table.getSelectionModel().getSelectedItem();
        }
    }

    @FXML
    void addClick(ActionEvent event) throws Exception {
        Dialog<Thing> addDialog = new AddDialog(new Thing("", "", 0));
        Optional<Thing> result = addDialog.showAndWait();
        if (result.isPresent()) {
            Thing thing = result.get();

            PreparedStatement ps = null;
            ps = App.DBConnection.prepareStatement("INSERT INTO things VALUES (?, ?, ?, ?)");
            ps.setString(1, thing.getName());
            ps.setString(2, thing.getDescription());
            ps.setInt(3, thing.getStatus());
            ps.setString(4, App.userLogin);
            ps.execute();

            refreshTable();
        }
    }

    @FXML
    void quitClick(ActionEvent event) throws Exception {
        App.setRoot("primary");
    }

    @FXML
    void initialize() throws Exception {
        nameCol.setCellValueFactory(new PropertyValueFactory<Thing, String>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Thing, String>("description"));
        statusCol.setCellValueFactory(new PropertyValueFactory<Thing, String>("stringStatus"));
        refreshTable();   
    }

    void refreshTable() throws Exception {
        table.getItems().clear();

        ObservableList<Thing> thing = FXCollections.observableArrayList();
        PreparedStatement ps = null;

        ps = App.DBConnection.prepareStatement("SELECT * FROM things WHERE owner = '" + App.userLogin + "'");
		ps.execute();
		ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            thing.add(new Thing(rs.getString(1), rs.getString(2), rs.getInt(3)));
        }

        table.getItems().addAll(thing);
    }
}