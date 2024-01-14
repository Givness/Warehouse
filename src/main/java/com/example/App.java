package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class App extends Application {

    private static Stage stage;

    public static Scene scene;

    public static Connection DBConnection;

    public static String userLogin;

    public static int titleHeight;

    @Override
    public void start(Stage stage) throws Exception {
        App.stage = stage;
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();        
        titleHeight = (int)(Stage.getWindows().get(0).getHeight() - scene.getHeight());
    }

    static void setRoot(String fxml) throws Exception {
        Parent p = loadFXML(fxml);
        scene.setRoot(p);
        stage.setWidth(p.prefWidth(-1));
        stage.setHeight(p.prefHeight(-1) + titleHeight);
        Stage.getWindows().get(0).centerOnScreen();
    }

    static void setResolution(int width, int height) {
        stage.setWidth(width);
        stage.setHeight(height);
    }

    private static Parent loadFXML(String fxml) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        String filename = "dbinfo.txt";
        String url = "";
        String username = "";
        String password = "";

        try(Scanner input = new Scanner(new File(filename))) {
        	url = input.next();
            username = input.next();
            password = input.next();
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }

        DBConnection = null;
		try{
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            DBConnection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection to Store DB succesfull!");
        }
        catch(Exception ex){
            System.out.println("Connection to Store DB failed...");
            System.out.println(ex);
        }
        launch(args);
    }

}