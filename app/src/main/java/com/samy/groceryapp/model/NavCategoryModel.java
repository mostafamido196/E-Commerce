package com.samy.groceryapp.model;

public class NavCategoryModel {
    String name;
    String img_url;
    String describtion;
    String discount;
    String type;

    public NavCategoryModel() {
    }

    public NavCategoryModel(String name, String img_url, String describtion, String discount, String type) {
        this.name = name;
        this.img_url = img_url;
        this.describtion = describtion;
        this.discount = discount;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
