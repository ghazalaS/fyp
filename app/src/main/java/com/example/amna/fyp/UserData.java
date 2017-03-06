package com.example.amna.fyp;

import java.io.Serializable;

/**
 * Created by M  saeed on 9/11/2016.
 */
public class UserData implements Serializable{
    public String uname;
    public String lname;
    public String fname;
    public String password;
    public String cnic;
    public String email;
    public String secretans;
    public String secretque;
    public String phno;
    public String category;
    public String expertise[];
    public String favourites[];
    public String reviews[];
    public String shop;
    public String longitude;
    public String latitude;
    public String avgRating;
   // public Shop shop;

    void setUname(String uname){
        this.uname=uname;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    void setLongitude(String lon){longitude=lon;}
    void setLatitude(String lat){latitude=lat;}
    void setLname(String lname){
        this.lname=lname;
    }
    void setFname(String fname){
        this.fname=fname;
    }
    void setPassword(String password){
        this.password=password;
    }
    void setCnic(String cnic){
        this.cnic=cnic;
    }
    void setEmail(String email){
        this.email=email;
    }
    void setSecretans(String secretans){
        this.secretans=secretans;
    }
    void setSecretque(String secretque){
        this.secretque=secretque;
    }
    void setPhno(String phno){
        this.phno=phno;
    }
    void setCategory(String category){
        this.category=category;
    }
    void setExpertise(String expertise[]){
        this.expertise=expertise;
    }
    void setReviews(String reviews[]){this.reviews=reviews;}

    void setShop(String shop){
        this.shop=shop;
    }

    String getUname()
    {
        return uname;
    }

    String getFname(){return fname;}
    String getLname(){return lname;}
    String getPassword(){return password;}
    String getCnic(){return cnic;}

    public String getAvgRating() {
        return avgRating;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getSecretans() {
        return secretans;
    }

    public String getCategory() {
        return category;
    }

    public String[] getExpertise() {
        return expertise;
    }

    public String getSecretque() {
        return secretque;
    }

    public String getEmail() {
        return email;
    }

    public String getPhno() {
        return phno;
    }

    public String getShop() {
        return shop;
    }

    public String[] getReviews() {
        return reviews;
    }
}
