package com.example.podlibrary;

import java.io.Serializable;

public class Order implements Serializable{

    private String recipientName;

    public Order(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
