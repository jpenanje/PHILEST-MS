package com.sms.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/**
 * Tools
 */
public class Tools {
    public static JsonNode getJsonNodeFromString(String str) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(str);
        } catch (Exception e) {
            System.out.println("Could not convert menu items to json");
            e.printStackTrace();
            return null;
        }
    }

    public static Pane getPaneFromControllerAndFxmlPath(Initializable controller, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Tools.class.getResource(fxmlPath));
            loader.setController(controller);
            Pane pane = loader.load();
            return pane;
        } catch (Exception e) {
            System.out.println("Could not load the file at " + fxmlPath);
            e.printStackTrace();
            return new Pane();
        }

    }

    public static Font getRegularFont(int fontSize){
        return Font.loadFont(Tools.class.getResource("/fonts/lato/Lato-Regular.ttf").getPath(), fontSize);
    }
}