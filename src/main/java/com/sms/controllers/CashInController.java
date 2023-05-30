package com.sms.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.interfaces.TableRowable;
import com.sms.models.CashIn;
import com.sms.models.CustomTableCell;
import com.sms.models.RowTypes;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

// Controller for manageing the CashIn table. It extends the Table Section Controller 
public class CashInController extends TableSectionController {

    // no arguement constructor, even if not used, it is required when loading this
    // controller simply from it's path
    public CashInController() {
        super(null, null, null, null, null, null);
    }

    // constructor for cashIn from the jsonNode containing data and meta data for
    // the table
    public CashInController(JsonNode node) {
        super(new CashInSearchController(), "/sections/CashInSearch.fxml", node, itemFromNodeFunction(), "cashin/",
                getAttributes());
        super.setColumns(getColumns());
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        super.initialize(arg0, arg1);
    }

    // returns the meta description of how the data will be represented on the table
    ArrayList<TableColumn<TableRowable, String>> getColumns() {
        ArrayList<TableColumn<TableRowable, String>> columns = new ArrayList<>();
        TableColumn<TableRowable, String> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("id"));
        column1.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.ID);
            }
        });

        TableColumn<TableRowable, String> column2 = new TableColumn<>("Student");
        column2.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("studentName"));
        column2.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.TEXT);
            }
        });

        TableColumn<TableRowable, String> column3 = new TableColumn<>("Purpose");
        column3.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("purpose"));
        column3.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.TEXT);
            }
        });

        TableColumn<TableRowable, String> column4 = new TableColumn<>("Amount");
        column4.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("amount"));
        column4.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.TEXT);
            }
        });

        TableColumn<TableRowable, String> column5 = new TableColumn<>("Academic Year");
        column5.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("academicYear"));
        column5.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.TEXT);
            }
        });

        TableColumn<TableRowable, String> column6 = new TableColumn<>("Date");
        column6.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("date"));
        column6.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.DATE);
            }
        });

        columns.addAll(Arrays.asList(column1, column2, column3, column4, column5, column6));

        makeColumnsNotSortable(columns);
        return columns;
    }

    // returns a function which describes how a row item is to be constructed
    // from a given node in the data
    static Function<JsonNode, TableRowable> itemFromNodeFunction() {
        return new Function<JsonNode, TableRowable>() {
            @Override
            public TableRowable apply(JsonNode node) {
                try {
                    System.out.println(node);
                    JsonNode itemNode = node.get(0);
                    String id = itemNode.get("id").asInt() + "";
                    int studentId = itemNode.get("student_data").get("id").asInt(0);
                    String studentName = itemNode.get("student_data").get("full_name").asText();
                    String purpose = itemNode.get("purpose").asText();
                    String academicYear = itemNode.get("academic_year").asText();
                    String amount = itemNode.get("amount").asText();
                    String date = itemNode.get("date").asText();
                    CashIn toBeReturned = new CashIn(id, studentId, studentName, purpose, academicYear, amount, date);
                    return toBeReturned;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("default cash in");
                return new CashIn();
            }
        };
    }

    // Returns the attributes of the lists in the data to be used in
    // form dropdowns
    static ArrayList<String> getAttributes() {
        ArrayList<String> attributes = new ArrayList<>();
        attributes.add("full_name");
        attributes.add("year");
        attributes.add("purpose");
        return attributes;
    }

    // Makes the columns not sortable using the default arrows for sorting each column
    static void makeColumnsNotSortable(ArrayList<TableColumn<TableRowable, String>> columns) {
        for (TableColumn<TableRowable, String> column : columns) {
            column.setSortable(false);
            column.setMaxWidth(5000);
        }
    }
}
