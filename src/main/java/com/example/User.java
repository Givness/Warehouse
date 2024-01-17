package com.example;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {
    private SimpleStringProperty name;
    private SimpleStringProperty password;
    private SimpleIntegerProperty type;

    User(String name, String password, int type){
        this.name = new SimpleStringProperty(name);
        this.password = new SimpleStringProperty(password);
        this.type = new SimpleIntegerProperty(type);
    }

    public SimpleStringProperty nameProperty() { return name;}
    public SimpleStringProperty passwordProperty() { return password;}
    public SimpleIntegerProperty typeProperty() { return type;}

    public String getName(){ return name.get();}
    public void setName(String value){ name.set(value);}    

    public String getPassword(){ return password.get();}
    public void setPassword(String value){ password.set(value);}
     
    public int getType(){ return type.get();}
    public void setType(int value){ type.set(value);}

}
