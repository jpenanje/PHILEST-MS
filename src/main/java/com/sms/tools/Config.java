package com.sms.tools;



/**
 * Config
 */

//  Configuration parameters for the application. 
// With some global varibles as well

public class Config {

    public static String primaryColor = "#4A0000";
    public static String whiteColor = "#FFFFFF";
    public static double tableRowHeight = 30;
    public static double iconHeight = tableRowHeight + 6.2;
    public static String valueDelimiter = "$$";
    public static int fetchItemTimeout = 10;
    public static int saveItemTimeout = 30;
    public static double numItemsRequestLoadingIncrement = 10;
    public static double dropDownRequestLoadingIncrement = 5;
    public static double metricsLoadingIncrement = 1/4;
    public static double yearsLoadingIncrement = 1/4;
    public static int deleteItemTimeout = 30;

    public static String token;
    public static String baseUrl = "http://127.0.0.1:8000";

    public static String currentUserPic;
    public static String currentUserPhone;
    public static String currentUserFullName;
    public static String currentUserName;
}