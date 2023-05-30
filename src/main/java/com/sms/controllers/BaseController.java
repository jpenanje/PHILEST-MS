package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.repositories.Repository;
import com.sms.tools.Config;
import com.sms.tools.Tools;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

// The base controller loads the different sections that form the whole application.
// It has a set of panes in it and calls appropriate controllers and fxml documents to
// fill them.
public class BaseController implements Initializable {

    @FXML
    private StackPane mainPane;

    @FXML
    private StackPane menuPane;

    @FXML
    private StackPane topBarPane;

    @FXML
    private StackPane basePane;

    private Function<Integer, NullType> onChange;

    private Function<Initializable, NullType> showProfileModal;

    private int currentItemIndex = 0;

    private Stage primaryStage;

    private JsonNode menuItems = Repository.getMenuItems();

    // Costructor
    public BaseController(Stage primaryStage) {
        super();
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        loadLeftMenu();
        loadHeader();
        loadMainPane("Dashboard");
    }

    // loads the left drawer with appropriate actions for the menu buttons.
    void loadLeftMenu() {
        this.onChange = new Function<Integer, NullType>() {
            @Override
            public NullType apply(Integer index) {
                currentItemIndex = index;
                loadMainPane(getTitleFromIndex(index));
                System.out.println("Changed pane to " + index);
                return null;
            }
        };
        Pane menuPaneChild = Tools.getPaneFromControllerAndFxmlPath(new LeftMenuController(menuItems, onChange, 0),
                "/sections/LeftMenu.fxml");
        menuPaneChild.setStyle("-fx-background-color: " + Config.primaryColor + ";");
        menuPane.getChildren().add(menuPaneChild);
    }

    // returns a title corresponding to the left menu item.
    String getTitleFromIndex(int index) {
        JsonNode newMenuItem = menuItems.get(index);
        return newMenuItem.get("title").asText();
    }

    // loads the header item with appropriate actions of it's components
    void loadHeader() {
        this.showProfileModal = new Function<Initializable, NullType>() {
            @Override
            public NullType apply(Initializable controller) {
                blurBasePane();
                showProfileModal(controller);
                return null;
            }
        };

        Pane headerPane = Tools.getPaneFromControllerAndFxmlPath(new HeaderController(showProfileModal),
                "/sections/Header.fxml");
        headerPane.setStyle("-fx-background-color: " + Config.whiteColor + ";");
        topBarPane.getChildren().add(headerPane);
    }

    // Loads the central pane
    void loadMainPane(String title) {
        Pane mainPane = Tools.getPaneFromControllerAndFxmlPath(new MainPaneController(currentItemIndex, title),
                "/sections/MainPane.fxml");
        this.mainPane.getChildren().clear();
        this.mainPane.getChildren().add(mainPane);
    }

    // blurs the Base pane when showing a popup
    void blurBasePane() {
        // non functional
    }

    // a function for showing the profile popup
    void showProfileModal(Initializable profileSectionController) {
        // create a new modal stage
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(primaryStage);

        // create a pane with some content
        ViewProfilePaneController viewProfilePaneController = new ViewProfilePaneController(profileSectionController);
        Pane modalPane = Tools.getPaneFromControllerAndFxmlPath(viewProfilePaneController,
                "/pages/ViewProfilePane.fxml");
        Scene modalScene = new Scene(modalPane);
        viewProfilePaneController.setStage(modalStage);

        // set the scene and show the stage
        modalStage.setScene(modalScene);
        modalStage.setResizable(false);
        Tools.addIconToStage(modalStage);
        modalStage.showAndWait();
    }

}
