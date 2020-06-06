package com.ftn.pma.model;

public class ShoppingCart {

    private int id;
    private String user_id;
    private String cars_id;

    public ShoppingCart()
    {

    }

    public ShoppingCart(String user_id, String cars_id) {
        this.user_id = user_id;
        this.cars_id = cars_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCars_id() {
        return cars_id;
    }

    public void setCars_id(String cars_id) {
        this.cars_id = cars_id;
    }
}
