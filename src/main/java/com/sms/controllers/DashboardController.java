package com.sms.controllers;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.models.Response;
import com.sms.tools.Config;
import com.sms.tools.RequestManager;
import com.sms.tools.Tools;

import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

// Controller for dashboard section
public class DashboardController implements Initializable {

    public DashboardController() {
        super();
    }

    // Dashboard constructor from node containing data and meta data
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
    private Text cashins;

    @FXML
    private Text cashouts;

    @FXML
    private MenuButton year;

    @FXML
    private StackPane graphPane;

    @FXML
    private StackPane basePane;

    String baseUrl = "dashboard/";

    String currentYear;

    Node cachedBasePane;

    Service fetchItemsService;

    ArrayList<ArrayList> items;

    @FXML
    private BarChart<String, Double> barChart;

    // changes the year of the graph and refreshed the data
    @FXML
    void changeYear(ActionEvent event) {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("init");
        showLoadingIcon();
        fetchItems(fetchItemsService);
        // initYearsField();
        // initBarChart();
    }

    // shows the loading icon
    void showLoadingIcon() {
        if (cachedBasePane == null) {
            cachedBasePane = basePane.getChildren().get(0);
        }
        Service fetchItemsService = getFetchItemsService();
        this.fetchItemsService = fetchItemsService;

        bindServiceWithDisplay(fetchItemsService);
        basePane.getChildren().set(0, getLoadingIcon(fetchItemsService));
        System.out.println("Loading icon...");
    }

    // starts the service to fetch items
    void fetchItems(Service service) {
        try {
            service.start();
        } catch (Exception e) {
            displayErrorPane();
        }
    }

    // displays the error pane
    void displayErrorPane() {
        basePane.getChildren().clear();
        basePane.getChildren().add(getErrorPane());
    }

    // returns the error pane
    Pane getErrorPane() {
        Function<NullType, NullType> refresh = new Function<NullType, NullType>() {
            @Override
            public NullType apply(NullType t) {
                refresh();
                return null;
            }
        };
        return Tools.getPaneFromControllerAndFxmlPath(new ErrorPaneController(refresh), "/sections/ErrorPane.fxml");
    }

    // returns the loading icon
    Node getLoadingIcon(Service service) {
        return Tools.getLoadingIcon(service);
    }

    // initializes the bar chart information and appearance
    void initBarChart(ArrayList<JsonNode> jsonData) {
        initNetIncome(jsonData);
        barChart.getData().clear();
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
        series1.getData().add(new XYChart.Data<>("Jan", getCashOutFromIndexAndData(0, jsonData)));
        series1.getData().add(new XYChart.Data<>("Feb", getCashOutFromIndexAndData(1, jsonData)));
        series1.getData().add(new XYChart.Data<>("Mar", getCashOutFromIndexAndData(2, jsonData)));
        series1.getData().add(new XYChart.Data<>("Apr", getCashOutFromIndexAndData(3, jsonData)));
        series1.getData().add(new XYChart.Data<>("May", getCashOutFromIndexAndData(4, jsonData)));
        series1.getData().add(new XYChart.Data<>("Jun", getCashOutFromIndexAndData(5, jsonData)));
        series1.getData().add(new XYChart.Data<>("Jul", getCashOutFromIndexAndData(6, jsonData)));
        series1.getData().add(new XYChart.Data<>("Aug", getCashOutFromIndexAndData(7, jsonData)));
        series1.getData().add(new XYChart.Data<>("Sep", getCashOutFromIndexAndData(8, jsonData)));
        series1.getData().add(new XYChart.Data<>("Oct", getCashOutFromIndexAndData(9, jsonData)));
        series1.getData().add(new XYChart.Data<>("Nov", getCashOutFromIndexAndData(10, jsonData)));
        series1.getData().add(new XYChart.Data<>("Dec", getCashOutFromIndexAndData(11, jsonData)));

        XYChart.Series<String, Double> series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data<>("Jan", getCashInFromIndexAndData(0, jsonData)));
        series2.getData().add(new XYChart.Data<>("Feb", getCashInFromIndexAndData(1, jsonData)));
        series2.getData().add(new XYChart.Data<>("Mar", getCashInFromIndexAndData(2, jsonData)));
        series2.getData().add(new XYChart.Data<>("Apr", getCashInFromIndexAndData(3, jsonData)));
        series2.getData().add(new XYChart.Data<>("May", getCashInFromIndexAndData(4, jsonData)));
        series2.getData().add(new XYChart.Data<>("Jun", getCashInFromIndexAndData(5, jsonData)));
        series2.getData().add(new XYChart.Data<>("Jul", getCashInFromIndexAndData(6, jsonData)));
        series2.getData().add(new XYChart.Data<>("Aug", getCashInFromIndexAndData(7, jsonData)));
        series2.getData().add(new XYChart.Data<>("Sep", getCashInFromIndexAndData(8, jsonData)));
        series2.getData().add(new XYChart.Data<>("Oct", getCashInFromIndexAndData(9, jsonData)));
        series2.getData().add(new XYChart.Data<>("Nov", getCashInFromIndexAndData(10, jsonData)));
        series2.getData().add(new XYChart.Data<>("Dec", getCashInFromIndexAndData(11, jsonData)));

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

    // returns the cash in value from fetched data
    double getCashInFromIndexAndData(int index, ArrayList<JsonNode> data){
        try {
            System.out.println();
            return data.get(index).get("cashin").asInt();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // returns the cash out value from fetched data
    double getCashOutFromIndexAndData(int index, ArrayList<JsonNode> data){
        try {
            System.out.println();
            return data.get(index).get("cashout").asInt();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // initializes the field year
    void initYearsField(ArrayList<String> yearsList) {
        year.getItems().setAll(getYearsMenuItems(yearsList));
    }

    // returns the menuItems for the year field
    ArrayList<MenuItem> getYearsMenuItems(ArrayList<String> yearsString) {
        ArrayList<MenuItem> yearsMenuItems = new ArrayList<MenuItem>();
        for (String yearString : yearsString) {
            yearsMenuItems.add(getYearMenuItem(yearString));
        }
        return yearsMenuItems;
    }

    // returns the year menu item for a given year
    MenuItem getYearMenuItem(String yearString) {
        MenuItem toBeReturned = new MenuItem(yearString);
        final String yearstring = yearString;
        toBeReturned.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentYear = yearString;
                refresh();
            }
        });
        toBeReturned.setStyle("-fx-cursor: HAND;");
        return toBeReturned;
    }

    // refreshes the dashboard
    void refresh() {
        items = null;

        showLoadingIcon();
        fetchItems(fetchItemsService);
    }

    // returns the service to fetch the items of the dashboard
    Service getFetchItemsService() {
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
                                    Function<Double, NullType> incrementProgress = new Function<Double, NullType>() {
                                        public NullType apply(Double increment) {

                                            try {
                                                Platform.runLater(() -> {
                                                    double newValue = (getProgress() * 100) + increment;
                                                    if (newValue - 100.0 <= Math.pow(10, -2)) {
                                                        updateProgress(newValue, 100.0);
                                                    }
                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            return null;
                                        }
                                    };
                                    // Response numOfItemsresponse = RequestManager.fetchNumberOfItems(searchUrl);

                                    HttpResponse<String> yearsResponse = RequestManager
                                            .fetchYears("years/")
                                            .get();
                                    Response yearsCustomResponse = Tools
                                            .getCustomResponseFromResponse(yearsResponse);

                                    if (yearsCustomResponse.getCode() == 200) {
                                        System.out.println(yearsCustomResponse.getData());
                                        updateProgress(Config.yearsLoadingIncrement, 100);

                                        items = new ArrayList<>();

                                        items.add(getYearsFromResponse(yearsCustomResponse));
                                        initYearFieldAndCurrentYear();

                                        // fetch metrics
                                        HttpResponse<String> metricsResponse = RequestManager
                                                .fetchMetrics("metrics/", getYearParam(items.get(0)))
                                                .get();
                                        Response metricsCustomResponse = Tools
                                                .getCustomResponseFromResponse(metricsResponse);

                                        System.out.println(metricsCustomResponse.getData());
                                        if (metricsCustomResponse.getCode() == 200) {
                                            incrementProgress.apply(Config.metricsLoadingIncrement);
                                            items.add(exractMetricsFromResponse(metricsCustomResponse));
                                        } else {
                                            items = null;
                                            updateProgress(100, 100);
                                        }

                                        // fetch graph data
                                        HttpResponse<String> graphResponse = RequestManager
                                                .fetchMetrics("graph/", getYearParam(items.get(0)))
                                                .get();
                                        Response graphCustomResponse = Tools
                                                .getCustomResponseFromResponse(graphResponse);

                                        if (graphCustomResponse.getCode() == 200) {
                                            incrementProgress.apply(Config.metricsLoadingIncrement);
                                            items.add(getGraphDataFromResponse(graphCustomResponse));

                                        } else {
                                            items = null;
                                            updateProgress(100, 100);
                                        }

                                        updateProgress(100, 100);
                                    } else {
                                        items = null;
                                        updateProgress(100, 100);
                                    }
                                } catch (Exception e) {
                                    System.out.println("error");
                                    e.printStackTrace();
                                    items = null;
                                    updateProgress(100, 100);
                                }

                            }
                        });
                        try {
                            future.get(Config.fetchItemTimeout, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            System.out.println("future error");
                            items = null;
                            updateProgress(100, 100);
                        }
                        return null;
                    }
                };
            }
        };
        return service;
    }

    // returns the url parameter representation of the year
    String getYearParam(ArrayList<String> years) {
        if (currentYear != null) {
            return "?" + Tools.getParamFromFieldAndAttribute(currentYear, "current_year");
        } else {
            return "?" + Tools.getParamFromFieldAndAttribute(getLatestYear(years), "current_year");
        }
    }

    // gets the latest academic year from a list of academic years
    String getLatestYear(ArrayList<String> years) {
        Collections.sort(years);
        return years.get(years.size() - 1);
    }

    // returns a list of years obtained from a response
    ArrayList<String> getYearsFromResponse(Response yearsResponse) {
        ArrayList<String> years = new ArrayList();
        for (JsonNode yearNode : yearsResponse.getData()) {
            years.add(yearNode.get("year").asText());
        }
        return years;
    }

    // extracts the metrics from a response and places them into a list
    ArrayList<Integer> exractMetricsFromResponse(Response metricsResponse) {
        ArrayList<Integer> metrics = new ArrayList<>();
        JsonNode data = metricsResponse.getData();
        metrics.add(data.get("num_students").asInt());
        metrics.add(data.get("num_classes").asInt());
        metrics.add(data.get("num_cashin").asInt());
        metrics.add(data.get("num_cashout").asInt());

        return metrics;
    }

    // ensures that te progression is represented on design
    void bindServiceWithDisplay(Service service) {
        service.progressProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                if (Math.abs(((Double) newValue - 1.0)) < Math.pow(10, -15)) {
                    if (items != null) {
                        refreshDashboardItems();
                        loadCachedPane();
                    } else {
                        displayErrorPane();
                    }

                }
            };
        });
    }

    // shows information on the dashboard from the items
    void refreshDashboardItems() {
        initYearsField(items.get(0));
        initMetrics(items.get(1));
        initBarChart(items.get(2));
    }

    // loads the cached pane
    void loadCachedPane() {
        basePane.getChildren().clear();
        basePane.getChildren().add(cachedBasePane);
    }

    // displays fetched metrics data
    void initMetrics(ArrayList<Integer> metrics) {
        students.setText(metrics.get(0) + "");
        classes.setText(metrics.get(1) + "");
        cashins.setText(metrics.get(2) + "");
        cashouts.setText(metrics.get(3) + "");
        
    }

    // sets the initial value of the year field
    void initYearFieldAndCurrentYear() {
        if(currentYear == null){
            currentYear = getLatestYear(items.get(0));
        year.setText(currentYear);
        }
        
    }

    // returns json nodes for the graph from the response
    ArrayList<JsonNode> getGraphDataFromResponse(Response graphResponse) {
        ArrayList<JsonNode> graphData = new ArrayList<>();
        for (JsonNode node : graphResponse.getData()) {
            graphData.add(node);
        }
        return graphData;
    }

    // inits the net income above the graph
    void initNetIncome(ArrayList<JsonNode> data){
        int netIncomeValue = data.get(12).get("net_income").asInt();
        String netIncomeText = Tools.addCommasToStringValue(netIncomeValue+"") + " FCFA";
        netIncome.setText(netIncomeText);
    }
    
}
