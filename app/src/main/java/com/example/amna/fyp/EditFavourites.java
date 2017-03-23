package com.example.amna.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NewShalimarComputer on 1/8/2017.
 */
public class EditFavourites extends AppCompatActivity {
    private ListView listView;
    ListAdapter adapter;
    ArrayList<String> dataItems = new ArrayList<String>();
    SearchView sv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_delete_fav);
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("RepairHub");

        TextView tvDone = (TextView) mCustomView.findViewById(R.id.tvDone);
        tvDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView tvCancel = (TextView) mCustomView.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        final String[] dataArray = getResources().getStringArray(R.array.favdata);
     /*   List<String> dataTemp = Arrays.asList(dataArray);
        dataItems.addAll(dataTemp);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new FavouritesAdapter(EditFavourites.this, dataItems);
        listView.setAdapter(adapter);*/

        String url = "https://sheltered-tor-47307.herokuapp.com/givefavourites/";
        String username=getIntent().getStringExtra("uname");
        JSONObject obj = new JSONObject();
        try{
            obj.put("uname", username);
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
                            String[] dataArray1=new String[jsonResponse.length()];
                            for(int i=0;i<jsonResponse.length();i++)
                            {
                                dataArray1[i]=jsonResponse.getJSONObject(i).getString("username");
                            }
                            List<String> dataTemp = Arrays.asList(dataArray1);
                            dataItems.addAll(dataTemp);
                            listView = (ListView) findViewById(R.id.listView);
                            adapter = new FavouritesAdapter(EditFavourites.this, dataItems);
                            listView.setAdapter(adapter);

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

        //SearchView
        sv=(SearchView)findViewById(R.id.searchView);
        sv.setIconifiedByDefault(false);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String newText) {
                // your text view here
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String rname) {
                startSearchByUsernameRequest(rname);
                return true;
            }

        });


       /* StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonResponse = new JSONObject(response).getJSONArray("data");
                            //JSONArray ja=new JSONArray(jsonResponse.getJSONArray("data"));
                            String[] dataArray1=new String[jsonResponse.length()];
                            for(int i=0;i<=jsonResponse.length();i++)
                            {
                                dataArray1[i]=jsonResponse.getJSONObject(i).getString("username");
                            }
                            List<String> dataTemp = Arrays.asList(dataArray);
                            dataItems.addAll(dataTemp);
                            listView = (ListView) findViewById(R.id.listView);
                            adapter = new FavouritesAdapter(EditFavourites.this, dataItems);
                            listView.setAdapter(adapter);
                            //String site = jsonResponse.getString("site"),
                            //        network = jsonResponse.getString("network");
                            //System.out.println("Site: "+site+"\nNetwork: "+network);
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
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                String username=getIntent().getStringExtra("uname");
                // the POST parameters:
                params.put("uname", username);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
              //  headers.put("User-agent", "My useragent");
                headers.put( "charset", "utf-8");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);*/

    }
    public void startSearchByUsernameRequest(String rname){
        String url = "https://sheltered-tor-47307.herokuapp.com/getrepairerlistbyusername/";
        JSONObject obj = new JSONObject();
        try{
            obj.put("r_username", rname);
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
                           // finish();

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
