package com.ftn.pma.model;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String surname;
    private String telephone;
    private String email;
    private String password;
    private String key;

    public User() {
    }

    public User(String name, String surname, String telephone, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
