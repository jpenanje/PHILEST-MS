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

public class AddExpenditureController implements Initializable {

    @FXML
    private Button addExpButton;

    @FXML
    private StackPane addExpenditureModal;

    @FXML
    private Button cancelExpButton;

    @FXML
    private DatePicker dateOfExpense;

    @FXML
    private TextField expenseAmount;

    @FXML
    private SplitMenuButton expenseType;

    @FXML
    private VBox formPane;

    @FXML
    private VBox mainPaneExp;

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

