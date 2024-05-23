package com.samy.groceryapp.model;

import java.io.Serializable;

public class ViewAllModel implements Serializable {
    private String name;
    private String description;
    private String img_url;
    private int price;
    private String rating;
    private String type;

    public ViewAllModel() {
    }

    public ViewAllModel(String name, String description, String img_url, int price, String rating, String type) {
        this.name = name;
        this.description = description;
        this.img_url = img_url;
        this.price = price;
        this.rating = rating;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
