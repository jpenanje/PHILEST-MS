package com.sms.models;

import com.fasterxml.jackson.databind.JsonNode;

public class Response {
    int code;
    JsonNode data;
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
