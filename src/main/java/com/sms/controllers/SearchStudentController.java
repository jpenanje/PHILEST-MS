package com.sms.controllers;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.sms.interfaces.ISearchBar;
import com.sms.tools.Tools;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;

// A controller for the search bar of the section Student
public class SearchStudentController implements Initializable, ISearchBar{

    @FXML
    private MenuButton owing;

    @FXML
    private MenuButton year;

    @FXML
    private MenuButton registered;

    @FXML
    private MenuButton searchClass;

    @FXML
    private TextField searchId;

    @FXML
    private TextField searchName;

    @FXML
    private TextField searchParent;

    @FXML
    private TextField searchPhone;

    Function<String, NullType> changeUrl;

    ArrayList<String> classDropDownItems;

    ArrayList<String> yearDropDownItems;

    ArrayList<String> registeredDropDownItems;

    ArrayList<String> owingDropDownItems;

    // filters the student table using the current attributes in all the fields
    @FXML
    void search(ActionEvent event) {
        changeUrl.apply(getCurrentUrl());
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initMenusItems();
    }

    // sets the function for changing the url used to fetch items for 
    // the student table
    public void setChangeUrl(Function<String, NullType> changeUrl){
        this.changeUrl = changeUrl;
    }

    // sets the lists of items to be used in drop down fields
    @Override
    public void setDropDownItems(ArrayList<ArrayList> dropDownItems) {
        System.out.println(dropDownItems);
        this.classDropDownItems = (ArrayList<String>)(dropDownItems.get(0)).clone();
        this.yearDropDownItems = (ArrayList<String>)(dropDownItems.get(1)).clone();
        initSearchClass();
        initSearchYear();
    }

    // initializes the dropdown fields
    void initMenusItems(){
        initRegistered();
        initOwing();
    }

    // initializes the class field as required
    void initSearchClass(){
        searchClass.getItems().clear();
        classDropDownItems.add(0,"Search Class");
        Tools.addDropDownItemsFromFieldAndItems(searchClass, classDropDownItems);
    }

    // initializes the year field as required
    void initSearchYear(){
        year.getItems().clear();
        yearDropDownItems.add(0,"Year");
        Tools.addDropDownItemsFromFieldAndItems(year, yearDropDownItems);
    }

    // initializes the registered field as required
    void initRegistered(){
        registered.getItems().clear();
        registeredDropDownItems = new ArrayList();
        registeredDropDownItems.add("Registered");
        registeredDropDownItems.add("Yes");
        registeredDropDownItems.add("No");
        Tools.addDropDownItemsFromFieldAndItems(registered, registeredDropDownItems);
    }

    // initializes the owing field as required
    void initOwing(){
        owing.getItems().clear();
        owingDropDownItems = new ArrayList();
        owingDropDownItems.add("Owing");
        owingDropDownItems.add("Yes");
        owingDropDownItems.add("No");
        Tools.addDropDownItemsFromFieldAndItems(owing, owingDropDownItems);
    }

    // gets the current search url from the search fields
    String getCurrentUrl(){

        String idParam = getParamFromFieldAndAttribute(searchId, "id");

        String nameParam = getParamFromFieldAndAttribute(searchName, "pupil_name");

        String classParam = getParamFromFieldAndAttribute(searchClass, "class_name");

        String parentNameParam = getParamFromFieldAndAttribute(searchParent, "parent_name");

        String parentPhoneParam = getParamFromFieldAndAttribute(searchPhone, "parent_phone");

        String registeredParam = getParamFromFieldAndAttribute(registered, "registered");

        String owingParam = getParamFromFieldAndAttribute(owing, "owing");

        String yearParam = getParamFromFieldAndAttribute(year, "year");


        String currentUrl = "?";
        if(idParam.length() > 0){
            currentUrl += idParam + "&";
        }
        if(nameParam.length() > 0){
            currentUrl += nameParam + "&";
        }
        if(classParam.length() > 0 && !classDropDownItems.get(0).equals(searchClass.getText())){
            currentUrl += classParam + "&";
        }
        if(yearParam.length() > 0 && !yearDropDownItems.get(0).equals(year.getText())){
            currentUrl += yearParam + "&";
        }
        if(parentNameParam.length() > 0){
            currentUrl += parentNameParam + "&";
        }
        if(parentPhoneParam.length() > 0){
            currentUrl += parentPhoneParam + "&";
        }
        if(registeredParam.length() > 0 && !registeredDropDownItems.get(0).equals(registered.getText())){
            currentUrl += registeredParam + "&";
        }
        if(owingParam.length() > 0 && !owingDropDownItems.get(0).equals(owing.getText())){
            currentUrl += owingParam + "&";
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

    // encodes the url paramter to escape special characters
    public static String encodeUrlParameter(String text) {
        try {
            return URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
