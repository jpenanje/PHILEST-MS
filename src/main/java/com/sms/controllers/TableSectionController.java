package com.sms.controllers;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.interfaces.ISearchBar;
import com.sms.interfaces.TableRowable;
import com.sms.models.Response;
import com.sms.models.Student;
import com.sms.tools.Config;
import com.sms.tools.RequestManager;
import com.sms.tools.Tools;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

// The generic table which is inherited by any class implementing
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

    // constructor taking the search bar, current node, url and form lists
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

    // moves to the next page or not if the current page is the last page
    @FXML
    void nextPage(ActionEvent event) {
        if (currentPage < maxPages - 1) {
            currentPage++;
            refreshDisplayItems();
        } else {
            showLastPageMessage();
        }
    }

    @FXML
    TableView<TableRowable> table;

    @FXML
    StackPane searchPane;

    // moves to previous page or not if the current page is the first page
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
    }

    // double clicking a table row opens a form
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

    // opens a popup for editing an item based
    void editItemByIndex(int index) {
        int actualIndex = (currentPage * maxPageItems) + index;
        TableRowable toBeEdited = items.get(actualIndex);
        displayEditItemPane(toBeEdited);
    }

    // displays edit item dialog for given data
    void displayEditItemPane(TableRowable toBeEdited) {
        String addFormViewPath = currentNode.get("form_view").asText();
        String addControllerPath = currentNode.get("form_controller").asText();
        Class[] classArgs = { getCurrentClass(), ArrayList.class, ArrayList.class, Function.class };
        Initializable addFormController = Tools.getControllerFromPath(addControllerPath, classArgs,
                toBeEdited, formDisplayDropDownLists, formDropDownLists, getRefreshFunction());
        Tools.showModal(addFormController, addFormViewPath, Tools.getStageFromNode(basePane));
    }

    // ensures that when resizing the table pane, the number of items
    // fits the size of the screen
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

    // refreshes the items displayed with the current url
    void refreshDisplayItems() {
        removeLoadingIcon();
        setMaxPages();
        setDisplayItems();
        refreshRows();
        refreshMaxPages();
        refreshCurrentPage();
    }

    // sets the display items based on the number of items to be displayed on a
    // page.
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

    // set max pages based on items
    void setMaxPages() {
        maxPages = items.size() / maxPageItems;
    }

    // adds displayItems into table
    void refreshRows() {
        
        System.out.println("refresh rows");
        table.getItems().clear();
        
        for (TableRowable item : displayItems) {
            table.getItems().add(item);
        }
        refreshIndices();
        refreshDeleteIcons();
    }

    // initialize columns in table
    void initializeTable() {
        table.getColumns().clear();
        table.getColumns().addAll(columns);
    }

    // refreshes the indices on the left based on the number of items displayed
    void refreshIndices() {
        System.out.println("refresh indices");
        indicesPane.getChildren().clear();
        for (int i = 0; i < displayItems.size(); i++) {
            int paneIndex = currentPage * maxPageItems + i + 1;
            indicesPane.getChildren().add(getIndexPane(paneIndex));
        }
    }

    // returns an index pane
    StackPane getIndexPane(int paneIndex) {
        StackPane pane = new StackPane();
        pane.setPrefWidth(10);
        pane.setPrefHeight(Config.iconHeight);
        Text content = new Text("" + paneIndex);
        content.setFill(Color.valueOf("#A6A6A6"));
        pane.getChildren().add(content);
        pane.setStyle("-fx-background-color:transparent;");
        return pane;
    }

    // refreshes the delete icons
    void refreshDeleteIcons() {
        deleteIconsPane.getChildren().clear();
        for (int i = 0; i < displayItems.size(); i++) {
            int paneIndex = currentPage * maxPageItems + i;
            deleteIconsPane.getChildren().add(getDeleteIconPane(paneIndex));
        }
    }

    // refreshes the max number of pages based on the the number of items
    void refreshMaxPages() {
        maxPages = (int) Math.ceil((double) items.size() / maxPageItems);
        totalItems.setText("" + items.size());
        itemsRange.setText(getItemRangeStr());
    }

    // returns the range of values for the pages
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

    // returns the delete icon pane;
    StackPane getDeleteIconPane(final int paneIndex) {
        StackPane deleteIconPane = new StackPane();
        Function<NullType, Stage> getPrimaryStage = new Function<NullType, Stage>() {
            @Override
            public Stage apply(NullType t) {
                return getPrimaryStage();
            }
        };
        Function onDelete = getDeleteFunctionFromIndex(paneIndex);
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

    // returns if an item is empty
    boolean itemsAreNotEmpty() {
        return !items.isEmpty();
    }

    // notifies last page reached
    void showLastPageMessage() {
        System.out.println("This is the last page");
    }

    // notifies currently in first page
    void showNoPreviousPageMessage() {
        System.out.println("This is the first page you can't go back");
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

    // removes the loading icon
    void removeLoadingIcon() {
        System.out.println("removed loading icon...");
    }

    // sets columns
    void setColumns(ArrayList columns) {
        this.columns = columns;
    }

    // loads the search pane
    void loadSearchPane() {
        searchPane.getChildren().add(Tools.getPaneFromControllerAndFxmlPath(searchBarController, searchBarFXMLPath));
    }

    // returns the primary stage
    Stage getPrimaryStage() {
        System.out.println(deleteIconsPane.getScene());
        return (Stage) ((deleteIconsPane.getScene()).getWindow());
    }

    // refreshes the current page
    void refreshCurrentPage() {
        pageNumber.setText("" + (currentPage + 1));
    }

    // returns the loading icon
    Node getLoadingIcon(Service service) {
        return Tools.getLoadingIcon(service);
    }

    // returns the service to fetch the items of the table
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

    // ensures that te progression is represented on design
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

    // loads the cached pane
    void loadCachedPane() {
        basePane.getChildren().clear();
        basePane.getChildren().add(cachedBasePane);
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

    // sets the title from the node
    void initializeTitle() {
        title.setText(currentNode.get("title").asText());
    }

    // initializes the add button text and action
    void initializeAddButton() {
        addButton.setText(currentNode.get("add_button_title").asText());
        addButton.setOnAction(getAddActionFromCurrentNode());
    }

    // returns the add action retrieved from the current node
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

    // adds an item from a response
    void addItemFromResponse(Response response) {
        items.add(getItemFromNode.apply(response.getData()));
    }

    // adds drop down items from keys and drop down objects
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

    // returns a function to refresh the table
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

    // returns the drop down item
    String getDisplayDropDownFromItemAndIndex(JsonNode dropDownItem, int index) {
        return dropDownItem.get(formDropDownAttributes.get(index)).asText();
    }

    // returns the number of items from a response
    int getNumberOfItemsFromResponse(Response numOfItemsresponse) {
        return numOfItemsresponse.getData().get("count").asInt();
    }

    // Computes the increment of loading icon based on the number of items
    double computeItemRequestLoadingIncrement(int numberOfItems) {
        double itemsRequestLoadingIncrement = 100
                - (Config.dropDownRequestLoadingIncrement + Config.numItemsRequestLoadingIncrement);
        if (numberOfItems != 0) {
            return itemsRequestLoadingIncrement / numberOfItems;
        } else {
            return itemsRequestLoadingIncrement;
        }

    }

    // returns the delete icon for a given index
    Function getDeleteFunctionFromIndex(int itemIndex) {
        return new Function<NullType, CompletableFuture<HttpResponse<String>>>() {
            @Override
            public CompletableFuture<HttpResponse<String>> apply(NullType t) {
                String itemId = getItemIdByIndex(itemIndex);
                return RequestManager.deleteItem(baseUrl, itemId);
            }
        };
    }

    // returns an item by index
    String getItemIdByIndex(int itemIndex) {
        TableRowable item = items.get(itemIndex);
        return item.getId();
    }

    // refreshes the table
    void refresh() {
        items = null;

        showLoadingIcon();
        fetchItems(fetchItemsService);
        initializeTable();

        bindTablePaneWithNumberOfDisplayItems();
        bindTableRowsWithEditAction();

        table.refresh();
    }

    // set the items to 
    void initializeItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
    }

    // clears the base pane
    void clearBasePane() {
        basePane.getChildren().clear();
    }

    // ensures that searching a new url refreshes the table
    void bindSearchBarWithSearchUrl() {
        ((ISearchBar) searchBarController).setChangeUrl(getChangeUrlFunction());
    }

    // returns the function for seting the search url
    Function<String, NullType> getChangeUrlFunction() {
        return new Function<String, NullType>() {
            @Override
            public NullType apply(String newSearchUrl) {
                setSearchUrl(newSearchUrl);
                return null;
            }
        };
    }

    // sets the search url
    void setSearchUrl(String newSearchUrl) {
        System.out.println("new search url :" + newSearchUrl);
        this.searchUrl = newSearchUrl;
        refresh();
    }

    // sets the search bar drop down items
    void setSearchBarDropDownItems() {
        ((ISearchBar) searchBarController).setDropDownItems(formDisplayDropDownLists);
    }

    // sets the initial url for the variable search url
    void initializeSearchUrl() {
        searchUrl = "";
    }

    // returns the current class for the table objects
    Class getCurrentClass() {
        try {
            return Class.forName(currentNode.get("class_path").asText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
