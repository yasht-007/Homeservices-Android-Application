package com.yash.homeservice.modal;

import androidx.annotation.NonNull;

public class Vendorservicelist {
    private String id;
    private String Service;
    private String image;

    public String getId() {
        return id;
    }

    public String getService() {
        return Service;
    }

    @NonNull
    @Override
    public String toString() {
        return Service;
    }

    public String getImage() {
        return image;
    }

    public Vendorservicelist(String id, String service, String image) {
        this.id = id;
        Service = service;
        this.image=image;
    }
}
