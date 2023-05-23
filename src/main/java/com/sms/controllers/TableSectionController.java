package com.sms.controllers;

import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.interfaces.ISearchBar;
import com.sms.interfaces.TableRowable;
import com.sms.models.CashIn;
import com.sms.models.CustomTableCell;
import com.sms.models.Response;
import com.sms.models.Student;
import com.sms.tools.Config;
import com.sms.tools.RequestManager;
import com.sms.tools.Tools;

import javafx.application.Platform;
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
import javafx.scene.control.Button;
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
    JsonNode currentNode;
    Service fetchItemsService;
    String baseUrl;
    String searchUrl;
    Function<JsonNode, TableRowable> getItemFromNode;
    ArrayList<String> formDropDownAttributes;
    ArrayList<TableRowable> items = new ArrayList<>();
    ArrayList<TableColumn<TableRowable, String>> columns = new ArrayList<>();
    ObservableList<TableRowable> displayItems = FXCollections.observableArrayList(new ArrayList());
    ArrayList<Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>> cellFactories;

    ArrayList<ArrayList> formDropDownLists = new ArrayList();

    ArrayList<ArrayList> formDisplayDropDownLists = new ArrayList();

    public TableSectionController(Initializable searchBarController, String searchBarFXMLPath, JsonNode currentNode,
            Function<JsonNode, TableRowable> getItemfromNode, String baseUrl,
            ArrayList<String> formDropDownAttributes) {
        super();
        this.getItemFromNode = getItemfromNode;
        this.searchBarController = searchBarController;
        this.searchBarFXMLPath = searchBarFXMLPath;
        this.currentNode = currentNode;
        this.formDropDownAttributes = formDropDownAttributes;
        this.baseUrl = baseUrl;
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
    private Text title;

    @FXML
    private Button addButton;

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
        initializeSearchUrl();
        bindSearchBarWithSearchUrl();
        initializeTitle();
        initializeAddButton();
        showLoadingIcon();
        fetchItems(fetchItemsService);
        initializeTable();
        bindTablePaneWithNumberOfDisplayItems();
        bindTableRowsWithEditAction();
        loadSearchPane();
        // loadMockItems();

        // refreshRows();
    }

    void bindTableRowsWithEditAction() {
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    // Double click detected
                    int selectedIndex = table.getSelectionModel().getSelectedIndex();
                    if (selectedIndex != -1) {
                        editItemByIndex(selectedIndex);
                    }
                }
            }
        });
    }

    void editItemByIndex(int index) {
        int actualIndex = (currentPage * maxPageItems) + index;
        TableRowable toBeEdited = items.get(actualIndex);
        displayEditItemPane(toBeEdited);
        // System.out.println("Edit item at index "+
        // ((currentPage*maxPageItems)+index));
    }

    void displayEditItemPane(TableRowable toBeEdited) {

        String addFormViewPath = currentNode.get("form_view").asText();
        String addControllerPath = currentNode.get("form_controller").asText();
        Class[] classArgs = { getCurrentClass(), ArrayList.class, ArrayList.class, Function.class };
        Initializable addFormController = Tools.getControllerFromPath(addControllerPath, classArgs,
                toBeEdited, formDisplayDropDownLists, formDropDownLists, getRefreshFunction());
        Tools.showModal(addFormController, addFormViewPath, Tools.getStageFromNode(basePane));
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
        // if (itemsAreNotEmpty()) {
            removeLoadingIcon();
            setMaxPages();
            setDisplayItems();
            refreshRows();
            refreshMaxPages();
            refreshCurrentPage();
        // } else {
        //     showLoadingIcon();
        // }
    }

    void setDisplayItems() {
        System.out.println("items: " + items);
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
        Function onDelete = getDeleteFunctionFromIndex(paneIndex);
        // Stage primaryStage = getPrimaryStage();
        deleteIconPane.setPrefHeight(Config.iconHeight);
        deleteIconPane.setMinHeight(Config.iconHeight);
        deleteIconPane.setMaxHeight(Config.iconHeight);
        deleteIconPane.getChildren().add(Tools.getPaneFromControllerAndFxmlPath(
                new DeleteIconController(getPrimaryStage, onDelete, getRefreshFunction()),
                "/components/deleteIcon.fxml"));
        deleteIconPane.setCursor(Cursor.HAND);
        deleteIconPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                System.out.println("confirm deletion of item at index " + paneIndex);
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
        // items.add(
        //         new Student("kkk", "Luna", "Class 1", "20000", 1, "Luna parent", "654370303", "true", "15000", "0", "0",
        //                 "0", "0"));

        // ArrayList<String> classes = new ArrayList<>();
        // classes.add("class 1");
        // classes.add("class 2");
        // classes.add("class 3");
        // classes.add("class 4");
        // classes.add("class 5");
        // classes.add("class 6");
        // formDropDownLists.add(classes);
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
        if (cachedBasePane == null) {
            cachedBasePane = basePane.getChildren().get(0);
        }
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
                                    HttpResponse<String> numberOfItemsResponse = RequestManager
                                            .fetchNumberOfItems(baseUrl, searchUrl)
                                            .get();
                                    Response numberOfItemsCustomResponse = Tools
                                            .getCustomResponseFromResponse(numberOfItemsResponse);

                                    if (numberOfItemsCustomResponse.getCode() == 200) {

                                        updateProgress(Config.numItemsRequestLoadingIncrement + 0.0, 100);
                                        int numberOfItems = getNumberOfItemsFromResponse(
                                                numberOfItemsCustomResponse);
                                        System.out.println(numberOfItems);
                                        ArrayList<Response> itemsResponses = RequestManager.fetchItems(baseUrl,
                                                searchUrl,
                                                incrementProgress, numberOfItems,
                                                computeItemRequestLoadingIncrement(numberOfItems));
                                        initializeItems();
                                        for (Response itemResponse : itemsResponses) {
                                            addItemFromResponse(itemResponse);
                                        }

                                        HttpResponse<String> dropDownListsResponse = RequestManager
                                                .fetchDropDownLists(baseUrl, incrementProgress,
                                                        Config.dropDownRequestLoadingIncrement)
                                                .get();

                                        Response dropDownListCustomResponse = Tools
                                                .getCustomResponseFromResponse(dropDownListsResponse);

                                        if (dropDownListCustomResponse.getCode() == 200) {
                                            addDropDownItemsFromResponse(dropDownListCustomResponse);
                                            incrementProgress.apply(Config.dropDownRequestLoadingIncrement);
                                        } else {
                                            items = null;
                                            updateProgress(100, 100);
                                        }
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

    void bindServiceWithDisplay(Service service) {
        service.progressProperty().addListener(new ChangeListener<Object>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Object> arg0, Object oldValue,
                    Object newValue) {
                if (Math.abs(((Double) newValue - 1.0)) < Math.pow(10, -15)) {
                    if (items != null) {
                        refreshDisplayItems();
                        setSearchBarDropDownItems();
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
        Function<NullType, NullType> refresh = new Function<NullType, NullType>() {
            @Override
            public NullType apply(NullType t) {
                refresh();
                return null;
            }
        };
        return Tools.getPaneFromControllerAndFxmlPath(new ErrorPaneController(refresh), "/sections/ErrorPane.fxml");
    }

    void initializeTitle() {
        title.setText(currentNode.get("title").asText());
    }

    void initializeAddButton() {
        addButton.setText(currentNode.get("add_button_title").asText());
        addButton.setOnAction(getAddActionFromCurrentNode());
    }

    EventHandler<ActionEvent> getAddActionFromCurrentNode() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                String addFormViewPath = currentNode.get("form_view").asText();
                String addControllerPath = currentNode.get("form_controller").asText();
                Class[] classArgs = { ArrayList.class, ArrayList.class, Function.class };
                Initializable addFormController = Tools.getControllerFromPath(addControllerPath, classArgs,
                        formDisplayDropDownLists, formDropDownLists, getRefreshFunction());
                Tools.showModal(addFormController, addFormViewPath, Tools.getStageFromNode(addButton));
            }
        };
    }

    void addItemFromResponse(Response response) {
        items.add(getItemFromNode.apply(response.getData()));
    }

    void addDropDownItemsFromResponse(Response response) {
        formDropDownLists = new ArrayList();
        formDisplayDropDownLists = new ArrayList<>();
        int index = 0;
        System.out.println(response.getData());
        for (JsonNode dropDownItemsList : response.getData()) {

            ArrayList<JsonNode> dropDownItems = new ArrayList<>();
            ArrayList<String> displayDropDownItems = new ArrayList<>();

            for (JsonNode dropDownItem : dropDownItemsList) {
                dropDownItems.add(dropDownItem);
                displayDropDownItems.add(getDisplayDropDownFromItemAndIndex(dropDownItem, index));
            }
            formDropDownLists.add(dropDownItems);
            formDisplayDropDownLists.add(displayDropDownItems);
            index++;
        }
        System.out.println(formDisplayDropDownLists);
    }

    Function getRefreshFunction() {
        return new Function<NullType, NullType>() {
            @Override
            public NullType apply(NullType t) {
                System.out.println("refresh");
                try {
                    Platform.runLater(() -> {
                        refresh();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
    }

    String getDisplayDropDownFromItemAndIndex(JsonNode dropDownItem, int index) {
        return dropDownItem.get(formDropDownAttributes.get(index)).asText();
    }

    int getNumberOfItemsFromResponse(Response numOfItemsresponse) {
        return numOfItemsresponse.getData().get("count").asInt();
    }

    double computeItemRequestLoadingIncrement(int numberOfItems) {
        double itemsRequestLoadingIncrement = 100
                - (Config.dropDownRequestLoadingIncrement + Config.numItemsRequestLoadingIncrement);
        if (numberOfItems != 0) {
            return itemsRequestLoadingIncrement / numberOfItems;
        } else {
            return itemsRequestLoadingIncrement;
        }

    }

    Function getDeleteFunctionFromIndex(int itemIndex) {
        return new Function<NullType, CompletableFuture<HttpResponse<String>>>() {
            @Override
            public CompletableFuture<HttpResponse<String>> apply(NullType t) {
                String itemId = getItemIdByIndex(itemIndex);
                return RequestManager.deleteItem(baseUrl, itemId);
            }
        };
    }

    String getItemIdByIndex(int itemIndex) {
        TableRowable item = items.get(itemIndex);
        return item.getId();
    }

    void refresh() {
        items = null;

        showLoadingIcon();
        fetchItems(fetchItemsService);
        initializeTable();

        bindTablePaneWithNumberOfDisplayItems();
        bindTableRowsWithEditAction();

        table.refresh();
    }

    void initializeItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
    }

    void clearBasePane() {
        basePane.getChildren().clear();
    }

    void bindSearchBarWithSearchUrl() {
        ((ISearchBar) searchBarController).setChangeUrl(getChangeUrlFunction());
    }

    Function<String, NullType> getChangeUrlFunction() {
        return new Function<String, NullType>() {
            @Override
            public NullType apply(String newSearchUrl) {
                setSearchUrl(newSearchUrl);
                return null;
            }
        };
    }

    void setSearchUrl(String newSearchUrl) {
        System.out.println("new search url :" + newSearchUrl);
        this.searchUrl = newSearchUrl;
        refresh();
    }

    void setSearchBarDropDownItems() {
        ((ISearchBar) searchBarController).setDropDownItems(formDisplayDropDownLists);
    }

    void initializeSearchUrl() {
        searchUrl = "";
    }

    Class getCurrentClass(){
        try {
            return Class.forName(currentNode.get("class_path").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
