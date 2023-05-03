package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.repositories.Repository;
import com.sms.tools.Tools;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MainPaneController implements Initializable {

    int currentItemIndex;
    public MainPaneController(int currentItemIndex, String newTitle) {
        super();
        this.currentItemIndex = currentItemIndex;
        this.newTitle = newTitle;
    }

    @FXML
    private StackPane MainPaneChild;

    @FXML
    private Button addButton;

    @FXML
    private Text title;

    private String newTitle;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if(currentItemIndex == 0){
            addButton.setVisible(false);
        }
        MainPaneChild.getChildren().add(getCurrentPaneFromIndex());
        title.setText(newTitle);
    }

    Pane getCurrentPaneFromIndex(){
        JsonNode currentNode = getCurrentNodeFromIndex();
        return Tools.getPaneFromLeftMenuNode(currentNode);
        
    }

    JsonNode getCurrentNodeFromIndex(){
        int counter = 0;
        for(JsonNode node :Repository.getMenuItems()){
            if(counter == currentItemIndex){
                return node;
            }
            counter++;
        }
        return null;
    }
}
