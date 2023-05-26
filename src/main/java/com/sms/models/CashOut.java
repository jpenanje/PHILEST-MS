package com.sms.models;

import com.sms.interfaces.TableRowable;

public class CashOut  implements TableRowable{
    private String id;
    private String receiverName = "Jack";
    private String purpose = "food";
    private String amount = "30000";
    private String date = "2023-05-23T12:00:00";

    public CashOut() {
        super();
    }

    public CashOut(String id, String receiverName, String purpose,  String amount, String date) {
        super();
        this.id = id;
        this.receiverName = receiverName;
        this.purpose = purpose;
        this.amount = amount;
        this.date = date;
        
    }

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

    @Override
    public String toJson() {
        // return null;
        // return {"id":11,"date":"2023-22-05 12:23:06","amount":"10000","student":11,"purpose":"registration","academic_year":"2023/2024"}
        // return "{\"date\": \"" + "05/23/2023" + "\", \"amount\": \"" + "20000" + "\", \"purpose\": \""
        //         + "installement" + "\", \"student\": " + 11 + ", \"academic_year\": \""+"2022/2023"+"\"}";

        return "{\"date\": \"" + date + "\", \"amount\": \"" + amount + "\", \"purpose\": \""
                + purpose + "\", \"name_of_receiver\": \"" + receiverName + "\"}";
    }

    @Override
    public TableRowable fromJson(String json) {
        // TODO Auto-generated method stub
        return null;
    }
}
