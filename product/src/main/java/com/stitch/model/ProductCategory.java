package com.stitch.model;



public enum ProductCategory {

    MEN("Men's wear"),

    KIDS("Kids wear"),

    COUPLE("Couple"),

    FAMILY("Family"),

    ASOEBI("Aso Ebi"),
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