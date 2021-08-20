package com.yash.homeservice.modal;

public class Servicerecycle {
    private String id;
    private String title1;
    private String title2;
    private String description;

    public String getImage() {
        return Image;
    }

    private String Image;

    private Servicerecycle(){}
    public Servicerecycle(String id, String title1, String title2, String description, String image) {
        this.id = id;
        this.title1 = title1;
        this.title2 = title2;
        this.description = description;
        this.Image = image;
    }

    public String getId() {
        return id;
    }


    public String getTitle1() {
        return title1;
    }

    public String getTitle2() {
        return title2;
    }

    public String getDescription() {
        return description;
    }

}
