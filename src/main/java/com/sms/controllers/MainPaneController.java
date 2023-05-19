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

public class MainPaneController implements Initializable {

    int currentItemIndex;

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
        MainPaneChild.getChildren().add(getCurrentPaneFromIndex(currentNode));
    }

    Pane getCurrentPaneFromIndex(JsonNode currentNode) {
        return Tools.getPaneFromLeftMenuNode(currentNode);
    }

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

    // EventHandler<ActionEvent> getAddActionFromCurrentNode(JsonNode currentNode) {
    // final JsonNode node = currentNode;
    // return new EventHandler<ActionEvent>() {
    // @Override
    // public void handle(ActionEvent arg0) {
    // if (currentItemIndex != 0) {
    // String addFormViewPath = node.get("form_view").asText();
    // String addControllerPath = node.get("form_controller").asText();
    // // Class[] classArgs = { Student.class };
    // Initializable addFormController =
    // Tools.getControllerFromPath(addControllerPath, null,
    // null);
    // Tools.showModal(addFormController, addFormViewPath,
    // Tools.getStageFromNode(mainPan));
    // }
    // // TODO Auto-generated method stub
    // }
    // };
    // }
}
