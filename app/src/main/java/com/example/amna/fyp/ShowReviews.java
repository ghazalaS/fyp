package com.example.amna.fyp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by NewShalimarComputer on 9/29/2016.
 */
public class ShowReviews extends AppCompatActivity {
    private ListView lvRev;
    private ArrayAdapter<String> arRev;
    private ArrayList<String > alRev;
    private String data[],exp[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_reviews);
        data=new String[getIntent().getExtras().getStringArray("data").length];
        data=getIntent().getExtras().getStringArray("data");
        int l=data.length;
        if(l==0)
        {
            callNoReviewToast();
        }
        exp=new String[l];
        lvRev = (ListView) findViewById(R.id.listView);
        alRev = new ArrayList<String>();
        //    int l=repairer.expertise.length;
        for(int i=0;i<l;i++) {
            alRev.add(data[i]);
        }
        ReviewsAdapter adapter = new ReviewsAdapter(alRev, this);
        lvRev.setAdapter(adapter);

    }
    void callNoReviewToast()
    {
        Toast.makeText(this, "No reviews yet!", Toast.LENGTH_LONG).show();

    }
}
