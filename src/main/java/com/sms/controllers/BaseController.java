package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import javax.lang.model.type.NullType;

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

    private Function<NullType, NullType> showProfileModal;

    private int currentItemIndex = 0;

    private Stage primaryStage;

    public BaseController(Stage primaryStage) {
        super();
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        loadLeftMenu();
        loadHeader();
        loadMainPane();
    }

    void loadLeftMenu() {
        this.onChange = new Function<Integer, NullType>() {
            @Override
            public NullType apply(Integer index) {
                currentItemIndex = index;
                loadMainPane();
                System.out.println("Changed pane to " + index);
                return null;
            }
        };
        Pane menuPaneChild = Tools.getPaneFromControllerAndFxmlPath(new LeftMenuController(onChange, 0),
                "/sections/LeftMenu.fxml");
        menuPaneChild.setStyle("-fx-background-color: " + Config.primaryColor + ";");
        menuPane.getChildren().add(menuPaneChild);
    }

    void loadHeader() {
        this.showProfileModal = new Function<NullType, NullType>() {
            @Override
            public NullType apply(NullType item) {
                blurBasePane();
                showProfileModal();
                return null;
            }
        };

        Pane headerPane = Tools.getPaneFromControllerAndFxmlPath(new HeaderController(showProfileModal),
                "/sections/Header.fxml");
        headerPane.setStyle("-fx-background-color: " + Config.whiteColor + ";");
        topBarPane.getChildren().add(headerPane);
    }

    void loadMainPane() {

        Pane mainPane = Tools.getPaneFromControllerAndFxmlPath(new MainPaneController(currentItemIndex),
                "/sections/MainPane.fxml");
        // mainPane.setStyle("-fx-background-color: " + Config.whiteColor + ";");
        this.mainPane.getChildren().add(mainPane);
    }


    void blurBasePane(){

    }

    void showProfileModal(){
        // create a new modal stage
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(primaryStage);

        // create a pane with some content
        ViewProfilePaneController viewProfilePaneController = new ViewProfilePaneController();
        Pane modalPane = Tools.getPaneFromControllerAndFxmlPath(viewProfilePaneController, "/pages/ViewProfilePane.fxml");
        Scene modalScene = new Scene(modalPane);
        viewProfilePaneController.setStage(modalStage);

        // set the scene and show the stage
        modalStage.setScene(modalScene);
        modalStage.setResizable(false);
        modalStage.showAndWait();
    }

}
