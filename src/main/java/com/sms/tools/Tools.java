package com.sms.tools;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    static Initializable getControllerFromNode(JsonNode node) {
        try {
            String className = node.get("controller").asText();
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object obj = constructor.newInstance();
            Initializable controller = (Initializable) obj;

            return controller;
        } catch (Exception e) {
            System.out.println("Could not load controller " + node.get("controller").asText());
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> listToArrayList(List list) {
        ArrayList<String> toBeReturned = new ArrayList<String>();
        for (Object item : list) {
            toBeReturned.add((String) item);
        }
        return toBeReturned;
    }

    public static String addCommasToStringValue(String value) {
        // Remove any existing commas from the number
        value = value.replaceAll(",", "");

        // Split the number into its integer and fractional parts (if any)
        String[] parts = value.split("\\.");
        String integerPart = parts[0];
        String fractionalPart = parts.length > 1 ? "." + parts[1] : "";

        // Insert commas after every three digits in the integer part of the number
        StringBuilder sb = new StringBuilder();
        int length = integerPart.length();
        for (int i = 0; i < length; i++) {
            if (i > 0 && (length - i) % 3 == 0) {
                sb.append(",");
            }
            sb.append(integerPart.charAt(i));
        }

        // Combine the integer and fractional parts (if any) and return the result
        return sb.toString() + fractionalPart;
    }

    public static double getPercentage(double value, double max){
        return (value/max) * 100;
    }

    public static String getColorFromPercentage(int percentage){
        if(percentage < 10){
            return "#FF0000";
        }
        else if(percentage < 30){
            return "#FF4141";
        }
        else if(percentage < 50){
            return "#FF8484";
        }
        else if(percentage < 65){
            return "#FFBBBB";
        }
        else if(percentage < 80){
            return "#7CFF7C";
        }
        else if(percentage < 90){
            return "#6CD86C";
        }
        else{
            return "#5CB25C";
        }
    }

    public static void showModal(Initializable controller, String fxmlPath, Stage primaryStage){
        System.out.println("Show delete confirmation modal");
        // create a new modal stage
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(primaryStage);

        // create a pane with some content
        // ViewProfilePaneController viewProfilePaneController = new ViewProfilePaneController();
        Pane modalPane = Tools.getPaneFromControllerAndFxmlPath(controller,
                fxmlPath);
        Scene modalScene = new Scene(modalPane);
        // viewProfilePaneController.setStage(modalStage);

        // set the scene and show the stage
        modalStage.setScene(modalScene);
        modalStage.setResizable(false);
        modalStage.showAndWait();
    }

    public static void closeStageFromNode(Node node){
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}