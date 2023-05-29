package com.sms.business;

import javafx.application.Application;

public class Launcher {

    // Reflects the call of the app launch from App.java so as to avoid a bug related to using maven
    // and java
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
