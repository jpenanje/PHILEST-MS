package com.sms.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.fxml.Initializable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class DashboardController implements Initializable {

    public DashboardController() {
        super();
    }

    public DashboardController(JsonNode node) {
        super();
    }

    @FXML
    private Text classes;

    @FXML
    private Text netIncome;

    @FXML
    private Text students;

    @FXML
    private Text subjects;

    @FXML
    private Text teachers;

    @FXML
    private MenuButton year;

    @FXML
    private StackPane graphPane;

    @FXML
    private BarChart<String, Double> barChart;

    @FXML
    void changeYear(ActionEvent event) {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        String[] years = {"2020", "2021", "2022", "2023"};
        ArrayList<String> yearsList = new ArrayList<>(Arrays.asList(years));
        year.getItems().setAll(getYearsMenuItems(yearsList));
        ValueAxis<Double> yAxis = (ValueAxis<Double>) barChart.getYAxis();
        yAxis.setTickLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                if (object.doubleValue() < 1000) {
                    return object.toString();
                } else {
                    return String.format("%.0fK", object.doubleValue() / 1000);
                }
            }
        
            @Override
            public Double fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });


        Path yAxisLine = (Path) barChart.lookup(".chart-vertical-grid-lines");
        yAxisLine.setOpacity(0);
        CornerRadii cornerRadii = new CornerRadii(20, 20, 0, 0, false);

        String firstGraphColor = "#FF2020";
        String secondGraphColor = "#7CFF7C";
        double barWidth = 20;
        barChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        barChart.setLegendVisible(false);
        barChart.getXAxis().setStyle("-fx-border-color: #E6E6E6; -fx-border-width:0.91 0 0 0;");
        barChart.getYAxis().setStyle("-fx-border-width:0;");
        barChart.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                barChart.setBarGap(barChart.getWidth() / 90);
                barChart.setCategoryGap(barChart.getWidth() / 30);
            }
        });


        XYChart.Series<String, Double> series1 = new XYChart.Series<>();
        series1.getData().add(new XYChart.Data<>("Jan", 100000.0));
        series1.getData().add(new XYChart.Data<>("Feb", 150000.0));
        series1.getData().add(new XYChart.Data<>("Mar", 300000.0));
        series1.getData().add(new XYChart.Data<>("Apr", 200000.0));
        series1.getData().add(new XYChart.Data<>("May", 300000.0));
        series1.getData().add(new XYChart.Data<>("Jun", 120000.0));
        series1.getData().add(new XYChart.Data<>("Jul", 200000.0));
        series1.getData().add(new XYChart.Data<>("Aug", 220000.0));
        series1.getData().add(new XYChart.Data<>("Sep", 250000.0));
        series1.getData().add(new XYChart.Data<>("Oct", 200000.0));
        series1.getData().add(new XYChart.Data<>("Nov", 120000.0));
        series1.getData().add(new XYChart.Data<>("Dec", 300000.0));


        XYChart.Series<String, Double> series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data<>("Jan", 200000.0));
        series2.getData().add(new XYChart.Data<>("Feb", 130000.0));
        series2.getData().add(new XYChart.Data<>("Mar", 120000.0));
        series2.getData().add(new XYChart.Data<>("Apr", 140000.0));
        series2.getData().add(new XYChart.Data<>("May", 220000.0));
        series2.getData().add(new XYChart.Data<>("Jun", 270000.0));
        series2.getData().add(new XYChart.Data<>("Jul", 140000.0));
        series2.getData().add(new XYChart.Data<>("Aug", 190000.0));
        series2.getData().add(new XYChart.Data<>("Sep", 270000.0));
        series2.getData().add(new XYChart.Data<>("Oct", 290000.0));
        series2.getData().add(new XYChart.Data<>("Nov", 140000.0));
        series2.getData().add(new XYChart.Data<>("Dec", 200000.0));

        barChart.getData().addAll(series1, series2);
        // NumberAxis yAxis = (NumberAxis)barChart.getYAxis();

        for (XYChart.Data<String, Double> data : series1.getData()) {
            StackPane bar = (StackPane) data.getNode();
            bar.setMinWidth(barWidth);
            bar.setPrefWidth(barWidth);
            bar.setMaxWidth(barWidth);
            bar.setPrefSize(barWidth, bar.getPrefHeight());
            bar.setBackground(new Background(new BackgroundFill(Color.valueOf(firstGraphColor), cornerRadii, null)));
        }

        for (XYChart.Data<String, Double> data : series2.getData()) {
            StackPane bar = (StackPane) data.getNode();
            bar.setMinWidth(barWidth);
            bar.setPrefWidth(barWidth);
            bar.setMaxWidth(barWidth);
            bar.setBackground(new Background(new BackgroundFill(Color.valueOf(secondGraphColor), cornerRadii, null)));
        }

        barChart.setAnimated(true);
    }

    ArrayList<MenuItem> getYearsMenuItems(ArrayList<String> yearsString){
        ArrayList<MenuItem> yearsMenuItems = new ArrayList<MenuItem>();
        for (String yearString : yearsString){
            yearsMenuItems.add(getYearMenuItem(yearString));
        }
        return yearsMenuItems;
    }

    MenuItem getYearMenuItem(String yearString){
        MenuItem toBeReturned = new MenuItem(yearString);
        final String yearstring = yearString;
        toBeReturned.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(yearstring);
            }
        });
        toBeReturned.setStyle("-fx-cursor: HAND;");
        return toBeReturned;
    }
}
