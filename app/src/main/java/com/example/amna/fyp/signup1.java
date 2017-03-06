package com.example.amna.fyp;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NewShalimarComputer on 9/12/2016.
 */
public class signup1 extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{
    private EditText etusername;
    private EditText etfirstname;
    private EditText etlastname;
    private EditText etpassword;
    private EditText etemail;
    private EditText etcnic;
    private EditText etphno;
    private EditText etsecretanswer;
    private EditText etshop;
    private Spinner spinCategory;
    private Spinner spinExpertise;
    private Spinner spinSecretQue;
    private Button btnSignup;
    private Button btnNext;
    private Button btnLoc;
    private ImageView ivIconUsername;
    private ImageView ivIconEmail;
    private ImageView ivIconCnic;

    private UserData userdata;
    Repairer r;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        userdata=new UserData();
        r=new Repairer();

        etusername=(EditText)findViewById(R.id.etUName);
        etfirstname=(EditText)findViewById(R.id.etFName);
        etlastname=(EditText)findViewById(R.id.etLName);
        etpassword=(EditText)findViewById(R.id.etPass);
        etemail=(EditText)findViewById(R.id.etEmail);
        etcnic=(EditText)findViewById(R.id.etCNIC);
        etphno=(EditText)findViewById(R.id.etPhone);
        etsecretanswer=(EditText)findViewById(R.id.etSecAns);
        spinCategory=(Spinner)findViewById(R.id.spinCategory);
        spinSecretQue=(Spinner)findViewById(R.id.spinSecQue);
        btnSignup=(Button)findViewById(R.id.btnReg);
        btnNext=(Button)findViewById(R.id.btnNext);
        btnSignup.setEnabled(false);
        btnNext.setEnabled(false);
        ivIconEmail=(ImageView)findViewById(R.id.ivIconEmail);
        ivIconUsername=(ImageView)findViewById(R.id.ivIconUsername);
        ivIconCnic=(ImageView)findViewById(R.id.ivIconCnic);
        btnSignup.setOnClickListener(this);
        btnLoc=(Button)findViewById(R.id.btnLoc);
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(signup1.this, MapsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate())
                    Toast.makeText(getBaseContext(), "Fill all the fields!", Toast.LENGTH_LONG).show();
                    // call AsynTask to perform network operation on separate thread
                else {
                    // userdata = new UserData();
                    r.setUname(etusername.getText().toString());
                    r.setFname(etfirstname.getText().toString());
                    r.setLname(etlastname.getText().toString());
                    r.setEmail(etemail.getText().toString());
                    r.setPassword(etpassword.getText().toString());
                    r.setPhno(etphno.getText().toString());
                    r.setCnic(etcnic.getText().toString());
                    r.setSecretque(spinSecretQue.getSelectedItem().toString());
                    r.setSecretans(etsecretanswer.getText().toString());
                    r.setCategory(spinCategory.getSelectedItem().toString());
                    r.setLongitude(userdata.getLongitude());
                    r.setLatitude(userdata.getLatitude());
                    Intent intent = new Intent(getBaseContext(), Expertise.class);
                    intent.putExtra("data", r);
                    startActivity(intent);
                }

            }
        });

        ArrayList<String> spCatDataSource=new ArrayList<String>();
        spCatDataSource.add("Client");
        spCatDataSource.add("Repairer");
        ArrayAdapter<String> adapterCat=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spCatDataSource);
        adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapterCat);
        spinCategory.setSelection(0);

        ArrayList<String> spExpDataSource=new ArrayList<String>();
        spExpDataSource.add("Client");
        spExpDataSource.add("Repairer");
        ArrayAdapter<String> adapterExp=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spExpDataSource);
        adapterExp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapterExp);
        spinCategory.setSelection(0);
        //  spinExpertise.setVisibility(View.GONE);

        ArrayList<String> spSQDataSource=new ArrayList<String>();
        spSQDataSource.add("Client");
        spSQDataSource.add("Repairer");
        ArrayAdapter<String> adapterSQ=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spSQDataSource);
        adapterSQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapterSQ);
        spinCategory.setSelection(0);
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position==1){
                    /*spinExpertise.setVisibility(View.VISIBLE);
                    etshop.setVisibility(View.VISIBLE);*/
                    btnNext.setVisibility(View.VISIBLE);
                    btnSignup.setVisibility(View.GONE);

                }
                else {
                   /* spinExpertise.setVisibility(View.GONE);
                    etshop.setVisibility(View.GONE);*/
                    btnNext.setVisibility(View.GONE);
                    btnSignup.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        if(!isConnected()){

            Toast.makeText(getApplicationContext(), "You are not connected to internet!", Toast.LENGTH_LONG).show();
        }
        // checking email pattern
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String email=etemail.getText().toString().trim();
        final String cnic=etcnic.getText().toString().trim();

        etcnic.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()<=13){
                    ivIconCnic.setImageResource(R.drawable.ttick);
                    btnSignup.setEnabled(true);
                    btnNext.setEnabled(true);

                }
                else
                {
                    ivIconCnic.setImageResource(R.drawable.tcross);
                    btnSignup.setEnabled(false);
                    btnNext.setEnabled(false);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        etemail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches() && s.length() > 0) {
                    ivIconEmail.setImageResource(R.drawable.ttick);
                    btnSignup.setEnabled(true);
                    btnNext.setEnabled(true);
                }
                else {
                    btnSignup.setEnabled(false);
                    btnNext.setEnabled(false);
                    ivIconEmail.setImageResource(R.drawable.tcross);
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
        etusername.setOnFocusChangeListener(this);



    }
    public void setTickIcon(){
        // Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT).show();
        ivIconUsername.setImageResource(R.drawable.ttick);

    }
    public void setCrossIcon(){
        // Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT).show();
        ivIconUsername.setImageResource(R.drawable.tcross);

    }

    public void taskUsername()
    {
        String json = "";
        String uname=etusername.getText().toString().trim();

        // 3. build jsonObject
        // JSONObject jsonObject = new JSONObject();

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("uname", uname);

        String url = "http://192.168.10.10:8081/checkusername/";

        JsonObjectRequest jsObjectRequest=new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean msg = response.getBoolean("message");
                    if (msg) {
                        setCrossIcon();
                    } else {
                        setTickIcon();
                    }
                }catch (JSONException j)
                {
                    j.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(signup1.this, "Something went wrong", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        MySingleton.getInstance(signup1.this).addToRequestQueue(jsObjectRequest);


    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
    /* When focus is lost check that the text field
    * has valid values.
    */
        if (!hasFocus) {
            if(!etusername.getText().toString().equals(""))
                taskUsername();
        }
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btnReg:
                if(!validate())
                    Toast.makeText(getBaseContext(), "Fill all the fields!", Toast.LENGTH_LONG).show();
                    // call AsynTask to perform network operation on separate thread
                else {
                    // userdata = new UserData();
                    userdata.setUname(etusername.getText().toString());
                    userdata.setFname(etfirstname.getText().toString());
                    userdata.setLname(etlastname.getText().toString());
                    userdata.setEmail(etemail.getText().toString());
                    userdata.setPassword(etpassword.getText().toString());
                    userdata.setPhno(etphno.getText().toString());
                    userdata.setCnic(etcnic.getText().toString());
                    userdata.setSecretque(spinSecretQue.getSelectedItem().toString());
                    userdata.setSecretans(etsecretanswer.getText().toString());
                    userdata.setCategory(spinCategory.getSelectedItem().toString());
                    taskSignup(userdata);
                    break;
                }

        }

    }
    public void callToast(String result){
        if(result.equals("true")){
            Toast.makeText(this, "Account created!", Toast.LENGTH_LONG).show();
            final Intent intent = new Intent(this, Login.class);
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(3500); // As I am using LENGTH_LONG in Toast
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
        else{
            Toast.makeText(this, "Account not created!", Toast.LENGTH_LONG).show();

        }
    }

    public void taskSignup(UserData userdata)
    {
        String json = "";

        // 3. build jsonObject
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("uname", userdata.getUname());
        postParam.put("lname", userdata.getLname());
        postParam.put("fname", userdata.getFname());
        postParam.put("password", userdata.getPassword());
        postParam.put("email",userdata.getEmail());
        postParam.put("phoneno", userdata.getPhno());
        postParam.put("secretans", userdata.getSecretans());
        postParam.put("secretque", userdata.getSecretque());
        postParam.put("category", userdata.getCategory());
        postParam.put("cnic", userdata.getCnic());
        postParam.put("longitude", userdata.getLongitude());
        postParam.put("latitude", userdata.getLatitude());

        String url="http://192.168.10.10:8081/register/";

        JsonObjectRequest jsObjectRequest=new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                callToast("true");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(signup1.this, "Something went wrong", Toast.LENGTH_LONG).show();
                callToast("false");
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        MySingleton.getInstance(signup1.this).addToRequestQueue(jsObjectRequest);


    }

    private boolean validate(){
        if(etusername.getText().toString().trim().equals(""))
            return false;
        else if(etlastname.getText().toString().trim().equals(""))
            return false;
        else if(etfirstname.getText().toString().trim().equals(""))
            return false;
        else if(etemail.getText().toString().trim().equals(""))
            return false;
        else if(etpassword.getText().toString().trim().equals(""))
            return false;
        else if(etphno.getText().toString().trim().equals(""))
            return false;
        else if(etcnic.getText().toString().trim().equals(""))
            return false;
        else if(etsecretanswer.getText().toString().trim().equals(""))
            return false;
        else if(userdata.latitude==null || userdata.longitude==null){
            Toast.makeText(this, "Enter Location!", Toast.LENGTH_LONG).show();
            return false;
        }

        else
            return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.hasExtra("latitude") && data.hasExtra("longitude")) {
                btnLoc.setText("Location set!!");
                userdata.setLongitude(String.valueOf(data.getExtras().getDouble("longitude")));
                userdata.setLatitude(String.valueOf(data.getExtras().getDouble("latitude")));
            }
        }
        else
        {
            userdata.latitude=null;
            userdata.longitude=null;
        }
    }
}
