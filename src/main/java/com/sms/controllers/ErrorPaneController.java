package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ErrorPaneController implements Initializable{

    Function refresh;

    public ErrorPaneController(Function refresh) {
        super();
        this.refresh = refresh;
    }

    @FXML
    void refresh(ActionEvent event) {
        refresh.apply(null);
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }
}
