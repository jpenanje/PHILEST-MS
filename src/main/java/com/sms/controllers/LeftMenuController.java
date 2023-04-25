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

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.repositories.Repository;
import com.sms.tools.Tools;

public class LeftMenuController implements Initializable {
    int selectedItemIndex;
    JsonNode menuItems;
    Function<Integer, NullType> onChange;

    LeftMenuController(Function<Integer, NullType> onChange, int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
        this.onChange = onChange;
    }

    @FXML
    private VBox menuItemsPane;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ArrayList<Pane> itemsPanes = new ArrayList<Pane>();
        menuItems = Repository.getMenuItems();
        int counter = 0;
        for (JsonNode menuItem : menuItems) {
            itemsPanes.add(getItemPaneFromJsonNode(menuItem, counter, counter == selectedItemIndex));
            counter++;
        }
        menuItemsPane.getChildren().addAll(itemsPanes);
    }

    Pane getItemPaneFromJsonNode(JsonNode menuItem, int index, boolean selected) {
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
        Initializable controller = new MenuItemController(menuItem, selected, onMenuItemClick, index);
        return Tools.getPaneFromControllerAndFxmlPath(controller, "/components/MenuItem.fxml");
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
}
