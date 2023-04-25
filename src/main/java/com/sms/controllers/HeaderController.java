package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.sms.tools.Tools;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class HeaderController implements Initializable {

    Function<NullType, NullType> showProfileModal;

    public HeaderController(Function<NullType, NullType> showProfileModal) {
        super();
        this.showProfileModal = showProfileModal;
    }

    @FXML
    private StackPane brandPane;

    @FXML
    private StackPane profilePane;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        brandPane.getChildren().add(getBrandPane());
        profilePane.getChildren().add(getProfilePane(showProfileModal));
    }

    Pane getBrandPane() {
        return Tools.getPaneFromControllerAndFxmlPath(new BrandPaneController(), "/components/BrandPane.fxml");
    }

    Pane getProfilePane(Function<NullType, NullType> showProfileModal) {
        Initializable profilePaneController = new ProfilePaneController(showProfileModal);
        return Tools.getPaneFromControllerAndFxmlPath(profilePaneController, "/components/ProfilePane.fxml");
    }
}
