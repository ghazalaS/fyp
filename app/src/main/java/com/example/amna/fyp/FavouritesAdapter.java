package com.example.amna.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by NewShalimarComputer on 1/8/2017.
 */
public class FavouritesAdapter extends ArrayAdapter<String> {
    public Context context;
    public ArrayList<String> data;
    Repairer r;
    String[] clientList=null;
    boolean isFav;
    public FavouritesAdapter(Context context, ArrayList<String> dataItem) {
        super(context, R.layout.fav_list_item, dataItem);
        this.data = dataItem;
        this.context = context;
    }
    public class ViewHolder {
        TextView text;
        Button button;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.fav_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.favName);
            viewHolder.button = (Button) convertView
                    .findViewById(R.id.btnDelFav);
            convertView.setTag(viewHolder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRequestForProfile(viewHolder.text.getText().toString());
                }
            });
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String temp = getItem(position);
        viewHolder.text.setText(temp);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             //   int i=(int)v.getTag();
                data.remove(position);
                FavouritesAdapter.this.notifyDataSetChanged();
                startRequest(viewHolder.text.getText().toString());



            }
        });

        return convertView;
    }
    public void startRequest(String r_username)
    {
        SharedPreferences prefs = getContext().getSharedPreferences("user", 0);
        String cname="";
        cname = prefs.getString("uname", "No name defined");
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            cname = prefs.getString("uname", "No name defined");//"No name defined" is the default value.

        }
        String url = "http://192.168.0.7:8000/deletefavourite/";
        JSONObject obj = new JSONObject();
        try{
            obj.put("c_username", cname);
            obj.put("r_username",r_username);
        }catch (JSONException je){
            je.printStackTrace();
        }

        //  RequestQueue queue = MyVolley.getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //JSONObject jsonResponse = response.getJSONObject("message");
                            boolean message=response.getBoolean("message");
                            if(message)
                            {
                                Toast.makeText(getContext(), "Favourite deleted!!", Toast.LENGTH_LONG).show();
                            }
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
        Volley.newRequestQueue(getContext()).add(jsObjRequest);
    }
    public void startRequestForProfile(String r_username){
        SharedPreferences prefs = getContext().getSharedPreferences("user", 0);
        String cname="";
        cname = prefs.getString("uname", "No name defined");
        String url = "http://192.168.0.7:8000/showrepairerprofile/";
        JSONObject obj = new JSONObject();
        try{
            obj.put("c_username", cname);
            obj.put("r_username",r_username);
        }catch (JSONException je){
            je.printStackTrace();
        }
        isFav=false;
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            boolean message=jsonObject.getBoolean("message");
                            if(message)
                            {
                                r=new Repairer();
                                r.setFname(jsonObject.getString("fname"));
                                r.setLname(jsonObject.getString("lname"));
                                r.setUname(jsonObject.getString("uname"));
                                r.setCnic(jsonObject.getString("cnic"));
                                r.setEmail(jsonObject.getString("email"));
                                r.setPhno(jsonObject.getString("contactno"));

                                JSONArray temp = jsonObject.getJSONArray("expertise");
                                int length = temp.length();
                                if (length > 0) {
                                    r.expertise = new String[length];
                                    for (int i = 0; i < length; i++) {
                                        r.expertise[i] = temp.getString(i);
                                    }
                                }
                                temp = jsonObject.getJSONArray("reviews");
                                length = temp.length();
                                if (length > 0) {
                                    r.reviews= new String[length];
                                    for (int i = 0; i < length; i++) {
                                        r.reviews[i] = temp.getString(i);
                                    }
                                }

                                temp = jsonObject.getJSONArray("clientList");
                                length = temp.length();
                                if (length > 0) {
                                    clientList= new String[length];
                                    for (int i = 0; i < length; i++) {
                                        clientList[i] = temp.getString(i);
                                    }
                                }
                                r.setShop(jsonObject.getString("shop"));
                                r.avgRating=jsonObject.getString("avgRating");
                                isFav=jsonObject.getBoolean("isFav");
                                openNextPage();
                            }
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
        Volley.newRequestQueue(getContext()).add(jsObjRequest);
    }
    public void openNextPage()
    {
        Intent i = new Intent(getContext(), ShowRepairerProfileToCustomer.class);
        i.putExtra("data", r);
        i.putExtra("isFav",isFav);
        if(clientList!=null){
            i.putExtra("clientList", clientList);
        }
        else {
            i.putExtra("clientList", "");
        }
        context.startActivity(i);
    }

}
