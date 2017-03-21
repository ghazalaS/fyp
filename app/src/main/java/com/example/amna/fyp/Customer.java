package com.example.amna.fyp;

import java.io.Serializable;

/**
 * Created by NewShalimarComputer on 9/27/2016.
 */
public class Customer extends UserData  implements Serializable {
    public String favourites[];

    private static Customer customer=new Customer();
    public static void setUserInfo(Customer custmer){
        customer.setUname(custmer.getUname());
        customer.setFname(custmer.getFname());
        customer.setLname(custmer.getLname());
        customer.setEmail(custmer.getEmail());
        customer.setPhno(custmer.getPhno());
        customer.setCnic(custmer.getCnic());
        customer.setLatitude(custmer.getLatitude());
        customer.setLongitude(custmer.getLongitude());
    }

    public static Customer getUserInfo(){
        return customer;
    }
}
