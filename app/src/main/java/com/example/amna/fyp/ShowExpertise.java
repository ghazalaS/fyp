package com.example.amna.fyp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by NewShalimarComputer on 9/29/2016.
 */
public class ShowExpertise extends AppCompatActivity {
    private ListView lvExp;
    private ArrayAdapter<String> arExp;
    private ArrayList<String > alExp;
    private String data[],exp[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        data=new String[getIntent().getExtras().getStringArray("data").length];
        data=getIntent().getExtras().getStringArray("data");
        int l=data.length;
        exp=new String[l];
        lvExp = (ListView) findViewById(R.id.listView);
        alExp = new ArrayList<String>();
    //    int l=repairer.expertise.length;
        for(int i=0;i<l;i++) {
            alExp.add(data[i]);
        }
        MyCustomAdapter adapter = new MyCustomAdapter(alExp, this);
        lvExp.setAdapter(adapter);

    }
}
