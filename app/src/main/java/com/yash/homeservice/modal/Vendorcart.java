package com.yash.homeservice.modal;

public class Vendorcart {
    private String name;
    private String service;
    private String id;
    private String timing;
    private String date;

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    private String number;
    private String uniquecode;
    private String servicecharge;
    private String orderstatus;
    private String address;
    private boolean expanded;

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

    public String getNumber() {
        return number;
    }

    public String getUniquecode() {
        return uniquecode;
    }

    public String getServicecharge() {
        return servicecharge;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public String getAddress() {
        return address;
    }

    public Vendorcart(String name, String service, String id, String timing, String date, String number, String uniquecode, String servicecharge, String orderstatus, String address) {
        this.name = name;
        this.service = service;
        this.id = id;
        this.timing = timing;
        this.date = date;
        this.number = number;
        this.uniquecode = uniquecode;
        this.servicecharge = servicecharge;
        this.orderstatus = orderstatus;
        this.address=address;
        this.expanded = false;
    }
}
