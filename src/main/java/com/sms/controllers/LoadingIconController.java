package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;

// Controller for the loading icon
public class LoadingIconController implements Initializable{

    Service service;
    public LoadingIconController(Service service) {
        super();
        this.service = service;
    }

    @FXML
    private ProgressBar progressBar;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        styleProgressBar();
        progressBar.progressProperty().bind(service.progressProperty());
    }

    // styles the progress bar
    void styleProgressBar(){
        progressBar.setStyle("-fx-box-border: goldenrod;");
    }
}
