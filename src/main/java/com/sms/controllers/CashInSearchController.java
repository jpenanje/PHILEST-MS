package com.sms.controllers;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.sms.interfaces.ISearchBar;
import com.sms.tools.Tools;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

// A controller for the search bar of the section CashIn
public class CashInSearchController implements Initializable, ISearchBar{


    @FXML
    private MenuButton year;

    @FXML
    private MenuButton searchPurpose;

    @FXML
    private TextField searchId;

    @FXML
    private TextField searchStudent;

    @FXML
    private TextField searchAmount;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;


    Function<String, NullType> changeUrl;

    ArrayList<String> classDropDownItems;

    ArrayList<String> yearDropDownItems;

    ArrayList<String> registeredDropDownItems;

    ArrayList<String> owingDropDownItems;

    // filters the cashIn table using the current attributes in all the fields
    @FXML
    void search(ActionEvent event) {
        changeUrl.apply(getCurrentUrl());
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initDateFields();
    }

    // sets the function for changing the url used to fetch items for 
    // the cashIn table
    public void setChangeUrl(Function<String, NullType> changeUrl){
        this.changeUrl = changeUrl;
    }

    // sets the lists of items to be used in drop down fields
    @Override
    public void setDropDownItems(ArrayList<ArrayList> dropDownItems) {
        System.out.println(dropDownItems);
        this.classDropDownItems = (ArrayList<String>)(dropDownItems.get(2)).clone();
        this.yearDropDownItems = (ArrayList<String>)(dropDownItems.get(1)).clone();
        initSearchClass();
        initSearchYear();
    }

    // initializes the purpose field as required
    void initSearchClass(){
        searchPurpose.getItems().clear();
        classDropDownItems.add(0,"Purpose");
        Tools.addDropDownItemsFromFieldAndItems(searchPurpose, classDropDownItems);
    }

    // initializes the year field as required
    void initSearchYear(){
        year.getItems().clear();
        yearDropDownItems.add(0,"Year");
        Tools.addDropDownItemsFromFieldAndItems(year, yearDropDownItems);
    }

    // gets the current search url from the search fields
    String getCurrentUrl(){

        String idParam = getParamFromFieldAndAttribute(searchId, "id");

        String nameParam = getParamFromFieldAndAttribute(searchStudent, "student");

        String classParam = getParamFromFieldAndAttribute(searchPurpose, "purpose");

        String parentNameParam = getParamFromFieldAndAttribute(searchAmount, "amount");

        String startDateParam = getParamFromFieldAndAttribute(startDate, "start_date");

        String endDateParam = getParamFromFieldAndAttribute(endDate, "end_date");

        String yearParam = getParamFromFieldAndAttribute(year, "year");


        String currentUrl = "?";
        if(idParam.length() > 0){
            currentUrl += idParam + "&";
        }
        if(nameParam.length() > 0){
            currentUrl += nameParam + "&";
        }
        if(classParam.length() > 0 && !classDropDownItems.get(0).equals(searchPurpose.getText())){
            currentUrl += classParam + "&";
        }
        if(yearParam.length() > 0 && !yearDropDownItems.get(0).equals(year.getText())){
            currentUrl += yearParam + "&";
        }
        if(parentNameParam.length() > 0){
            currentUrl += parentNameParam + "&";
        }

        if(startDateParam.length() > 0){
            currentUrl += startDateParam + "&";
        }

        if(endDateParam.length() > 0){
            currentUrl += endDateParam + "&";
        }

        if(currentUrl.length() > 1){
            return currentUrl.substring(0, currentUrl.length()-1);
        }
        else{
            return "";
        }
    }

    // returns a valid encoded url parameter to be appended to the
    // search url
    String getParamFromFieldAndAttribute(TextField field, String attribute){
        String param = "";
        if(field.getText().length() > 0 ){
            param = attribute+ "=" + encodeUrlParameter(field.getText());
        }
        return param;
    }

    // overloads the getParamFromFieldAndAttribute for menubutton fields
    String getParamFromFieldAndAttribute(MenuButton field, String attribute){
        String param = "";
        if(field.getText().length() > 0 ){
            param = attribute+ "=" + encodeUrlParameter(field.getText());
        }
        return param;
    }

    // overloads the getParamFromFieldAndAttribute for date picker fields
    String getParamFromFieldAndAttribute(DatePicker field, String attribute){
        String param = "";
        if(field.getValue() != null ){
            String utcDate = Tools.getUtcDateStringFromLocalDate(field.getValue());
            param = attribute+ "=" + encodeUrlParameter(utcDate);
        }
        return param;
    }

    // encodes the url paramter to escape special characters
    public static String encodeUrlParameter(String text) {
        try {
            return URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //  initializes the date fields formats as DD/MM/YYYY
    void initDateFields(){
        StringConverter<LocalDate> converter = Tools.getDateConverter();

        startDate.setConverter(converter);
        endDate.setConverter(converter);
    }
}
