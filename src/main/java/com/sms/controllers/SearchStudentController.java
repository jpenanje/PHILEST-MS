package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;

public class SearchStudentController implements Initializable{

    @FXML
    private MenuButton owing;

    @FXML
    private MenuButton registered;

    @FXML
    private MenuButton searchClass;

    @FXML
    private TextField searchId;

    @FXML
    private TextField searchName;

    @FXML
    private TextField searchParent;

    @FXML
    private TextField searchPhone;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        
    }
}
