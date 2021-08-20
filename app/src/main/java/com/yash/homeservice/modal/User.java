package com.yash.homeservice.modal;

public class User {
    private String id;
    private String email;
    private String phone;
    private String address;
    private String name;


    public User(){

    }

    public User(String id, String email, String phone, String address, String name) {
        this.id=id;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.name = name;
    }
    public String getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }


}
