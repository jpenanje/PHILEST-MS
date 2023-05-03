package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentsController extends TableSectionController{
    
    public StudentsController() {
        // requires the search controller and the fxml path
        super(new SearchStudentController(), "/sections/StudentSearch.fxml");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        super.initialize(arg0, arg1);
    }
}
