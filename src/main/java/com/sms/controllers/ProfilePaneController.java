package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ProfilePaneController implements Initializable {

    Function<NullType, NullType> showProfileModal;

    public ProfilePaneController(Function<NullType, NullType> showProfileModal) {
        super();
        this.showProfileModal = showProfileModal;
    }

    @FXML
    private ImageView pic;

    @FXML
    private Text role;

    @FXML
    private Text userName;

    @FXML
    void showProfileModal(MouseEvent event) {
        showProfileModal.apply(null);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        role.setText("admin");
        userName.setText("Username");
    }

}

