package com.yash.homeservice.modal;

public class Subcategory {

    private String id;

    public String getId() {
        return id;
    }

    private String image;
    private String title;

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public Subcategory(String id,String image, String title) {
        this.id=id;
        this.image = image;
        this.title = title;
    }
}
