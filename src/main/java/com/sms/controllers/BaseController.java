package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.sms.tools.Config;
import com.sms.tools.Tools;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class BaseController implements Initializable {

    @FXML
    private StackPane mainPane;

    @FXML
    private StackPane menuPane;

    @FXML
    private StackPane topBarPane;

    private Function<Integer, NullType> onChange;

    private Function<NullType, NullType> showProfileModal;

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
                System.out.println("Changed pane to " + index);
                return null;
            }
        };
        Pane menuPaneChild = Tools.getPaneFromControllerAndFxmlPath(new LeftMenuController(onChange, 0),
                "/sections/LeftMenu.fxml");
        menuPaneChild.setStyle("-fx-background-color: " + Config.primaryColor + ";");
        menuPane.getChildren().add(menuPaneChild);
    }
    

    void loadHeader(){
        this.showProfileModal = new Function<NullType, NullType>() {
            @Override
            public NullType apply(NullType item) {
                System.out.println("show profile modal");
                return null;
            }
        };

        Pane headerPane = Tools.getPaneFromControllerAndFxmlPath(new HeaderController(showProfileModal),
                "/sections/Header.fxml");
        headerPane.setStyle("-fx-background-color: " + Config.whiteColor + ";");
        topBarPane.getChildren().add(headerPane);
    }


    void loadMainPane(){

        Pane mainPane = Tools.getPaneFromControllerAndFxmlPath(new MainPaneController(),
                "/sections/MainPane.fxml");
        // mainPane.setStyle("-fx-background-color: " + Config.whiteColor + ";");
        this.mainPane.getChildren().add(mainPane);
    }

    
}
