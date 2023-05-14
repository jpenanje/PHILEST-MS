package com.sms.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.interfaces.TableRowable;
import com.sms.models.CustomTableCell;
import com.sms.models.Student;
import com.sms.tools.Config;
import com.sms.tools.Tools;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TableSectionController implements Initializable {

    int currentPage = 0;
    int maxPages = 0;
    int maxPageItems = 10;
    Initializable searchBarController;
    String searchBarFXMLPath;
    ArrayList<String> titles;
    Service fetchItemsService;
    ArrayList<TableRowable> items = new ArrayList<>();
    ArrayList<TableColumn<TableRowable, String>> columns = new ArrayList<>();
    ObservableList<TableRowable> displayItems = FXCollections.observableArrayList(new ArrayList());
    ArrayList<Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>> cellFactories;

    public TableSectionController(Initializable searchBarController, String searchBarFXMLPath) {
        super();
        this.searchBarController = searchBarController;
        this.searchBarFXMLPath = searchBarFXMLPath;
    }

    @FXML
    StackPane basePane;

    Node cachedBasePane;

    @FXML
    StackPane tablePane;

    @FXML
    VBox indicesPane;

    @FXML
    VBox deleteIconsPane;

    @FXML
    private Text totalItems;

    @FXML
    private Text itemsRange;

    @FXML
    private Text pageNumber;

    @FXML
    void nextPage(ActionEvent event) {
        if (currentPage < maxPages - 1) {
            currentPage++;
            refreshDisplayItems();
        } else {
            showLastPageMessage();
        }
    }

    // Table
    @FXML
    TableView<TableRowable> table;

    @FXML
    StackPane searchPane;

    @FXML
    void previousPage(ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            refreshDisplayItems();
        } else {
            showNoPreviousPageMessage();
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("init");
        showLoadingIcon();
        fetchItems(fetchItemsService);
        initializeTable();
        bindTablePaneWithNumberOfDisplayItems();
        loadSearchPane();
        loadMockItems();

        // refreshRows();
    }

    void bindTablePaneWithNumberOfDisplayItems() {
        tablePane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
                System.out.println("changing elements based on page height");
                maxPageItems = (int) ((tablePane.getHeight() / Config.iconHeight) - 1.7);
                refreshDisplayItems();
                System.out.println("new max page items: " + maxPageItems);
            }
        });
    }

    void refreshDisplayItems() {
        // displayItems.addListener(new ListChangeListener<TableRowable>() {
        // @Override
        // public void onChanged(Change<? extends TableRowable> newValue) {
        if (itemsAreNotEmpty()) {
            removeLoadingIcon();
            setMaxPages();
            setDisplayItems();
            refreshRows();
            refreshMaxPages();
            refreshCurrentPage();
        } else {
            showLoadingIcon();
        }
        // }
        // });
    }

    void setDisplayItems() {

        if (currentPage > maxPages) {
            currentPage = maxPages;
        }

        int startIndex = (currentPage * maxPageItems);
        System.out.println("maxPages " + maxPages);
        System.out.println("currentPage " + currentPage);

        if (currentPage == maxPages) {
            int endIndex = startIndex + (items.size() % maxPageItems);
            displayItems.setAll(items.subList(startIndex, endIndex));
        } else {
            displayItems.setAll(items.subList(startIndex, startIndex + maxPageItems));
        }
    }

    void setMaxPages() {
        maxPages = items.size() / maxPageItems;
    }

    void refreshRows() {
        System.out.println("refresh rows");
        table.getItems().clear();
        ;
        for (TableRowable item : displayItems) {
            table.getItems().add(item);
        }
        refreshIndices();
        refreshDeleteIcons();
    }

    void initializeTable() {
        table.getColumns().clear();
        table.getColumns().addAll(columns);
        // addColumns();
    }

    // void addColumns() {
    // int index = 0;
    // for (String title : titles) {
    // System.out.println(title);
    // TableColumn<TableRowable, String> column = new TableColumn<>(title);
    // column.setCellValueFactory(new PropertyValueFactory<TableRowable,
    // String>("pupilName"));
    // column.setCellFactory(cellFactories.get(index));
    // // column.setPrefWidth(Control.USE_COMPUTED_SIZE);
    // table.getColumns().add(column);
    // // column
    // index++;
    // }
    // }

    void refreshIndices() {
        System.out.println("refresh indices");
        indicesPane.getChildren().clear();
        for (int i = 0; i < displayItems.size(); i++) {
            int paneIndex = currentPage * maxPageItems + i + 1;
            indicesPane.getChildren().add(getIndexPane(paneIndex));
        }
    }

    StackPane getIndexPane(int paneIndex) {
        StackPane pane = new StackPane();
        pane.setPrefWidth(10);
        pane.setPrefHeight(Config.iconHeight);
        Text content = new Text("" + paneIndex);
        content.setFill(Color.valueOf("#A6A6A6"));
        pane.getChildren().add(content);
        pane.setStyle("-fx-background-color:transparent;");
        return pane;
        // return new StackPane(new Text("" + paneIndex));
    }

    void refreshDeleteIcons() {
        deleteIconsPane.getChildren().clear();
        for (int i = 0; i < displayItems.size(); i++) {
            int paneIndex = currentPage * maxPageItems + i;
            deleteIconsPane.getChildren().add(getDeleteIconPane(paneIndex));
        }
    }

    void refreshMaxPages() {
        // round up division
        maxPages = (int) Math.ceil((double) items.size() / maxPageItems);
        totalItems.setText("" + items.size());
        itemsRange.setText(getItemRangeStr());
    }

    String getItemRangeStr() {
        int itemsSize = items.size();
        if (itemsSize > 0) {
            int floorValue = currentPage * maxPageItems + 1;
            int roofValue = floorValue + displayItems.size() - 1;
            return floorValue + " - " + roofValue;
        } else {
            return "";
        }
    }

    StackPane getDeleteIconPane(final int paneIndex) {
        // StackPane deleteIconPane = new StackPane(new Text("" + paneIndex));
        StackPane deleteIconPane = new StackPane();
        Function<NullType, Stage> getPrimaryStage = new Function<NullType, Stage>() {
            @Override
            public Stage apply(NullType t) {
                return getPrimaryStage();
            }
        };
        // Stage primaryStage = getPrimaryStage();
        deleteIconPane.setPrefHeight(Config.iconHeight);
        deleteIconPane.setMinHeight(Config.iconHeight);
        deleteIconPane.setMaxHeight(Config.iconHeight);
        deleteIconPane.getChildren().add(Tools.getPaneFromControllerAndFxmlPath(
                new DeleteIconController(getPrimaryStage), "/components/deleteIcon.fxml"));
        deleteIconPane.setCursor(Cursor.HAND);
        deleteIconPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                System.out.println("delete " + paneIndex);
            }
        });
        return deleteIconPane;
    }

    boolean itemsAreNotEmpty() {
        return !items.isEmpty();
    }

    void loadMockItems() {
        // items = null;
        items.add(new Student());
        items.add(new Student());
        items.add(new Student());
        items.add(new Student());
        items.add(new Student());
        items.add(new Student());
        items.add(new Student());
        items.add(new Student());
        items.add(new Student());
        items.add(new Student());
        items.add(new Student());
        items.add(new Student());
        items.add(new Student());
        // displayItems.add(new Student("class 2"));
        // table.getItems().add(new Student());
    }

    void showLastPageMessage() {
        System.out.println("This is the last page");
    }

    void showNoPreviousPageMessage() {
        System.out.println("This is the first page you can't go back");
    }

    void showLoadingIcon() {
        cachedBasePane = basePane.getChildren().get(0);
        Service fetchItemsService = getFetchItemsService();
        this.fetchItemsService = fetchItemsService;
        bindServiceWithDisplay(fetchItemsService);
        basePane.getChildren().set(0, getLoadingIcon(fetchItemsService));
        System.out.println("Loading icon...");
    }

    void fetchItems(Service service) {
        try {
            service.start();
        } catch (Exception e) {
            displayErrorPane();
        }
    }

    void removeLoadingIcon() {
        System.out.println("removed loading icon...");
    }

    void setColumns(ArrayList columns) {
        this.columns = columns;
    }

    void loadSearchPane() {

        searchPane.getChildren().add(Tools.getPaneFromControllerAndFxmlPath(searchBarController, searchBarFXMLPath));

    }

    Stage getPrimaryStage() {
        System.out.println(deleteIconsPane.getScene());
        return (Stage) ((deleteIconsPane.getScene()).getWindow());
    }

    void refreshCurrentPage() {
        pageNumber.setText("" + (currentPage + 1));
    }

    Node getLoadingIcon(Service service) {
        return Tools.getLoadingIcon(service);
    }

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
                                    for (int i = 0; i <= 100; i++) {
                                        updateProgress(i, 100);
                                        try {
                                            Thread.sleep(50);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("error");
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

    void bindServiceWithDisplay(Service service) {
        service.progressProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                if (Math.abs(((Double) newValue - 1.0)) < Math.pow(10, -15)) {
                    if (items != null) {
                        refreshDisplayItems();
                        loadCachedPane();
                    } else {
                        displayErrorPane();
                    }

                }
            };
        });
    }

    void loadCachedPane() {
        basePane.getChildren().clear();
        basePane.getChildren().add(cachedBasePane);
    }

    void displayErrorPane() {
        basePane.getChildren().clear();
        basePane.getChildren().add(getErrorPane());
    }

    Pane getErrorPane() {
        Function<NullType, NullType> refresh = new Function<NullType,NullType>() {
            @Override
            public NullType apply(NullType t) {
                initialize(null, null);
                return null;
            }
        };
        return Tools.getPaneFromControllerAndFxmlPath(new ErrorPaneController(refresh), "/sections/ErrorPane.fxml");
    }
}
