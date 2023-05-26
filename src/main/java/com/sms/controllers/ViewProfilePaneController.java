package com.sms.controllers;

import java.io.File;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ViewProfilePaneController implements Initializable {

    @FXML
    private VBox formPane;

    @FXML
    private VBox newPassword;

    @FXML
    private VBox previousPassword;

    @FXML
    private Button showPasswordBtn;

    @FXML
    private VBox mainPane;

    @FXML
    private ImageView showPasswordIcon;

    @FXML
    private StackPane basePane;

    @FXML
    private Text fullNameErrorMessage;

    @FXML
    private Text newPasswordErrorMessage;

    @FXML
    private Text phoneNumberErrorMessage;

    @FXML
    private Text previousPasswordErrorMessage;

    @FXML
    private Text usernameErrorMessage;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField previousPasswordField;

    @FXML
    private TextField newPasswordTextField;

    @FXML
    private TextField previousPasswordTextField;

    @FXML
    private ImageView profilePic;

    @FXML
    private StackPane profilePicPane;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField fullnameField;

    @FXML
    private HBox buttonsPane;

    private boolean saved;    

    private boolean duplicateUserName;

    private File currentImageFile;

    private Stage stage;

    private Pane loadingIcon;

    private Initializable profileSectionController;

    ObservableList<Node> buttons;

    double viewWidth = 82;
    double viewHeight = 79.5;

    public ViewProfilePaneController(Initializable profileSectionController) {
        super();
        this.profileSectionController = profileSectionController;
    }

    @FXML
    void cancel(ActionEvent event) {
        Tools.closeStageFromNode(basePane);
    }

    @FXML
    void save(ActionEvent event) {
        boolean isValidForm = validateForm();
        if (isValidForm) {
            showLoadingIcon();
            Service saveService = getSaveService();
            bindSaveServiceWithDisplay(saveService);
            saveService.start();
        }
    }

    @FXML
    void selectProfilePic(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        // Set initial directory (optional)
        File initialDirectory = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(initialDirectory);

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            currentImageFile = selectedFile;
            updateCurrentImageFile();
            centerProfilePic();
        }
    }

    @FXML
    void toggleEditPasswordFields(ActionEvent event) {
        ObservableList<Node> children = formPane.getChildren();
        if (children.contains(previousPassword)) {
            children.removeAll(previousPassword, newPassword);
            showShowPasswordButton();
        } else {
            children.addAll(previousPassword, newPassword);
            showHidePasswordButton();
        }
    }

    @FXML
    void toggleNewPasswordHidden(MouseEvent event) {
        if (newPasswordField.isVisible()) {
            newPasswordTextField.setText(newPasswordField.getText());
            newPasswordField.setVisible(false);
            newPasswordTextField.setVisible(true);
        } else {
            newPasswordField.setText(newPasswordTextField.getText());
            newPasswordField.setVisible(true);
            newPasswordTextField.setVisible(false);
        }
    }

    @FXML
    void togglePreviousPasswordHidden(MouseEvent event) {
        if (previousPasswordField.isVisible()) {
            previousPasswordTextField.setText(previousPasswordField.getText());
            previousPasswordField.setVisible(false);
            previousPasswordTextField.setVisible(true);
        } else {
            previousPasswordField.setText(previousPasswordTextField.getText());
            previousPasswordField.setVisible(true);
            previousPasswordTextField.setVisible(false);
        }
    }

    void showLoadingIcon() {
        System.out.println("Show student form loading icon");
        buttons = FXCollections.observableArrayList(buttonsPane.getChildren());
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().add(loadingIcon);
    }

    void showHidePasswordButton() {
        showPasswordBtn.setText("Hide password");
        showPasswordIcon.setRotate(180);
    }

    void showShowPasswordButton() {
        showPasswordBtn.setText("Edit password");
        showPasswordIcon.setRotate(0);
    }

    void updateCurrentImageFile() {
        if (currentImageFile != null) {
            profilePic.setImage(new Image(currentImageFile.getAbsolutePath()));
        }

    }

    void setStage(Stage newStage) {
        this.stage = newStage;
    }

    void bindStageHeight() {
        formPane.heightProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                stage.sizeToScene();
                stage.centerOnScreen();
            };
        });
    }

    void clipProfilePicPane() {

        Circle clipShape = new Circle(40);
        clipShape.setLayoutX(viewWidth / 2);
        clipShape.setLayoutY(viewHeight / 2);
        profilePicPane.setClip(clipShape);
        // profilePic.setClip(clipShape);
        centerProfilePic();
    }

    void centerProfilePic() {
        profilePic.setPreserveRatio(true);

        if (currentImageFile != null) {

            double viewAspectRatio = viewWidth / viewHeight;

            Image currentImage = new Image(currentImageFile.getAbsolutePath());
            double imageAspectRatio = currentImage.getWidth() / currentImage.getHeight();

            if (imageAspectRatio < viewAspectRatio) {
                // The image is taller than the ImageView, so set the fitWidth
                profilePic.setFitWidth(viewWidth);
                profilePic.setFitHeight(viewWidth / imageAspectRatio);
            } else {
                // The image is wider than the ImageView, so set the fitHeight
                profilePic.setFitHeight(viewHeight);
                profilePic.setFitWidth(viewHeight * imageAspectRatio);
            }
        }
    }

    void setProfilePicPaneSize() {
        profilePicPane.setPrefWidth(viewWidth);
        profilePicPane.setMinWidth(viewWidth);
        profilePicPane.setMaxWidth(viewWidth);
        profilePicPane.setPrefHeight(viewHeight);
        profilePicPane.setMinHeight(viewHeight);
        profilePicPane.setMaxHeight(viewHeight);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initializeFormFields();
        toggleEditPasswordFields(null);
        bindStageHeight();
        clipProfilePicPane();
        setProfilePicPaneSize();
        loadingIcon = getLoadingIcon();
    }

    boolean validateForm() {
        return (validateUsernameField() &
                validateProfilePic() &
                validatePhoneNumberField() &
                validatePasswordFields());
    }

    boolean validatePhoneNumberField(){
        return Tools.digitValidation(phoneField, phoneNumberErrorMessage);
    }

    boolean validateUsernameField(){
        return Tools.simpleValidation(usernameField, usernameErrorMessage);
    }

    boolean validatePasswordFields(){
        // return true;
        return Tools.passwordValidation(previousPasswordField, previousPasswordTextField, previousPasswordErrorMessage,
        newPasswordField, newPasswordTextField, newPasswordErrorMessage);
    }

    void initializeFormFields(){
        if(Config.currentUserName != null){
            usernameField.setText(Config.currentUserName);
        }

        if(Config.currentUserFullName != null){
            fullnameField.setText(Config.currentUserFullName);
        }

        if(Config.currentUserPhone != null){
            phoneField.setText(Config.currentUserPhone);
        }

        if(Config.currentUserPic != null && !Config.currentUserPic.isEmpty()){
            profilePic.setImage(Tools.getImageFromBase64(Config.currentUserPic));
        }
    }

    Pane getLoadingIcon() {
        return Tools.getPaneFromControllerAndFxmlPath(null, "/components/CircleLoadingIcon.fxml");
    }

    Service getSaveService() {
        System.out.println("get Save user Service");
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
                                    String currentUserRequestBody = getCurrentUser();

                                    HttpResponse<String> saveResponse;
                                    saveResponse = saveUser(currentUserRequestBody);
                                    Response customResponse = Tools.getCustomResponseFromResponse(saveResponse);

                                    if (customResponse.getCode() < 300 && customResponse.getCode() >= 200) {
                                        saved = true;
                                        updateProgress(100, 100);

                                    }

                                    else if (customResponse.getCode() == 409) {
                                        saved = false;
                                        duplicateUserName = true;
                                        updateProgress(100, 100);

                                    }

                                    else {
                                        updateProgress(100, 100);
                                    }
                                } catch (Exception e) {
                                    System.out.println("error");
                                    saved = false;
                                    updateProgress(100, 100);
                                }
                            }
                        });
                        try {
                            future.get(Config.saveItemTimeout, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            System.out.println("future error");
                            saved = false;
                            updateProgress(100, 100);
                        }
                        return null;
                    }
                };
            }
        };
        return service;
    }


    String getCurrentUser(){
        if(Tools.isEmptyPasswordField(previousPasswordField, previousPasswordTextField) || 
        Tools.isEmptyPasswordField(newPasswordTextField, newPasswordField)){
            return "{"+
            "\"phone_number\":\""+getPhoneNumber()+"\","+
            "\"picture\":\""+getUserPic()+"\","+
            "\"username\":\""+getUserUserName()+"\","+
            "\"full_name\":\""+getUserFullName()+"\""+
        "}";
        }

        else{
            return "{"+
            "\"phone_number\":\""+getPhoneNumber()+"\","+
            "\"picture\":\""+getUserPic()+"\","+
            "\"username\":\""+getUserUserName()+"\","+
            "\"full_name\":\""+getUserFullName()+"\","+
            "\"password\":\""+getNewPassword()+"\","+
            "\"old_password\":\""+getOldPassword()+"\""+
        "}";
        }
    }

    String getNewPassword(){
        if(newPasswordField.isVisible()){
            return newPasswordField.getText();
        }
        return newPasswordTextField.getText();
    }

    String getOldPassword(){
        if(previousPasswordField.isVisible()){
            return previousPasswordField.getText();
        }
        return previousPasswordTextField.getText();
    }

    HttpResponse<String> saveUser(String requestBody) throws Exception {
        System.out.println(requestBody);
        return RequestManager.updateItem("custom_user/", requestBody).get();
    }

    void bindSaveServiceWithDisplay(Service service) {
        service.progressProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                if (Math.abs(((Double) newValue - 1.0)) < Math.pow(10, -15)) {
                    if (saved) {
                        System.out.println("saved");
                        removeLoadingIcon();
                        saveCurrentUserLocally();
                        showSuccessIconForSeconds();
                        refreshProfilePane();
                        // closePane();
                    } else {
                        System.out.println("not saved");
                        removeLoadingIcon();
                        if(duplicateUserName){
                            String duplicateUserNameErrorMessage ="The username is already taken. Please change this username";
                            showError(duplicateUserNameErrorMessage);
                        }
                        else{
                            showErrorMessage();
                        }
                        
                    }
                }
            };
        });
    }

    void waitAndGoBack() {
        Service service = getWaitService();
        bindWaitServiceWithDisplay(service);
        service.start();
    }

    void bindWaitServiceWithDisplay(Service service) {
        service.progressProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                if (Math.abs(((Double) newValue - 1.0)) < Math.pow(10, -15)) {
                    Tools.closeStageFromNode(formPane);
                }
            };
        });
    }

    void showSuccessIconForSeconds() {
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().add(getSuccessPane());
        waitAndGoBack();
    }

    void removeLoadingIcon() {
        System.out.println("Remove loading icon");
        buttonsPane.getChildren().clear();
        buttonsPane.getChildren().addAll(buttons);
    }

    boolean validateProfilePic(){
        if(Tools.getKBImageSize(profilePic.getImage()) > 100){
            showError("The size of the image is too large pick an image with size around 100kb");
            return false;
        }
        return true;
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
                                    saved = false;
                                    updateProgress(100, 100);
                                }

                            }
                        });
                        try {
                            future.get(Config.saveItemTimeout, TimeUnit.SECONDS);
                            saved = true;
                        } catch (Exception e) {
                            System.out.println("future error");
                            saved = false;
                            updateProgress(100, 100);
                        }
                        return null;
                    }
                };
            }
        };
        return service;
    }

    void showErrorMessage() {
        System.out.println("show error message");
        Initializable errorInSavingPageController = new ErrorPageController(
                "There was an issue while trying to save this item. Check your internet connection and try again.");
        Tools.showModal(errorInSavingPageController, "/pages/ErrorPage.fxml",
                Tools.getStageFromNode(formPane));
    }

    void showError(String error) {
        System.out.println("show error message");
        Initializable errorInSavingPageController = new ErrorPageController(error);
        Tools.showModal(errorInSavingPageController, "/pages/ErrorPage.fxml",
                Tools.getStageFromNode(formPane));
    }

    Pane getSuccessPane() {
        return Tools.getPaneFromControllerAndFxmlPath(null, "/components/SuccessPane.fxml");
    }

    String getUserFullName(){
        return fullnameField.getText();
    }

    String getUserPic(){
        if(profilePic.getImage() != null){
            System.out.println(Tools.imageToBase64(profilePic.getImage()));
            return Tools.imageToBase64(profilePic.getImage());
        }
        else{
            return "";
        }
        
    }

    String getUserUserName(){
        return usernameField.getText();
    }

    String getPhoneNumber(){
        return phoneField.getText();
    }

    void saveCurrentUserLocally(){
        Config.currentUserFullName = getUserFullName();
        Config.currentUserPic = getUserPic();
        Config.currentUserName = getUserUserName();
        Config.currentUserPhone = getPhoneNumber();
    }

    void refreshProfilePane(){
        ((ProfilePaneController)profileSectionController).refresh();
    }
}
