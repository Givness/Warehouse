package com.example;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Thing {

    private SimpleStringProperty name;
    private SimpleStringProperty description;
    private SimpleIntegerProperty status;

    Thing(String name, String description, int status){
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.status = new SimpleIntegerProperty(status);
    }
     
    public SimpleStringProperty nameProperty() { return name;}
    public SimpleStringProperty descriptionProperty() { return description;}

    public String getName(){ return name.get();}
    public void setName(String value){ name.set(value);}    

    public String getDescription(){ return description.get();}
    public void setDescription(String value){ description.set(value);}
     
    public int getStatus(){ return status.get();}
    public void setStatus(int value){ status.set(value);}

    public String getStringStatus() { return intToStatusString(status.get()); }

    public static String intToStatusString(int status) {

        switch (status) {
            case 0:
                return "Ожидает подтверждения приёма";
        
            case 1:
                return "Готово к прёму на склад";
            
            case 2:
                return "Лежит на складе";
                
            case 3:
                return "Ожидает подтверждения вывоза";

            case 4:
                return "Готово к вывозу со склада";

            case 5:
                return "Заявка на приём отклонена";

            default:
                return "N/A";
        }

    }
}
