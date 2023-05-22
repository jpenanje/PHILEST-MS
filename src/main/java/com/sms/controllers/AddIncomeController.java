package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AddIncomeController implements Initializable {

    @FXML
    private Button addIncButton;

    @FXML
    private StackPane addIncomeModal;

    @FXML
    private Button cancelIncButton;

    @FXML
    private DatePicker dateOfIncome;

    @FXML
    private VBox formPane;

    @FXML
    private TextField incomeAmount;

    @FXML
    private SplitMenuButton incomeType;

    @FXML
    private VBox mainPaneInc;

    @FXML
    private VBox previousPassword1;

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }

    
}

