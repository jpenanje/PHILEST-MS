package com.sms.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sms.interfaces.TableRowable;
import com.sms.tools.Tools;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class TableSectionController implements Initializable {

    int currentPage = 0;
    int maxPages = 0;
    int maxPageItems = 10;
    Initializable searchBarController;
    String searchBarFXMLPath;

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<TableRowable> items = new ArrayList<>();
    ObservableList<TableRowable> displayItems = FXCollections.observableArrayList(new ArrayList());

    public TableSectionController(Initializable searchBarController, String searchBarFXMLPath) {
        super();
        this.searchBarController = searchBarController;
        this.searchBarFXMLPath = searchBarFXMLPath;
    }

    @FXML
    StackPane basePane;

    @FXML
    VBox indicesPane;

    @FXML
    VBox deleteIconsPane;

    @FXML
    void nextPage(MouseEvent event) {
        if (currentPage < maxPages) {
            currentPage++;
            setDisplayItems();
            refreshRows();
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
    void previousPage(MouseEvent event) {
        if (currentPage > 0) {
            currentPage--;
            setDisplayItems();
            refreshRows();
        } else {
            showNoPreviousPageMessage();
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        showLoadingIcon();
        fetchItems();
        addListenerToDisplayItems();
        initializeTable();
        loadSearchPane();
    }

    void addListenerToDisplayItems() {
        displayItems.addListener(new ListChangeListener<TableRowable>() {
            @Override
            public void onChanged(Change<? extends TableRowable> newValue) {
                if (itemsAreNotEmpty()) {
                    removeLoadingIcon();
                    setMaxPages();
                    setDisplayItems();
                    refreshRows();
                    refreshMaxPages();
                } else {
                    showLoadingIcon();
                }
            }
        });
    }

    void setDisplayItems() {

        int startIndex = (currentPage * maxPageItems) - 1;

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
        for (TableRowable item : displayItems) {
            table.getItems().add(currentPage, item);
        }
        refreshIndices();
        refreshDeleteIcons();
    }

    void initializeTable() {
        table.setRowFactory(new Callback<TableView<TableRowable>,TableRow<TableRowable>>() {
            public TableRow<TableRowable> call(TableView<TableRowable>  arg0) {
                // requires the specificaiton of the method to change the table rowable to a tablerow
                return null;
            };
        });
        ObservableList<TableColumn<TableRowable, ?>> columns = table.getColumns();
        int index = 0;
        for (TableColumn<TableRowable, ?> column : columns) {
            column.setText(titles.get(index));
            index++;
        }
    }

    void refreshIndices() {
        indicesPane.getChildren().clear();
        for (int i = 0; i < displayItems.size(); i++) {
            int paneIndex = currentPage * maxPageItems + i + 1;
            indicesPane.getChildren().add(getIndexPane(paneIndex));
        }
    }

    StackPane getIndexPane(int paneIndex) {
        return new StackPane(new Text("" + paneIndex));
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
    }

    StackPane getDeleteIconPane(final int paneIndex) {
        StackPane deleteIconPane = new StackPane(new Text("" + paneIndex));
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

    void fetchItems() {
        System.out.println("fetched items");
    }

    void showLastPageMessage() {
        System.out.println("This is the last page");
    }

    void showNoPreviousPageMessage() {
        System.out.println("This is the first page you can't go back");
    }

    void showLoadingIcon() {
        System.out.println("Loading icon...");
    }

    void removeLoadingIcon() {
        System.out.println("removed loading icon...");
    }

    void loadSearchPane() {

        searchPane.getChildren().add(Tools.getPaneFromControllerAndFxmlPath(searchBarController, searchBarFXMLPath));

    }
}
