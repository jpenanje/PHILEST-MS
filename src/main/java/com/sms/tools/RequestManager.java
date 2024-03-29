package com.sms.tools;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import javax.lang.model.type.NullType;

import com.fasterxml.jackson.databind.JsonNode;
import com.sms.models.Response;
import com.sms.models.Student;

// Manages requests of the application
public class RequestManager {

    // fetches the number of items to be returned for a given url
    public static CompletableFuture<HttpResponse<String>> fetchNumberOfItems(String baseUrl, String searchUrl) {
        System.out.println("fetch number of items");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.baseUrl+"/" + baseUrl + "count/" + searchUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Token " + Config.token)
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }

    // fetches the years for the dashboard
    public static CompletableFuture<HttpResponse<String>> fetchYears(String baseUrl) {
        System.out.println("fetch years for dashboard");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.baseUrl+"/" + baseUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Token " + Config.token)
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }

    // fetches the metrics for the dashboard
    public static CompletableFuture<HttpResponse<String>> fetchMetrics(String baseUrl, String endUrl) {
        System.out.println("fetch metrics for dashboard");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.baseUrl+"/" + baseUrl +endUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Token " + Config.token)
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }

    // fetches the items based on a given url
    public static ArrayList<Response> fetchItems(String baseUrl, String searchUrl,
            Function<Double, NullType> incrementFunction, int numberOfItems, double progress) {
        ArrayList<Response> items = new ArrayList();
        System.out.println("fetch items");
        if(numberOfItems > 0){
            for (int i = 0; i < numberOfItems; i++) {
                try {
                    HttpResponse<String> response = fetchItem(i, baseUrl, searchUrl, incrementFunction, progress).get();
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
        }

        else{
            incrementFunction.apply(100 - (Config.dropDownRequestLoadingIncrement + Config.numItemsRequestLoadingIncrement));
        }
        return items;
    }

    // fetches a single item based on a given url
    public static CompletableFuture<HttpResponse<String>> fetchItem(int index, String endUrl, String searchUrl,
            Function<Double, NullType> incrementFunction, double progress) {
        System.out.println("fetch item");
        HttpClient client = HttpClient.newHttpClient();
        
        String filterStr = "";
        if(searchUrl.length() > 0){
            filterStr = searchUrl + "&page=" + (index + 1);
        }
        else{
            filterStr = "?page=" + (index + 1);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.baseUrl + "/" + endUrl + filterStr))
                .header("Content-Type", "application/json")
                .header("Authorization", "Token "+Config.token)
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }

    // fetches the list of lists for the dropdown fields
    public static CompletableFuture<HttpResponse<String>> fetchDropDownLists(String endUrl,
            Function<Double, NullType> incrementFunction, double increment) {
        System.out.println("fetch drop down items");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.baseUrl+"/" + endUrl + "lists/"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Token "+Config.token)
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }

    // posts a json string to a given url
    public static CompletableFuture<HttpResponse<String>> postItem(String endUrl, String body){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;

        if(Config.token != null){
            request = HttpRequest.newBuilder()
                .uri(URI.create(Config.baseUrl+"/"+endUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Token "+Config.token)
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();
        }

        else{
            request = HttpRequest.newBuilder()
            .uri(URI.create(Config.baseUrl+"/"+endUrl))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
            .build(); 
        }

        // Send the request asynchronously
        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }

    // updates an item from a given url and json string
    public static CompletableFuture<HttpResponse<String>> updateItem(String endUrl, String body){
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.baseUrl+"/"+endUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Token "+Config.token)
                .PUT(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }

    // deletes an item from a url and an id
    public static CompletableFuture<HttpResponse<String>> deleteItem(String endUrl, String id){
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.baseUrl+"/"+endUrl+id+"/"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Token "+Config.token)
                .DELETE()
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return responseFuture;
    }
}
