package com.sms.interfaces;

// interface for models which can be displayed in a table
public interface TableRowable {
    public String toJson();
    public TableRowable fromJson(String json);
    public String getId();
}
