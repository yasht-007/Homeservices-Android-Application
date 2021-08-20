package com.yash.homeservice.modal;

public class Servicebanner {
    private String id;
    private String bannerImage;

    public Servicebanner(String bannerimage, String id) {
        this.bannerImage = bannerimage;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public String getBanner() {
        return bannerImage;
    }


}
