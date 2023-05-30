package com.sms.models;

import com.fasterxml.jackson.databind.JsonNode;

// custom response class
public class Response {
    int code;
    JsonNode data;

    // getters and setters
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public JsonNode getData() {
        return data;
    }
    public void setData(JsonNode data) {
        this.data = data;
    }
}
