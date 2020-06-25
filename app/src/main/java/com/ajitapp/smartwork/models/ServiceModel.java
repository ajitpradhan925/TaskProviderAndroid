package com.ajitapp.smartwork.models;

public class ServiceModel {
    private String service_name;
    private String service_price;
    private String service_description;
    private boolean service_status;
    private boolean service_enabled;



    private String service_id;


    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_price() {
        return service_price;
    }

    public void setService_price(String service_price) {
        this.service_price = service_price;
    }

    public String getService_description() {
        return service_description;
    }

    public void setService_description(String service_description) {
        this.service_description = service_description;
    }

    public boolean getService_status() {
        return service_status;
    }

    public void setService_status(boolean service_status) {
        this.service_status = service_status;
    }

    public boolean getService_enabled() {
        return service_enabled;
    }

    public void setService_enabled(boolean service_enabled) {
        this.service_enabled = service_enabled;
    }
}
