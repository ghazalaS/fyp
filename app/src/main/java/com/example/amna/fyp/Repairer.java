package com.example.amna.fyp;

import java.io.Serializable;

/**
 * Created by NewShalimarComputer on 9/27/2016.
 */
public class Repairer extends UserData  implements Serializable {
    public String expertise[];
    public String shop;

    void setShop(String shop){
        this.shop=shop;
    }
    void setExpertise(String expertise[]){
        this.expertise=expertise;
    }
    public String[] getExpertise() {
        return expertise;
    }
    public String getShop() {
        return shop;
    }


}
