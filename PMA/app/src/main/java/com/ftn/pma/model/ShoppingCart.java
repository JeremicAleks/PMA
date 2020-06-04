package com.ftn.pma.model;

import java.util.List;

public class ShoppingCart {

    private int id;
    private List<Car> kupljeni_automobili;

    public ShoppingCart()
    {

    }
    public ShoppingCart(int id, List<Car> kupljeni_automobili) {
        this.id = id;
        this.kupljeni_automobili = kupljeni_automobili;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Car> getKupljeni_automobili() {
        return kupljeni_automobili;
    }

    public void setKupljeni_automobili(List<Car> kupljeni_automobili) {
        this.kupljeni_automobili = kupljeni_automobili;
    }
}
