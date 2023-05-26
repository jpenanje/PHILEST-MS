package com.sms.controllers;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.models.Response;
import com.sms.tools.Config;
import com.sms.tools.RequestManager;
import com.sms.tools.Tools;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    private StackPane addItemModal;

    @FXML
    private HBox buttonsPane;

    @FXML
    private VBox formPane;

    @FXML
    private TextField password;

    @FXML
    private Text passwordErrorMessage;

    @FXML
    private Text title;

    @FXML
    private TextField username;

    @FXML
    private Text usernameErrorMessage;

    ObservableList<Node> buttons;

    private Pane loadingIcon;

    boolean loggedIn;

    boolean invalidCredentials;

    @FXML
    void login(ActionEvent event) {
        boolean isValidForm = validateForm();
        if (isValidForm) {
            showLoadingIcon();
            Service loginService = getLoginService();
            bindLoginServiceWithDisplay(loginService);
            loginService.start();
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        loadingIcon = getLoadingIcon();
    }

    void showLoadingIcon() {
        System.out.println("Show student form loading icon");
        buttons = FXCollections.observableArrayList(buttonsPane.getChildren());
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().add(loadingIcon);
    }

    boolean validateForm() {
        return (validateUsernameField() &
                validatePasswordField());
    }

    boolean validateUsernameField() {
        return Tools.simpleValidation(username, usernameErrorMessage);
    }

    boolean validatePasswordField() {
        return Tools.simpleValidation(password, passwordErrorMessage);
    }

    Pane getLoadingIcon() {
        return Tools.getPaneFromControllerAndFxmlPath(null, "/components/CircleLoadingIcon.fxml");
    }

    Service getLoginService() {
        System.out.println("get Save Student Service");
        invalidCredentials = false;
        Service service = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String currentUserRequestBody = getCurrentUserJson();

                                    HttpResponse<String> loginResponse;
                                    loginResponse = login(currentUserRequestBody);
                                    Response customResponse = Tools.getCustomResponseFromResponse(loginResponse);
                                    System.out.println(customResponse.getCode());

                                    if (customResponse.getCode() < 300 && customResponse.getCode() >= 200) {
                                        loggedIn = true;
                                        updateProgress(100, 100);
                                        saveUserInfoFromResponse(customResponse);
                                    }
                                    else if (customResponse.getCode() == 400){
                                        showWrongCredentialsErrorMessage();
                                        invalidCredentials = true;
                                        updateProgress(100, 100);
                                    }

                                    else {
                                        updateProgress(100, 100);
                                    }
                                } catch (Exception e) {
                                    System.out.println("error");
                                    loggedIn = false;
                                    updateProgress(100, 100);
                                }

                            }
                        });
                        try {
                            future.get(Config.saveItemTimeout, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            System.out.println("future error");
                            loggedIn = false;
                            updateProgress(100, 100);
                        }
                        return null;
                    }
                };
            }
        };
        return service;
    }

    String getCurrentUserJson() {
        return "{\"username\":\""+username.getText()+"\",\"password\":\""+password.getText()+"\"}";
    }

    HttpResponse<String> login(String requestBody) throws Exception {
        return RequestManager.postItem("api-token-auth/", requestBody).get();
    }

    void bindLoginServiceWithDisplay(Service service) {
        service.progressProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                if (Math.abs(((Double) newValue - 1.0)) < Math.pow(10, -15)) {
                    if (loggedIn) {
                        System.out.println("logged in");
                        removeLoadingIcon();
                        showSuccessIconForSeconds();
                        // closePane();
                    } else {
                        System.out.println("not logged in");
                        removeLoadingIcon();
                        if(!invalidCredentials){
                            showErrorMessage();
                        }
                        
                    }
                }
            };
        });
    }

    void removeLoadingIcon() {
        System.out.println("Remove loading icon");
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().addAll(buttons);
    }

    void showErrorMessage() {
        System.out.println("show error message");
        Initializable errorInSavingPageController = new ErrorPageController(
                "There was an issue while trying to save this item. Check your internet connection and try again.");
        Tools.showModal(errorInSavingPageController, "/pages/ErrorPage.fxml",
                Tools.getStageFromNode(username));
    }

    void showSuccessIconForSeconds() {
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().add(getSuccessPane());
        waitAndLogin();
    }

    Pane getSuccessPane() {
        return Tools.getPaneFromControllerAndFxmlPath(null, "/components/SuccessPane.fxml");
    }

    void waitAndLogin() {
        Service service = getWaitService();
        bindWaitServiceWithDisplay(service);
        service.start();
    }

    Service getWaitService() {
        Service service = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    try {
                                        Thread.sleep(600);
                                        updateProgress(100, 100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();

                                    }
                                } catch (Exception e) {
                                    System.out.println("error");
                                    loggedIn = false;
                                    updateProgress(100, 100);
                                }

                            }
                        });
                        try {
                            future.get(Config.saveItemTimeout, TimeUnit.SECONDS);
                            loggedIn = true;
                        } catch (Exception e) {
                            System.out.println("future error");
                            loggedIn = false;
                            updateProgress(100, 100);
                        }
                        return null;
                    }
                };
            }
        };
        return service;
    }

    void bindWaitServiceWithDisplay(Service service) {
        service.progressProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                if (Math.abs(((Double) newValue - 1.0)) < Math.pow(10, -15)) {
                    Stage stage = new Stage();
                    Tools.openStage(new BaseController(stage), "/pages/Base.fxml", stage, true);
                    Tools.closeStageFromNode(username);
                }
            };
        });
    }

    void showWrongCredentialsErrorMessage(){
        Tools.showWrongCredentials(usernameErrorMessage);
        Tools.showWrongCredentials(passwordErrorMessage);
    }

    void saveUserInfoFromResponse(Response response){
        JsonNode responseNode = response.getData();
        Config.token = responseNode.get("token").asText();
        Config.currentUserName = responseNode.get("username").asText();
        Config.currentUserFullName = responseNode.get("full_name").asText();
        Config.currentUserPhone = responseNode.get("phone_number").asText();
        Config.currentUserPic = responseNode.get("pic").asText();
    }

}
