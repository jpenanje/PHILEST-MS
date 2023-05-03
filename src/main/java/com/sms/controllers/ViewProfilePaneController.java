package com.sms.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
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

    private File currentImageFile;

    private Stage stage;

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

    double viewWidth = 82;
    double viewHeight = 79.5;

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {

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
        toggleEditPasswordFields(null);
        bindStageHeight();
        clipProfilePicPane();
        setProfilePicPaneSize();
    }

}
