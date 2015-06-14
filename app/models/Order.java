package models;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Created by rmasal on 2015-06-09.
 */


public class Order {
    String url;
    JsonNode node;

    public Order(String url, JsonNode node) {
        this.url = url;
        this.node = node;

    }




}
