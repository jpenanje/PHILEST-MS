package com.sms.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.tools.Config;
import com.sms.tools.Tools;

public class LeftMenuController implements Initializable {
    int selectedItemIndex;
    JsonNode menuItems;
    Function<Integer, NullType> onChange;

    LeftMenuController(JsonNode menuItems,Function<Integer, NullType> onChange, int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
        this.onChange = onChange;
        this.menuItems = menuItems;
    }

    @FXML
    private VBox menuItemsPane;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ArrayList<Pane> itemsPanes = new ArrayList<Pane>();
        int counter = 0;
        for (JsonNode menuItem : menuItems) {
            itemsPanes.add(getItemPaneFromJsonNode(menuItem, counter, counter == selectedItemIndex));
            counter++;
        }
        menuItemsPane.getChildren().addAll(itemsPanes);
    }

    Pane getItemPaneFromJsonNode(JsonNode menuItem, int index, boolean selected) {
        Initializable controller;
        if(menuItem.get("title").asText().equals("logout")){
            controller = getLogoutController(menuItem, index, selected);
        }
        else{
            controller = getPageMenuItemController(menuItem, index, selected);
        }
        
        return Tools.getPaneFromControllerAndFxmlPath(controller, "/components/MenuItem.fxml");
    }

    Initializable getLogoutController(JsonNode menuItem, int index, boolean selected){
        Function<Integer, NullType> onMenuItemClick = new Function<Integer, NullType>() {
            @Override
            public NullType apply(Integer newIndex) {
                logout();
                return null;
            }
        };
        return new MenuItemController(menuItem, selected, onMenuItemClick, index);
    }

    Initializable getPageMenuItemController(JsonNode menuItem, int index, boolean selected){
        Function<Integer, NullType> onMenuItemClick = new Function<Integer, NullType>() {
            @Override
            public NullType apply(Integer newIndex) {
                if (newIndex != selectedItemIndex) {
                    unselectCurrentMenuItem();
                    selectCurrentMenuItem(newIndex);
                    onChange.apply(newIndex);
                }
                return null;
            }
        };
        return new MenuItemController(menuItem, selected, onMenuItemClick, index);
    }

    String getNewTitleFromMenuItems(int newIndex){
        JsonNode newMenuItem = menuItems.get(newIndex);
        return newMenuItem.get("title").asText();
    }

    void unselectCurrentMenuItem() {
        menuItemsPane.getChildren().remove(selectedItemIndex);
        JsonNode unselectedMenuItemNode = menuItems.get(selectedItemIndex);
        Pane unselectedMenuItem = getItemPaneFromJsonNode(unselectedMenuItemNode, selectedItemIndex, false);
        menuItemsPane.getChildren().add(selectedItemIndex, unselectedMenuItem);
    }

    void selectCurrentMenuItem(int newIndex) {
        selectedItemIndex = newIndex;
        menuItemsPane.getChildren().remove(selectedItemIndex);
        JsonNode selectedMenuItemNode = menuItems.get(selectedItemIndex);
        Pane selectedMenuItem = getItemPaneFromJsonNode(selectedMenuItemNode, selectedItemIndex, true);
        menuItemsPane.getChildren().add(selectedItemIndex, selectedMenuItem);
    }

    void logout(){
        removeToken();
        clearCurrentUser();
        Stage stage = new Stage();
        Tools.openStage(new LoginController(), "/pages/Login.fxml", stage, false);
        Tools.closeStageFromNode(menuItemsPane);
    }

    void removeToken(){
        Config.token = null;
    }

    void clearCurrentUser(){
        Config.currentUserFullName = null;
        Config.currentUserName = null;
        Config.currentUserPhone = null;
        Config.currentUserPic = null;
    }
}
