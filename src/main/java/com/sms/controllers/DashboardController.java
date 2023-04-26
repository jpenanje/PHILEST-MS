package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.text.Text;

public class DashboardController implements Initializable {

    @FXML
    private Text classes;

    @FXML
    private Text netIncome;

    @FXML
    private Text students;

    @FXML
    private Text subjects;

    @FXML
    private Text teachers;

    @FXML
    private MenuButton year;

    @FXML
    void changeYear(ActionEvent event) {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub

    }
}
