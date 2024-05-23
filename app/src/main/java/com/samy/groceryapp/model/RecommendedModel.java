package com.samy.groceryapp.model;

public class RecommendedModel {

    String name;
    int price;
    String rating;
    String img_url;
    String description;
    String type;

    public RecommendedModel() {
    }

    public RecommendedModel(String name, int price, String rating, String img_url, String description,String type) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.img_url = img_url;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getDescription() {
        return description;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
