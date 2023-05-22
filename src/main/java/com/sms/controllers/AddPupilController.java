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
import javafx.scene.text.Text;

public class AddPupilController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private StackPane addStudentModal;

    @FXML
    private Button cancelButton;

    @FXML
    private DatePicker dateAdded;

    @FXML
    private TextField fees;

    @FXML
    private VBox formPane;

    @FXML
    private Text fullNameErrorMessage;

    @FXML
    private Text fullNameErrorMessage11;

    @FXML
    private VBox mainPane;

    @FXML
    private TextField parentEmail;

    @FXML
    private TextField parentName;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Text phoneNumberErrorMessage;

    @FXML
    private VBox previousPassword;

    @FXML
    private VBox previousPassword1;

    @FXML
    private Text previousPasswordErrorMessage;

    @FXML
    private Text previousPasswordErrorMessage1;

    @FXML
    private TextField previousPasswordTextField;

    @FXML
    private TextField previousPasswordTextField1;

    @FXML
    private SplitMenuButton pupilClass;

    @FXML
    private TextField pupilName;

    @FXML
    private Text usernameErrorMessage;

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

