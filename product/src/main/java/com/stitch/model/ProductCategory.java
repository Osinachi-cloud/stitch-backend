package com.stitch.model;



public enum ProductCategory {

    MEN("Men's wear"),
    WOMEN("Women's Wear"),

    CHILDREN("Children's wear");

    private final String description;

    ProductCategory(String description){
        this.description = description;
    }

    @Override
    public String toString(){
        return description;
    }


}