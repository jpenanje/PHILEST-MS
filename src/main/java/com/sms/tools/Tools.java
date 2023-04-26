package com.sms.tools;

import java.lang.reflect.Constructor;

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

    public static Font getRegularFont(int fontSize) {
        return Font.loadFont(Tools.class.getResource("/fonts/lato/Lato-Regular.ttf").getPath(), fontSize);
    }

    public static Pane getPaneFromLeftMenuNode(JsonNode node) {
        Initializable controller = getControllerFromNode(node);
        return getPaneFromControllerAndFxmlPath(controller, node.get("view").asText());
    }

    static Initializable getControllerFromNode(JsonNode node){
        try {
            String className = node.get("controller").asText();
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object obj = constructor.newInstance();
            Initializable controller = (Initializable)obj;

            return controller;
        } catch (Exception e) {
            System.out.println("Could not load controller "+ node.get("controller").asText());
            e.printStackTrace();
            return null;
        }
    }
}