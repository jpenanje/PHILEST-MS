package com.sms.tools;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.models.Response;

public class RequestManager {
    public static CompletableFuture<HttpResponse<String>> fetchNumberOfItems(String endUrl) {
        System.out.println("fetch number of items");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8000/" + endUrl + "count/"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Token 5644029f942e815732679b78054e39997d767d94")
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }

    public static ArrayList<Response> fetchItems(String endUrl,
            Function<Double, NullType> incrementFunction, int numberOfItems, double progress) {
                ArrayList<Response> items = new ArrayList();
        System.out.println("fetch items");
        for (int i = 0; i < numberOfItems; i++) {
            try {
                HttpResponse<String> response = fetchItem(i, endUrl, incrementFunction, progress).get();
                Response customResponse = Tools.getCustomResponseFromResponse(response);
                if (customResponse.getCode() == 200) {
                    items.add(customResponse);
                    incrementFunction.apply(progress);
                } else {
                    System.out.println("could not load an item");
                    return null;
                }
            } catch (Exception e) {
                System.out.println("could not load an item");
                return null;
                // TODO: handle exception
            }
        }
        return items;
    }

    public static CompletableFuture<HttpResponse<String>> fetchItem(int index, String endUrl,
            Function<Double, NullType> incrementFunction, double progress) {
        System.out.println("fetch item");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8000/" + endUrl + "?page=" + (index + 1)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Token 5644029f942e815732679b78054e39997d767d94")
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }

    public static CompletableFuture<HttpResponse<String>> fetchDropDownLists(String endUrl, Function<Double, NullType> incrementFunction, double increment){
        System.out.println("fetch drop down items");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8000/" + endUrl + "lists/"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Token 5644029f942e815732679b78054e39997d767d94")
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }
}
