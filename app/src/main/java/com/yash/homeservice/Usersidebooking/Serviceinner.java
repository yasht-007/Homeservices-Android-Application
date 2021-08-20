package com.yash.homeservice.Usersidebooking;

public class Serviceinner {
    private String id;
    private String serviceImage;

    public Serviceinner(String id, String serviceImage) {
        this.id = id;
        this.serviceImage = serviceImage;
    }

    public String getId() {
        return id;
    }

    public String getServiceImage() {
        return serviceImage;
    }


}
