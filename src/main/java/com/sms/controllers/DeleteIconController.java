package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.sms.tools.Tools;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class DeleteIconController implements Initializable{
    Function<NullType, Stage> getPrimaryStage;

    public DeleteIconController(Function<NullType, Stage> getPrimaryStage) {
        super();
        this.getPrimaryStage = getPrimaryStage;
    }

    @FXML
    void onDelete(){
        Tools.showModal(new DeleteValidationController(getPrimaryStage.apply(null)), "/pages/DeleteValidationPane.fxml", getPrimaryStage.apply(null));
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }
}
