package com.sms.models;

import com.sms.interfaces.TableRowable;

// Class representing entries of money
public class CashIn implements TableRowable{

    private String id;
    private int studentId = 1;
    private String studentName = "Junior";
    private String purpose = "food";
    private String academicYear = "2022/2023";
    private String amount = "30000";
    private String date = "today";

    // no arg constructor
    public CashIn() {
        super();
    }

    // constructor with initial values for every field
    public CashIn(String id, int studentId, String studentName, String purpose, String academicYear, String amount, String date) {
        super();
        this.id = id;
        this.studentName = studentName;
        this.studentId = studentId;
        this.purpose = purpose;
        this.academicYear = academicYear;
        this.amount = amount;
        this.date = date;
        
    }

    //  getters and setters

    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getAcademicYear() {
        return academicYear;
    }
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // returns a json representation of an object of this class
    @Override
    public String toJson() {
        System.out.println(studentId);
        return "{\"date\": \"" + date + "\", \"amount\": \"" + amount + "\", \"purpose\": \""
                + purpose + "\", \"student\": " + studentId + ", \"academic_year\": \""+academicYear+"\"}";
    }

    // converts a json string to an object of this class
    @Override
    public TableRowable fromJson(String json) {
        return null;
    }
}
