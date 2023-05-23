package com.sms.interfaces;

public interface TableRowable {
    public String toJson();
    public TableRowable fromJson(String json);
    public String getId();
}
