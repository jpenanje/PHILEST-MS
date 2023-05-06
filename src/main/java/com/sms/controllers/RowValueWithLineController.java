package com.sms.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.sms.tools.Config;
import com.sms.tools.Tools;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * RowValueWithLineController
 */
public class RowValueWithLineController implements Initializable {

    String valueStr;

    public RowValueWithLineController(String value) {
        super();
        this.valueStr = value;
    }

    @FXML
    private Text percentage;

    @FXML
    private StackPane progressPane;

    @FXML
    private Text value;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        String[] items = valueStr.split(Pattern.quote(Config.valueDelimiter));
        initValue(items);
        int percentage = initPercentage(items);
        initProgressBar(percentage);
    }

    void initValue(String[] items) {
        value.setText(Tools.addCommasToStringValue(items[0]));
    }

    int initPercentage(String[] items) {
        int percentage = (int) Tools.getPercentage(Double.parseDouble(items[0]), Double.parseDouble(items[1]));
        this.percentage.setText(percentage + "%");
        return percentage;
    }

    void initProgressBar(int percentage) {
        int maxPaneWidth = 65;
        progressPane.setPrefWidth(maxPaneWidth * percentage / 100);
        progressPane.setMinWidth(maxPaneWidth * percentage / 100);
        progressPane.setMaxWidth(maxPaneWidth * percentage / 100);
        String backgroundColor = Tools.getColorFromPercentage(percentage);
        progressPane.setStyle(progressPane.getStyle() + "-fx-background-color:"+backgroundColor+";");
        
    }
}