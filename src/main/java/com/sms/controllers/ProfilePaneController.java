package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.sms.tools.Config;
import com.sms.tools.Tools;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

// Controller of profile section in top bar
public class ProfilePaneController implements Initializable {

    Function<Initializable, NullType> showProfileModal;

    // constructor with function for showing the edit profile modal
    public ProfilePaneController(Function<Initializable, NullType> showProfileModal) {
        super();
        this.showProfileModal = showProfileModal;
    }

    @FXML
    private ImageView pic;

    @FXML
    private Text role;

    @FXML
    private Text userName;

    // shows the view/edit profile modal
    @FXML
    void showProfileModal(MouseEvent event) {
        showProfileModal.apply(this);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        refresh();
    }

    // refreshes the profile section on the right of the top bar
    void refresh(){
        if(Config.currentUserName != null && !Config.currentUserName.isEmpty()){
            userName.setText(Config.currentUserName);
        }
        if(Config.currentUserPic != null && !Config.currentUserPic.isEmpty()){
            pic.setImage(Tools.getImageFromBase64(Config.currentUserPic));
        }
        role.setText("admin");
    }

}

