package com.example.podlibrary;

import java.io.Serializable;

/**
 * Created by Buzz on 05/01/2017.
 */

public class Serialisation implements Serializable{

    private String orderObject;

    public void Serialisation(String orderObject){
        this.orderObject = orderObject;
    }
    // add more features if there are any e.g. orderID, orderImage etc...
}
