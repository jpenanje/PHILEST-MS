package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.repositories.Repository;
import com.sms.tools.Tools;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

// Controller of the section of the app showing a table
public class MainPaneController implements Initializable {

    int currentItemIndex;

    // contructor with title and drawer index of current main pane
    public MainPaneController(int currentItemIndex, String newTitle) {
        super();
        this.currentItemIndex = currentItemIndex;
        this.newTitle = newTitle;
    }

    @FXML
    private StackPane MainPaneChild;

    private String newTitle;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        JsonNode currentNode = getCurrentNodeFromIndex();
        MainPaneChild.getChildren().add(getCurrentPane(currentNode));
    }

    // returns the current table from json node
    Pane getCurrentPane(JsonNode currentNode) {
        return Tools.getPaneFromLeftMenuNode(currentNode);
    }

    // returns the node of the current item from its index
    JsonNode getCurrentNodeFromIndex() {
        int counter = 0;
        for (JsonNode node : Repository.getMenuItems()) {
            if (counter == currentItemIndex) {
                return node;
            }
            counter++;
        }
        return null;
    }
}
