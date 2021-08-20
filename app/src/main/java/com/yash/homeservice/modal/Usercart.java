package com.yash.homeservice.modal;

import androidx.annotation.NonNull;

public class Usercart {

    private String image;
    private String name;
    private String service;
    private String id;
    private String timing;
    private String date;
    private String number;

    public String getNumber() {
        return number;
    }

    private String uniquecode;
    private String servicecharge;
    private String orderstatus;
    private boolean expanded;


    public String getOrderstatus() {
        return orderstatus;
    }

    public Usercart(String image, String name, String service,String number, String id, String timing, String date, String uniquecode, String servicecharge,String orderstatus) {
        this.image = image;
        this.name = name;
        this.service = service;
        this.number=number;
        this.id = id;
        this.timing = timing;
        this.date = date;
        this.uniquecode = uniquecode;
        this.servicecharge = servicecharge;
        this.orderstatus=orderstatus;
        this.expanded=false;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getService() {
        return service;
    }

    public String getId() {
        return id;
    }

    public String getTiming() {
        return timing;
    }

    public String getDate() {
        return date;
    }

    public String getUniquecode() {
        return uniquecode;
    }

    public String getServicecharge() {
        return servicecharge;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
