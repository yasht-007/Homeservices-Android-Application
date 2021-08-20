package com.yash.homeservice.modal;

public class ServiceShow {
    private String id;
    private String profile;
    private String name;
    private String service;
    private String vendorid;

    public String getVendorid() {
        return vendorid;
    }

    private float rating;


    public String getId() {
        return id;
    }

    public String getProfile() {
        return profile;
    }

    public String getName() {
        return name;
    }

    public String getService() {
        return service;
    }

    public float getRating() {
        return rating;
    }

    public String getServicecharge() {
        return servicecharge;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getShopname() {
        return shopname;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    private String servicecharge;


    public ServiceShow(String id, String profile, String name, String service, float rating, String servicecharge, String city, String address, String shopname, String subcategory, String phone, String email, String mincharge, String maxcharge,String vendorid) {
        this.id = id;
        this.profile = profile;
        this.name = name;
        this.service = service;
        this.rating = rating;
        this.servicecharge = servicecharge;
        this.city = city;
        this.address = address;
        this.shopname = shopname;
        this.subcategory = subcategory;
        this.phone = phone;
        this.email = email;
        this.mincharge=mincharge;
        this.maxcharge=maxcharge;
        this.vendorid=vendorid;
    }

    private String city;
    private String address;
    private String shopname;
    private String subcategory;
    private String phone;
    private String email;
    private String mincharge;

    public String getMincharge() {
        return mincharge;
    }

    public String getMaxcharge() {
        return maxcharge;
    }

    private String maxcharge;

}


