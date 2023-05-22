package com.sms.tools;

import java.lang.reflect.Constructor;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.controllers.LoadingIconController;
import com.sms.models.Response;

import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
            System.out.println("Could not convert "+str+" items to json");
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
        // Initializable controller = getControllerFromNode(node);
        String viewPath = node.get("view").asText();
        String controllerPath = node.get("controller").asText();
        Class[] classArgs = { JsonNode.class };
        Initializable controller = Tools.getControllerFromPath(controllerPath, classArgs,
                node);
        return getPaneFromControllerAndFxmlPath(controller, viewPath);
    }

    public static Pane getPaneFromPathsOfControllerAndView(String controllerPath, String viewPath){
        Initializable controller = getControllerFromPath(controllerPath, null);
        return getPaneFromControllerAndFxmlPath(controller, viewPath);
    }

    public static Initializable getControllerFromPath(String controllerPath, Class[] objectArgs, Object... args){
        try {
            String className = controllerPath;
            Class<?> clazz = Class.forName(className);

            Constructor<?> constructor = clazz.getDeclaredConstructor(objectArgs);
            Object obj = constructor.newInstance(args);
            Initializable controller = (Initializable) obj;

            return controller;

        } catch (Exception e) {
            System.out.println("Could not load controller " + controllerPath);
            e.printStackTrace();
            return null;
        }
    }

    static Initializable getControllerFromNode(JsonNode node) {
        return getControllerFromPath(node.get("controller").asText(), null);
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

    public static Node getLoadingIcon(Service service){
        return getPaneFromControllerAndFxmlPath(new LoadingIconController(service), "/components/LoadingIcon.fxml");
    }

    public static Stage getStageFromNode(Node node){
        return (Stage)node.getScene().getWindow();
    }

    public static boolean simpleValidation(TextInputControl field, Text errorText){
        errorText.setText("This field is required");
        if(!field.getText().isEmpty()){
            errorText.setVisible(false);
            return true;
        }
        else{
            errorText.setVisible(true);
            return false;
        }
    }

    public static boolean simpleValidation(ButtonBase field, Text errorText){
        errorText.setText("This field is required");
        if(!field.getText().isEmpty()){
            errorText.setVisible(false);
            return true;
        }
        else{
            errorText.setVisible(true);
            return false;
        }
    }

    public static boolean dropDownValidation(ButtonBase field, String defaultValue, Text errorText){
        return simpleValidation(field, errorText) && defaultDropDownValidation(field, defaultValue, errorText);
    }

    public static boolean defaultDropDownValidation(ButtonBase field, String defaultValue, Text errorText){
        errorText.setText("Please select a value for this field");
        if(field.getText().equals(defaultValue) ){
            errorText.setVisible(true);
            return false;
        }
        else{
            errorText.setVisible(false);
            return true;
        }
    }

    public static boolean digitValidation(TextInputControl field, Text errorText){
        try {
            Integer.parseInt(field.getText());
            errorText.setVisible(false);
            return true;
        } catch (Exception e) {
            errorText.setText("Please fill only digits");
            errorText.setVisible(true);
            return false;
        }
    }

    public static Response getCustomResponseFromResponse(HttpResponse<String> response){
        Response customResponse = new Response();
        customResponse.setCode(response.statusCode());
        customResponse.setData(getJsonNodeFromString(response.body()));
        return customResponse;
    }

    public static void addDropDownItemsFromFieldAndItems(MenuButton field, ArrayList<String> itemsStr){
        if(itemsStr != null){
            ArrayList menuItems = getMenuItems(itemsStr, field);
            field.getItems().addAll(menuItems);
        }
    }

    static ArrayList<MenuItem> getMenuItems(ArrayList<String> itemsStr, MenuButton field){
        ArrayList<MenuItem> items = new ArrayList<>();
        for(String itemStr : itemsStr){
            items.add(getMenuItem(itemStr, field));
        }
        return items;
    }

    static MenuItem getMenuItem(String text, MenuButton field) {
        MenuItem toBeReturned = new MenuItem();
        toBeReturned.setText(text);
        final String finalText = text;
        toBeReturned.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                field.setText(finalText);
            }
        });

        return toBeReturned;
    }
    
}