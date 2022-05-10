package com.example.firebase;

public class dataholder {
    public dataholder(String name, String phone, String address, String price, String trans) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.price = price;
        this.trans = trans;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    String phone;
    String address;
    String price;
    String trans;
}
