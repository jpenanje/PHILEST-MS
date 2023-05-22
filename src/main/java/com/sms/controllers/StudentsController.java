package com.sms.controllers;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.interfaces.TableRowable;
import com.sms.models.CustomTableCell;
import com.sms.models.RowTypes;
import com.sms.models.Student;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class StudentsController extends TableSectionController {

    public StudentsController() {
        super(null, null, null, null, null, null);
    }

    public StudentsController(JsonNode node) {
        super(new SearchStudentController(), "/sections/StudentSearch.fxml", node, itemFromNodeFunction(), "students/", getAttributes());
        super.setColumns(getColumns());
        // requires the search controller and the fxml path
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        super.initialize(arg0, arg1);
    }

    ArrayList<TableColumn<TableRowable, String>> getColumns() {
        ArrayList<TableColumn<TableRowable, String>> columns = new ArrayList<>();
        // { "ID", "Pupil's Name", "Class", "Parent's Name", "Phone Number",
        // "Registered",
        // "Installement 1", "Installement 2", "Installement 3", "Installement 4",
        // "Installement 5", "Total paid",
        // "Fees owed" };
        TableColumn<TableRowable, String> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("id"));
        column1.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.ID);
            }
        });

        TableColumn<TableRowable, String> column2 = new TableColumn<>("Pupil's Name");
        column2.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("pupilName"));
        column2.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.TEXT);
            }
        });

        TableColumn<TableRowable, String> column3 = new TableColumn<>("Class");
        column3.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("studentClass"));
        column3.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.TEXT);
            }
        });

        TableColumn<TableRowable, String> column4 = new TableColumn<>("Parent's Name");
        column4.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("parentName"));
        column4.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.TEXT);
            }
        });

        TableColumn<TableRowable, String> column5 = new TableColumn<>("Phone Number");
        column5.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("phoneNumber"));
        column5.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.TEXT);
            }
        });

        TableColumn<TableRowable, String> column6 = new TableColumn<>("Registered");
        column6.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("registered"));
        column6.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.TICK);
            }
        });

        TableColumn<TableRowable, String> column7 = new TableColumn<>("Installement 1");
        column7.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("installment1"));
        column7.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.VALUE);
            }
        });

        TableColumn<TableRowable, String> column8 = new TableColumn<>("Installement 2");
        column8.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("installment2"));
        column8.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.VALUE);
            }
        });

        TableColumn<TableRowable, String> column9 = new TableColumn<>("Installement 3");
        column9.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("installment3"));
        column9.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.VALUE);
            }
        });

        TableColumn<TableRowable, String> column10 = new TableColumn<>("Installement 4");
        column10.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("installment4"));
        column10.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.VALUE);
            }
        });

        TableColumn<TableRowable, String> column11 = new TableColumn<>("Installement 5");
        column11.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("installment5"));
        column11.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.VALUE);
            }
        });

        TableColumn<TableRowable, String> column12 = new TableColumn<>("Total paid");
        column12.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("totalPaid"));
        column12.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.VALUE);
            }
        });

        TableColumn<TableRowable, String> column13 = new TableColumn<>("Fees owed");
        column13.setCellValueFactory(new PropertyValueFactory<TableRowable, String>("feesOwed"));
        column13.setCellFactory(new Callback<TableColumn<TableRowable, String>, TableCell<TableRowable, String>>() {
            @Override
            public TableCell<TableRowable, String> call(TableColumn<TableRowable, String> column) {
                return new CustomTableCell(RowTypes.TEXT);
            }
        });

        columns.addAll(Arrays.asList(column1, column2, column3, column4, column5, column6, column7, column8, column9,
                column10, column11, column12, column13));

        return columns;
    }

    static Function<JsonNode, TableRowable> itemFromNodeFunction() {
        return new Function<JsonNode, TableRowable>() {
            @Override
            public TableRowable apply(JsonNode node) {
                try {
                    System.out.println(node);
                    JsonNode studentNode = node.get(0);
                    String pupilName = studentNode.get("full_name").asText();
                    String id = studentNode.get("id").asInt() + "";
                    String parentName = studentNode.get("parent_name").asText();
                    String phoneNumber = studentNode.get("parent_phone").asText();
                    String studentClass = studentNode.get("class").get("name").asText();
                    String classFee = studentNode.get("class").get("fee").asText();
                    String registered = "false";
                    String installment1 = "0";
                    String installment2 = "0";
                    String installment3 = "0";
                    String installment4 = "0";
                    String installment5 = "0";

                    int installementIndex = 1;
                    if (studentNode.get("cash_ins") != null) {
                        for (JsonNode cashInNode : studentNode.get("cash_ins")) {
                            if (cashInNode.get("purpose").asText().equals("registration")) {
                                registered = "true";
                            } 
                            else if (cashInNode.get("purpose").asText().equals("installement")
                                    && installementIndex < 6) {
                                switch (installementIndex) {
                                    case 1: {
                                        installment1 = cashInNode.get("amount").asText();
                                        installementIndex++;
                                        break;
                                    }
                                    case 2: {
                                        installment2 = cashInNode.get("amount").asText();
                                        installementIndex++;
                                        break;
                                    }
                                    case 3: {
                                        installment3 = cashInNode.get("amount").asText();
                                        installementIndex++;
                                        break;
                                    }
                                    case 4: {
                                        installment4 = cashInNode.get("amount").asText();
                                        installementIndex++;
                                        break;
                                    }
                                    case 5: {
                                        installment5 = cashInNode.get("amount").asText();
                                        installementIndex++;
                                        break;
                                    }
                                }

                            }
                        }
                    }

                    Student toBeReturned = new Student(id, pupilName, studentClass, classFee, parentName,
                    phoneNumber, registered, installment1, installment2, installment3,
                    installment4, installment5);
                    return toBeReturned;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new Student();
            }
        };
    }


    static ArrayList<String> getAttributes(){
        ArrayList<String> attributes = new ArrayList<>();
        attributes.add("name");
        return attributes;
    }
}
