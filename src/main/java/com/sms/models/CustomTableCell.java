package com.sms.models;

import com.sms.controllers.RowValueWithLineController;
import com.sms.interfaces.TableRowable;
import com.sms.tools.Config;
import com.sms.tools.Tools;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class CustomTableCell extends TableCell<TableRowable, String> {
    private final Label label;
    private final StackPane pane = new StackPane();
    private RowTypes type;

    public CustomTableCell(RowTypes type) {
        super();
        this.type = type;
        pane.setPadding(new Insets(0, 30, 0, 30));
        label = new Label();
        label.setStyle("-fx-font-size: 12.5px;");
        label.setTextFill(Color.BLACK);
        label.setWrapText(true);
        pane.getChildren().add(label);
        pane.setPrefHeight(Config.tableRowHeight);
        pane.setMinHeight(Config.tableRowHeight);
        pane.setMaxHeight(Config.tableRowHeight);

        pane.setPrefWidth(USE_COMPUTED_SIZE);
        pane.setMinWidth(USE_COMPUTED_SIZE);
        pane.setMaxWidth(USE_COMPUTED_SIZE);

        setGraphic(pane);
        // setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        switch (type) {
            case ID: {
                loadIDItem(item, empty);
                break;
            }
            case TICK: {
                loadTickItem(item, empty);
                break;
            }
            case VALUE: {
                loadValueItem(item, empty);
                break;
            }
            case DATE: {
                loadDateItem(item, empty);
                break;
            }
            default: {
                loadTextItem(item, empty);
            }
        }

    }

    void loadTextItem(String item, boolean empty) {
        pane.getChildren().clear();
        pane.getChildren().add(label);
        if (item == null || empty) {
            label.setText("");
        } else {
            label.setText(item);
        }
    }

    void loadDateItem(String item, boolean empty) {
        pane.getChildren().clear();
        pane.getChildren().add(label);
        if (item == null || empty) {
            label.setText("");
        } else {
            String text = Tools.getDateTextFromUtcStr(item);
            label.setText(text);
        }
    }

    void loadIDItem(String item, boolean empty) {
        pane.getChildren().clear();
        pane.getChildren().add(label);
        if (item == null || empty) {
            label.setText("");
        } else {
            label.setText(item);
        }
        label.setTextFill(Color.valueOf("#A6A6A6"));
    }

    void loadTickItem(String item, boolean empty) {
        pane.getChildren().clear();
        if (item == null || empty) {
            pane.getChildren().add(new StackPane());
        } else {
            if (item.equals("true")) {
                pane.getChildren().add(getTickIcon());
            } else {
                pane.getChildren().add(getCrossIcon());
            }
        }
    }

    StackPane getCrossIcon() {
        try {
            return FXMLLoader.load(getClass().getResource("/components/CrossIcon.fxml"), null);
        } catch (Exception e) {
            System.out.println("Could not load the tick icon");
            return null;
        }
    }

    StackPane getTickIcon() {
        try {
            return FXMLLoader.load(getClass().getResource("/components/TickIcon.fxml"), null);
        } catch (Exception e) {
            System.out.println("Could not load the tick icon");
            return null;
        }
    }

    void loadValueItem(String item, boolean empty) {
        pane.getChildren().clear();
        if (item == null || empty) {
            label.setText("");
        } else {
            Initializable controller = new RowValueWithLineController(item);
            pane.getChildren()
                    .add(Tools.getPaneFromControllerAndFxmlPath(controller, "/components/RowValueWithLine.fxml"));
        }
    }

}
