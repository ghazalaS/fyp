package com.example.amna.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zain on 1/7/2017.
 */
public class NeedHelp extends AppCompatActivity {
    private Button btnSearch;
    private SearchView searchView;
    private Spinner spinCategory;
    private String expertise="";
    private String message="";
    needHelpData needHelpData;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.need_help);

        needHelpData=new needHelpData();

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

       // View mCustomView = mInflater.inflate(R.layout.logout_action_bar, null);
        View mCustomView = mInflater.inflate(R.layout.logout_back_action_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("RepairHub");

        TextView tvLogout = (TextView) mCustomView.findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                editor.putString("uname", "");
                editor.putString("password", "");
                editor.putString("category", "");
                editor.commit();
                Intent i = new Intent(getBaseContext(), Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        ImageView ivBack=(ImageView) mCustomView.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);


        spinCategory=(Spinner)findViewById(R.id.spinCategory);

        ArrayList<String> spSQDataSource=new ArrayList<String>();
        spSQDataSource.add("Painter");
        spSQDataSource.add("Constructor");
        spSQDataSource.add("Appliance Repairer");
        spSQDataSource.add("Cleaning Service");
        spSQDataSource.add("Wood Work");
        spSQDataSource.add("Electrical Service");
        spSQDataSource.add("Plumber");
        spSQDataSource.add("Masonary/Tile Work");
        spSQDataSource.add("Landscaping/Gardening");
        spSQDataSource.add("Home Improvement");
        ArrayAdapter<String> adapterSQ=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spSQDataSource);
        adapterSQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapterSQ);
        spinCategory.setSelection(0);
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position == 0) {
                    expertise = "Painter";
                }
                if (position == 1) {
                    expertise = "Constructor";
                }
                if (position == 2) {
                    expertise = "Appliance Repairer";
                }
                if (position == 3) {
                    expertise = "Cleaning Service";
                }
                if (position == 4) {
                    expertise = "Wood Work";
                }
                if (position == 5) {
                    expertise = "Electrical Service";
                }
                if (position == 6) {
                    expertise = "Plumber";
                }
                if (position == 7) {
                    expertise = "Masonary/Tile Work";
                }
                if (position == 8) {
                    expertise = "Landscaping/Gardening";
                }
                if (position == 9) {
                    expertise="Home Improvement";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        searchView=(SearchView) findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                expertise=query;
                startRequest();

                //Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(getBaseContext(), newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        btnSearch=(Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRequest();

            }
        });

    }

    private void startRequest() {
        String url = "https://sheltered-tor-47307.herokuapp.com/getrepairerlistbyexpertise/";
        JSONObject obj = new JSONObject();
        try{
            obj.put("expertise", expertise);
        }catch (JSONException je){
            je.printStackTrace();
        }

        //  RequestQueue queue = MyVolley.getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonResponse = response.getJSONArray("data");
                            //JSONArray ja=new JSONArray(jsonResponse.getJSONArray("data"));
                            String[] rname=new String[jsonResponse.length()];
                            String[] rating=new String[jsonResponse.length()];
                            for(int i=0;i<jsonResponse.length();i++)
                            {
                                rname[i]=jsonResponse.getJSONObject(i).getString("username");
                                rating[i]=jsonResponse.getJSONObject(i).getString("rating");
                            }
                            Intent i=new Intent(getBaseContext(),SearchActivity.class);
                            i.putExtra("rname",rname);
                            i.putExtra("rating",rating);
                            startActivity(i);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        Volley.newRequestQueue(this).add(jsObjRequest);
    }



}
