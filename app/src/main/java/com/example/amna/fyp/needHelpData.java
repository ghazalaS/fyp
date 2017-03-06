package com.example.amna.fyp;

import java.io.Serializable;

/**
 * Created by zain on 2/20/2017.
 */
public class needHelpData  implements Serializable {

    String repairersList[];
    String ratingList[];
    String repairerName,expertise;


    void setRepairersList(String repairersList[]){ this.repairersList=repairersList; }
    public String[] getRepairersList() { return repairersList; }

    void setRatingList(String ratingList[]){this.ratingList=ratingList;}
    public String[] getRatingList() {return ratingList;}

    void setRepairerName(String repairerName){this.repairerName=repairerName;}
    public String getRepairerName() { return repairerName; }

    void setExpertise(String expertise){this.expertise=expertise;}
    public String getExpertise() {return expertise;}
}
