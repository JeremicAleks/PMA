package com.ftn.pma.model;

import java.io.Serializable;

public class Car implements Serializable {
    private byte[] image;
    private int id;
    private String brand;
    private String model;
    private double price;
    private String power;
    private String horsePower;
    private String torque;
    private String revAtMaxPower;
    private String transmission;
    private double height;
    private double length;
    private double width;

    public Car() {
    }

    public Car(String brand, String model, double price, String power, String horsePower, String torque, String revAtMaxPower, String transmission, double height, double length, double width) {
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.power = power;
        this.horsePower = horsePower;
        this.torque = torque;
        this.revAtMaxPower = revAtMaxPower;
        this.transmission = transmission;
        this.height = height;
        this.length = length;
        this.width = width;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(String horsePower) {
        this.horsePower = horsePower;
    }

    public String getTorque() {
        return torque;
    }

    public void setTorque(String torque) {
        this.torque = torque;
    }

    public String getRevAtMaxPower() {
        return revAtMaxPower;
    }

    public void setRevAtMaxPower(String revAtMaxPower) {
        this.revAtMaxPower = revAtMaxPower;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
