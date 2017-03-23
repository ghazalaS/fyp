package com.example.amna.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kiransaeed507 on 1/10/2017.
 */
public class RepairerProfile extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    Repairer repairer;
    private Button editButton;
    private TextView tvFName, tvLName, tvUName, tvPhoneNo, tvCNIC, tvEmail, tvAddress, tvShop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repairer_profile);

        tvFName = (TextView) findViewById(R.id.tvFName);
        tvUName = (TextView) findViewById(R.id.tvUName);
        tvPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);
        tvCNIC = (TextView) findViewById(R.id.tvCNIC);
        tvShop = (TextView) findViewById(R.id.tvShop);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.logout_action_bar, null);
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
                startActivity(i);
                finish();
            }
        });
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.three_buttons_activity);

        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.repairer_menu, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                switch (itemId) {
                    case R.id.notification:
                        Intent i = new Intent(getBaseContext(), Notification.class);
                        startActivity(i);
                        break;

                    case R.id.expertise:
                        startRequestForExpertise();
                        break;
                }
            }
        });
        repairer = (Repairer) getIntent().getSerializableExtra("data");
        // Set the color for the active tab. Ignored on mobile when there are more than three tabs.
        bottomBar.setActiveTabColor("#C2185B");
        editButton = (Button) findViewById(R.id.btnEdit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), EditRepairerProfile.class);
                i.putExtra("data", repairer);
                startActivity(i);
            }
        });

        tvFName.setText(repairer.getFname() + " " + repairer.getLname());
        //   tvLName.setText(customer.getLname());
        tvUName.setText(repairer.getUname());
        tvPhoneNo.setText(repairer.getPhno());
        tvCNIC.setText(repairer.getCnic());
        tvEmail.setText(repairer.getEmail());
        tvShop.setText(repairer.getShop());

        Repairer.setUserInfo(repairer);
    }


    public void startRequestForExpertise(){
        SharedPreferences prefs = getBaseContext().getSharedPreferences("user", 0);
        String rname="";
        rname = prefs.getString("uname", "No name defined");
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            rname = prefs.getString("uname", "No name defined");//"No name defined" is the default value.

        }
        String url = "https://sheltered-tor-47307.herokuapp.com/giveexpertise/";
        JSONObject obj = new JSONObject();
        try{
            obj.put("r_username",rname);
        }catch (JSONException je){
            je.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonResponse = response.getJSONArray("data");
                            //   JSONArray ja=new JSONArray(jsonResponse.getJSONArray("expertise"));
                            //  ja=jsonResponse.getJSONArray("expertise");
                            String[] expertise=new String[jsonResponse.length()];

                            // String[] rating=new String[jsonResponse.length()];
                            for(int i=0;i<jsonResponse.length();i++)
                            {
                                expertise[i]=jsonResponse.getString(i);
                            }
                            Intent i=new Intent(getBaseContext(),EditExperties.class);
                            i.putExtra("expertise",expertise);
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

    @Override
    protected void onResume(){
        super.onResume();
        repairer=Repairer.getUserInfo();
        tvFName.setText(repairer.getFname()+" "+repairer.getLname());
        tvUName.setText(repairer.getUname());
        tvPhoneNo.setText(repairer.getPhno());
        tvCNIC.setText(repairer.getCnic());
        tvShop.setText(repairer.getShop());
        tvEmail.setText(repairer.getEmail());
    }
}
