package com.sms.models;

import com.sms.interfaces.TableRowable;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;

public class CustomTableCell extends TableCell<TableRowable, String> {
        private final Label label;
    
        public CustomTableCell() {
            super();
            label = new Label();
            label.setStyle("-fx-font-size: 14px;");
            label.setWrapText(true);
            setGraphic(label);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                label.setText("");
            } else {
                label.setText(item);
            }
        }
    
}
