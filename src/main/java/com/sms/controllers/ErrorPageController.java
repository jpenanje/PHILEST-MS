package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.sms.tools.Tools;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class ErrorPageController implements Initializable {

    String errorMessage;

    public ErrorPageController(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

    @FXML
    private Button okButton;

    @FXML
    private Text message;

    @FXML
    void validate(ActionEvent event) {
        Tools.closeStageFromNode(okButton);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        message.setText(errorMessage);
    }
}
