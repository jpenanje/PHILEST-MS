package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

// controller for the error page displayed when a table fails to load
public class ErrorPaneController implements Initializable{

    Function refresh;

    // constructor for an error page with a function to refresh the page.
    public ErrorPaneController(Function refresh) {
        super();
        this.refresh = refresh;
    }

    // refreshes the section on which the error page was displayed
    @FXML
    void refresh(ActionEvent event) {
        refresh.apply(null);
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }
}
