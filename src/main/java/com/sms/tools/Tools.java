package com.sms.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.controllers.LoadingIconController;
import com.sms.models.Response;

import javafx.concurrent.Service;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * Tools
 */

// Class containing methods reused a lot through out the project
public class Tools {

    // casts a string to a json object and returns it
    public static JsonNode getJsonNodeFromString(String str) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(str);
        } catch (Exception e) {
            System.out.println("Could not convert " + str + " items to json");
            e.printStackTrace();
            return null;
        }
    }

    // returns a pane from a controller and the fxml path to its view
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

    // returns a pane from the json node of that section
    public static Pane getPaneFromLeftMenuNode(JsonNode node) {
        // Initializable controller = getControllerFromNode(node);
        String viewPath = node.get("view").asText();
        String controllerPath = node.get("controller").asText();
        Class[] classArgs = { JsonNode.class };
        Initializable controller = Tools.getControllerFromPath(controllerPath, classArgs,
                node);
        return getPaneFromControllerAndFxmlPath(controller, viewPath);
    }

    // returns a pane from the path to the controller and the path to the view
    public static Pane getPaneFromPathsOfControllerAndView(String controllerPath, String viewPath) {
        Initializable controller = getControllerFromPath(controllerPath, null);
        return getPaneFromControllerAndFxmlPath(controller, viewPath);
    }

    // returns a controller from its path
    public static Initializable getControllerFromPath(String controllerPath, Class[] objectArgs, Object... args) {
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

    // returns a controller from the section node
    static Initializable getControllerFromNode(JsonNode node) {
        return getControllerFromPath(node.get("controller").asText(), null);
    }

    // converts a list of objects to an array list of Strings
    public static ArrayList<String> listToArrayList(List list) {
        ArrayList<String> toBeReturned = new ArrayList<String>();
        for (Object item : list) {
            toBeReturned.add((String) item);
        }
        return toBeReturned;
    }

    // adds commas after every 3 digits
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

    // returns a percentage from a value and its max
    public static double getPercentage(double value, double max) {
        return (value / max) * 100;
    }

    // returns a color corresponding to a given percentage value
    public static String getColorFromPercentage(int percentage) {
        if (percentage < 10) {
            return "#FF0000";
        } else if (percentage < 30) {
            return "#FF4141";
        } else if (percentage < 50) {
            return "#FF8484";
        } else if (percentage < 65) {
            return "#FFBBBB";
        } else if (percentage < 80) {
            return "#7CFF7C";
        } else if (percentage < 90) {
            return "#6CD86C";
        } else {
            return "#5CB25C";
        }
    }

    // show a modal from the primary stage and the controller and fxml path of the
    // pane
    public static void showModal(Initializable controller, String fxmlPath, Stage primaryStage) {

        System.out.println("show modal");

        // create a new modal stage
        Stage modalStage = new Stage();
        addIconToStage(modalStage);

        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(primaryStage);

        // create a pane with some content
        // ViewProfilePaneController viewProfilePaneController = new
        // ViewProfilePaneController();
        Pane modalPane = Tools.getPaneFromControllerAndFxmlPath(controller,
                fxmlPath);
        Scene modalScene = new Scene(modalPane);
        // viewProfilePaneController.setStage(modalStage);

        // set the scene and show the stage
        modalStage.setScene(modalScene);
        modalStage.setResizable(false);
        modalStage.showAndWait();
    }

    // closes a stage from a node
    public static void closeStageFromNode(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    // returns a loading icon basdd on a service
    public static Node getLoadingIcon(Service service) {
        return getPaneFromControllerAndFxmlPath(new LoadingIconController(service), "/components/LoadingIcon.fxml");
    }

    // returns the stage on which a node is
    public static Stage getStageFromNode(Node node) {
        return (Stage) node.getScene().getWindow();
    }

    // validates a field and highlights the error message
    // when the field is empty
    public static boolean simpleValidation(TextInputControl field, Text errorText) {
        errorText.setText("This field is required");
        if (!field.getText().isEmpty()) {
            errorText.setVisible(false);
            return true;
        } else {
            errorText.setVisible(true);
            return false;
        }
    }

    // overloads the previous method for datepicker fields
    public static boolean simpleValidation(DatePicker field, Text errorText) {
        errorText.setText("This field is required");
        if (field.getValue() != null) {

            errorText.setVisible(false);
            return true;
        } else {
            errorText.setVisible(true);
            return false;
        }
    }

    // overloads the previous method for button base field(like combo box)
    public static boolean simpleValidation(ButtonBase field, Text errorText) {
        errorText.setText("This field is required");
        if (!field.getText().isEmpty()) {
            errorText.setVisible(false);
            return true;
        } else {
            errorText.setVisible(true);
            return false;
        }
    }

    // validates the selected item is not the default drop down value
    // and field is not empty
    public static boolean dropDownValidation(ButtonBase field, String defaultValue, Text errorText) {
        return simpleValidation(field, errorText) && defaultDropDownValidation(field, defaultValue, errorText);
    }

    // validates the selected item is not the default drop down value
    public static boolean defaultDropDownValidation(ButtonBase field, String defaultValue, Text errorText) {
        errorText.setText("Please select a value for this field");
        if (field.getText().equals(defaultValue)) {
            errorText.setVisible(true);
            return false;
        } else {
            errorText.setVisible(false);
            return true;
        }
    }

    // validates digits are entered to the field and it
    // that the field is not empty
    public static boolean digitValidation(TextInputControl field, Text errorText) {
        try {
            Integer.parseInt(field.getText());
            errorText.setVisible(false);
            return simpleValidation(field, errorText);
        } catch (Exception e) {
            errorText.setText("Please fill only digits");
            errorText.setVisible(true);
            return false;
        }
    }

    // validates the passwords are not the same
    public static boolean passwordValidation(TextInputControl passwordField1, TextInputControl passwordTextField1,
            Text errorText1,
            TextInputControl passwordField2, TextInputControl passwordTextField2, Text errorText2) {
        String errorMessage = "The old and new passwords are the same";
        errorText1.setText(errorMessage);
        errorText2.setText(errorMessage);
        String password1 = "";
        String password2 = "";
        if (passwordField1.isVisible()) {
            password1 = passwordField1.getText();
        } else {
            password1 = passwordTextField1.getText();
        }

        if (passwordField2.isVisible()) {
            password2 = passwordField2.getText();
        } else {
            password2 = passwordTextField2.getText();
        }
        try {
            if (password1.equals(password2) && password1.length() > 0) {
                errorText1.setVisible(true);
                errorText2.setVisible(true);
                return false;
            } else {
                errorText1.setVisible(false);
                errorText2.setVisible(false);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // casts an HttpResponse to the custom class Response
    public static Response getCustomResponseFromResponse(HttpResponse<String> response) {
        Response customResponse = new Response();
        customResponse.setCode(response.statusCode());
        customResponse.setData(getJsonNodeFromString(response.body()));
        return customResponse;
    }

    // adds the drop down items to be displayed in a drop down field
    public static void addDropDownItemsFromFieldAndItems(MenuButton field, ArrayList<String> itemsStr) {
        if (itemsStr != null) {
            ArrayList menuItems = getMenuItems(itemsStr, field);
            field.getItems().addAll(menuItems);
        }
    }

    // returns a la list of menuItem objects from a list of string
    static ArrayList<MenuItem> getMenuItems(ArrayList<String> itemsStr, MenuButton field) {
        ArrayList<MenuItem> items = new ArrayList<>();
        for (String itemStr : itemsStr) {
            items.add(getMenuItem(itemStr, field));
        }
        return items;
    }

    // returns a menuItem from a text
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

    // casts local date to utc date string
    public static String getUtcDateStringFromLocalDate(LocalDate localDate) {
        LocalDateTime utcDateTime = LocalDateTime.of(localDate, LocalDateTime.now().toLocalTime())
                .atOffset(ZoneOffset.UTC)
                .toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return utcDateTime.format(formatter);
    }

    // formats a utc string to a date with the format DD/MM/YYYY
    public static String getDateTextFromUtcStr(String utcString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS][.SSSSSSSS][.SSSSSSS][.SSSSSS][.SSSSS][.SSSS][.SSS][.SS][.S]");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Parse the input string into a LocalDateTime object
        LocalDateTime dateTime = LocalDateTime.parse(utcString, inputFormatter);

        // Format the LocalDateTime object into the desired output format
        String formattedDate = dateTime.format(outputFormatter);
        // System.out.println(utcString);
        return formattedDate;
    }

    // returns a StringConverter which can convert a localDate to string
    public static StringConverter<LocalDate> getDateConverter() {
        return new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
    }

    // converts a string date to a localDate object
    public static LocalDate getLocalDateFromString(String dateStr) {
        // Define the formatter for the given date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // Parse the string to LocalDate using the formatter
        return LocalDate.parse(dateStr, formatter);
    }

    // Opens a stage from the controller, fxmlPath and the primary stage
    public static void openStage(Initializable controller, String fxmlPath, Stage primaryStage, boolean fullScreen) {
        try {

            FXMLLoader loader = new FXMLLoader(Tools.class.getResource(fxmlPath));
            // BaseController controller = new BaseController(primaryStage);
            loader.setController(controller);
            Pane mainPage = loader.load();
            Scene sc = new Scene(mainPage);
            if (fullScreen) {
                primaryStage.setMaximized(true);
            }
            addIconToStage(primaryStage);
            primaryStage.setScene(sc);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Could not open stage with view at: " + fxmlPath);
            e.printStackTrace();
        }
    }

    // shows an error message for wrong credentials
    public static void showWrongCredentials(Text errorText) {
        errorText.setText("wrong credentials");
        errorText.setVisible(true);
    }

    // converts an image to a base64 string
    public static String imageToBase64(Image image) {
        String base64Image = "";
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            base64Image = Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Image;
    }

    // returns the kilo byte size of the image
    // not precise at all
    public static int getKBImageSize(Image image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] imageBytes = outputStream.toByteArray();

        return imageBytes.length / (1024 * 10);
    }

    // returns an image from a base64 string
    public static Image getImageFromBase64(String base64Image) {

        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

        return new Image(inputStream);
    }

    // checks if password fields are empty
    public static boolean isEmptyPasswordField(TextInputControl field1, TextInputControl field2) {
        return isEmptyString(field1.getText()) && isEmptyString(field2.getText());
    }

    // checks if a string is null or empty
    static boolean isEmptyString(String toBeChecked) {
        return toBeChecked.equals(null) || toBeChecked.isEmpty();
    }

    // adds the logo to a stage
    public static void addIconToStage(Stage stage) {
        Image icon = new Image(Tools.class.getResourceAsStream("/images/logo.png"));
        stage.getIcons().add(icon);
    }

}