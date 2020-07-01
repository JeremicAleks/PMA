package com.ftn.pma.model;

import java.io.Serializable;
import java.util.List;

public class Reservation implements Serializable {

    private String email;
    private List<TypeOfService> typeOfService;
    private String date;
    private String time;
    private int userId;
    private String userKey;

    public Reservation() {
    }

    public Reservation(String email, List<TypeOfService> typeOfService, String date, String time) {
        this.email = email;
        this.typeOfService = typeOfService;
        this.date = date;
        this.time = time;
    }

    public Reservation(String email, List<TypeOfService> typeOfService, String date, String time, int userId) {
        this.email = email;
        this.typeOfService = typeOfService;
        this.date = date;
        this.time=time;
        this.userId=userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TypeOfService> getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(List<TypeOfService> typeOfService) {
        this.typeOfService = typeOfService;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
