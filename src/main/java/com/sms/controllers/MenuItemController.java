package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

// controller of a drawer menu item
public class MenuItemController implements Initializable {

    JsonNode menuItem;
    boolean selected;
    Function<Integer, NullType> onClick;
    int index;
    
    public MenuItemController(JsonNode menuItem, boolean selected, Function<Integer, NullType> onClick, int index) {
        this.menuItem = menuItem;
        this.selected = selected;
        this.onClick = onClick;
        this.index = index;
    }

    @FXML
    private ImageView image;

    @FXML
    void onMouseClicked(MouseEvent event) {
        onClick.apply(index);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        URL url;

        if (selected) {
            url = getClass().getResource(menuItem.get("selectedImgPath").asText());
        } else {
            url = getClass().getResource(menuItem.get("unselectedImgPath").asText());
        }

        Image imageData = new Image(url.toExternalForm());
        image.setImage(imageData);
    }
}
