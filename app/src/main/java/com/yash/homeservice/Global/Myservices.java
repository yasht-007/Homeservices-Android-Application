package com.yash.homeservice.Global;

public class Myservices {
    private String maincat;
    private String subcat;
    private String servicecharge;

    public Myservices(String maincat, String subcat, String servicecharge) {
        this.maincat = maincat;
        this.subcat = subcat;
        this.servicecharge = servicecharge;
    }

    public String getMaincat() {
        return maincat;
    }

    public String getSubcat() {
        return subcat;
    }

    public String getServicecharge() {
        return servicecharge;
    }
}
