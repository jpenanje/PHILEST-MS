package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.sms.tools.Tools;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DeleteValidationController implements Initializable {

    Stage stage;

    
    public DeleteValidationController(Stage stage) {
        super();
        this.stage = stage;
    }

    @FXML
    StackPane basePane;

    @FXML
    void cancel(ActionEvent event) {
        Tools.closeStageFromNode(basePane);
    }

    @FXML
    void delete(ActionEvent event) {
        Tools.closeStageFromNode(basePane);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // bindStageHeight();
    }

    void bindStageHeight() {
        basePane.heightProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                stage.sizeToScene();
                stage.centerOnScreen();
            };
        });
    }
}
