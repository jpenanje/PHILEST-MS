package com.sms.models;

import com.sms.interfaces.TableRowable;

// Class representing payments
public class CashOut  implements TableRowable{
    private String id;
    private String receiverName = "Jack";
    private String purpose = "food";
    private String amount = "30000";
    private String date = "2023-05-23T12:00:00";

    // no arg constructor
    public CashOut() {
        super();
    }

    // constructor with initial values for every field
    public CashOut(String id, String receiverName, String purpose,  String amount, String date) {
        super();
        this.id = id;
        this.receiverName = receiverName;
        this.purpose = purpose;
        this.amount = amount;
        this.date = date;
        
    }

    //  getters and setters

    public String getReceiverName() {
        return receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
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
        return "{\"date\": \"" + date + "\", \"amount\": \"" + amount + "\", \"purpose\": \""
                + purpose + "\", \"name_of_receiver\": \"" + receiverName + "\"}";
    }

    // converts a json string to an object of this class
    @Override
    public TableRowable fromJson(String json) {
        return null;
    }
}
