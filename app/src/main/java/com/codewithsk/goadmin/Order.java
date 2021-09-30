package com.codewithsk.goadmin;

public class Order {
    String uid,user_contact,email,total_price,status,orderId,deleveryAddress;

    public Order() {
    }

    public String getDeleveryAddress() {
        return deleveryAddress;
    }

    public void setDeleveryAddress(String deleveryAddress) {
        this.deleveryAddress = deleveryAddress;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}
