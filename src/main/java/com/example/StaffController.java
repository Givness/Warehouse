package com.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class StaffController {

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
    private TableColumn<Thing, String> ownerCol;

    @FXML
    void confirmClick(ActionEvent event) throws Exception {
        Thing thing = table.getSelectionModel().getSelectedItem();
        if (thing == null) return;
        PreparedStatement ps = null;
        switch (thing.getStatus()) {
            case 0:   
                ps = App.DBConnection.prepareStatement("UPDATE things SET status = 1 WHERE name ='" + thing.getName() + "'");
                break;
        
            case 1:   
                ps = App.DBConnection.prepareStatement("UPDATE things SET status = 2 WHERE name ='" + thing.getName() + "'");
                break;

            case 3:   
                ps = App.DBConnection.prepareStatement("UPDATE things SET status = 4 WHERE name ='" + thing.getName() + "'");
                break;

            case 4:   
                ps = App.DBConnection.prepareStatement("DELETE FROM things WHERE name ='" + thing.getName() + "'");
                break;

            default:
                return;
        }
		ps.execute();
        refreshTable();
    }

    @FXML
    void quitClick(ActionEvent event) throws Exception {
        App.setRoot("primary");
    }

    @FXML
    void tableClick(MouseEvent event) {

    }

    @FXML
    void initialize() throws Exception {
        nameCol.setCellValueFactory(new PropertyValueFactory<Thing, String>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Thing, String>("description"));
        statusCol.setCellValueFactory(new PropertyValueFactory<Thing, String>("stringStatus"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<Thing, String>("owner"));
        refreshTable();   
    }

    void refreshTable() throws Exception {
        table.getItems().clear();

        ObservableList<Thing> thing = FXCollections.observableArrayList();
        PreparedStatement ps = null;
        ps = App.DBConnection.prepareStatement("SELECT * FROM things WHERE status != 2 AND status != 5");
		ps.execute();
		ResultSet rs = ps.getResultSet();
        while (rs.next()) {
            thing.add(new Thing(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4)));
        }

        table.getItems().addAll(thing);
    }
}
