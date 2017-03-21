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

    private static Repairer repairer=new Repairer();

    public static void setUserInfo(Repairer repairer1){
        repairer.setUname(repairer1.getUname());
        repairer.setFname(repairer1.getFname());
        repairer.setLname(repairer1.getLname());
        repairer.setEmail(repairer1.getEmail());
        repairer.setPhno(repairer1.getPhno());
        repairer.setShop(repairer1.getShop());
        repairer.setCnic(repairer1.getCnic());
        repairer.setLatitude(repairer1.getLatitude());
        repairer.setLongitude(repairer1.getLongitude());
    }

    public static Repairer getUserInfo(){
        return repairer;
    }
}
