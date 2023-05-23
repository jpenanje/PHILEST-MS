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
    Function delete;
    Function refresh;

    public DeleteIconController(Function<NullType, Stage> getPrimaryStage, Function delete, Function refresh) {
        super();
        this.getPrimaryStage = getPrimaryStage;
        this.delete = delete;
        this.refresh = refresh;
    }

    @FXML
    void onDelete(){
        Tools.showModal(new DeleteValidationController(getPrimaryStage.apply(null), delete, refresh), "/pages/DeleteValidationPane.fxml", getPrimaryStage.apply(null));
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }
}
