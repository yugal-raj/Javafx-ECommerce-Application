package com.example.ecommerce;

public class Customer {
    int id;
    String name;
    String email;
    String mobile;
    String password;

    String address;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Customer(int id, String name, String email, String mobile, String password, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

}
