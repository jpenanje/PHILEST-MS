package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.sms.tools.Tools;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ErrorInSavingPageController implements Initializable{

    @FXML
    private Button okButton;

    @FXML
    void validate(ActionEvent event) {
        Tools.closeStageFromNode(okButton);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }
}
