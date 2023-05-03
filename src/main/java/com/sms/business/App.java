package com.sms.business;

import com.sms.controllers.BaseController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println(getClass().getResource("/"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/Base.fxml"));
            BaseController controller = new BaseController(primaryStage);
            loader.setController(controller);
            Pane mainPage = loader.load();
            Scene sc = new Scene(mainPage);
            primaryStage.setMaximized(true);
            primaryStage.setScene(sc);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
