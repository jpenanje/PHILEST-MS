package com.sms.business;

import com.sms.controllers.BaseController;
import com.sms.controllers.LoginController;
import com.sms.tools.Tools;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application {

    // Starts the application
    @Override
    public void start(Stage primaryStage) {
        try {
            LoginController loginController = new LoginController();
            Tools.showModal(loginController, "/pages/Login.fxml", primaryStage);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
