package com.example.amna.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NewShalimarComputer on 1/9/2017.
 */
public class EditCustomerProfile extends AppCompatActivity {
    EditText etUName,etFName,etLName,etPass,etEmail,etPhone;
    Button btnLoc;
    private TextView tvDone;
    private String message,password,uname;
    private ImageView ivIconEmail;
    private ImageView ivIconUsername;
    private ImageView ivShowPassword,ivShowPasswordOff;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_customer_profile);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("RepairHub");

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        password= sharedPreferences.getString("password", null);
        uname= sharedPreferences.getString("uname",null);

        customer=(Customer) getIntent().getSerializableExtra("data");


        etUName=(EditText)findViewById(R.id.etUName);
        etFName=(EditText)findViewById(R.id.etFName);
        etLName=(EditText)findViewById(R.id.etLName);
        etPass=(EditText)findViewById(R.id.etPass);
        etPhone=(EditText)findViewById(R.id.etPhone);
        etEmail=(EditText)findViewById(R.id.etEmail);
        ivIconEmail=(ImageView)findViewById(R.id.ivIconEmail);
        ivIconUsername=(ImageView)findViewById(R.id.ivIconUsername);
        ivShowPassword=(ImageView)findViewById(R.id.ivShowPassword);
        ivShowPasswordOff=(ImageView)findViewById(R.id.ivShowPasswordOff);

        etUName.setText(uname,TextView.BufferType.EDITABLE);
        etFName.setText(customer.getFname(),TextView.BufferType.EDITABLE);
        etLName.setText(customer.getLname(),TextView.BufferType.EDITABLE);
        etPhone.setText(customer.getPhno(),TextView.BufferType.EDITABLE);
        etEmail.setText(customer.getEmail(),TextView.BufferType.EDITABLE);
        etPass.setText(password,TextView.BufferType.EDITABLE);
        //btnLoc.setText("Location!");

        btnLoc=(Button)findViewById(R.id.btnLoc);
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                startActivityForResult(intent, 1);

            }
        });

        tvDone = (TextView) mCustomView.findViewById(R.id.tvDone);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate())
                    Toast.makeText(getBaseContext(), "Fill all the fields!", Toast.LENGTH_LONG).show();
                else {
                    startRequest();
                }
            }
        });

        TextView tvCancel = (TextView) mCustomView.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Data not saved!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getBaseContext(), CustomerProfile.class);
                i.putExtra("data", customer);
                startActivity(i);
                finish();
                //System.exit(0);
            }
        });

        ivShowPasswordOff.setVisibility(View.GONE);
        ivShowPassword.setVisibility(View.VISIBLE);

        ivShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivShowPasswordOff.setVisibility(View.VISIBLE);
                ivShowPassword.setVisibility(View.GONE);
                etPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                //etPass.setSelection(etPass.length());
            }
        });

        ivShowPasswordOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivShowPasswordOff.setVisibility(View.GONE);
                ivShowPassword.setVisibility(View.VISIBLE);
                etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                //etPass.setSelection(etPass.length());
            }
        });

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String email=etEmail.getText().toString().trim();
        etEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches() && s.length() > 0) {
                    ivIconEmail.setImageResource(R.drawable.ttick);
                    tvDone.setEnabled(true);
                } else {
                    tvDone.setEnabled(false);
                    ivIconEmail.setImageResource(R.drawable.tcross);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });

        etUName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!etUName.getText().toString().equals("")) {
                        if (customer.getUname().equals(etUName.getText().toString())){
                            ivIconUsername.setImageResource(R.drawable.ttick);
                            tvDone.setEnabled(true);
                        }
                        else{
                            checkUsername();
                        }
                    }
                }
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    private void checkUsername() {
        String url = "https://sheltered-tor-47307.herokuapp.com/checkusername/";
        Map<String, String> params = new HashMap<String, String>();
        //params.put("uname", customerData.getUname());
        String uname=etUName.getText().toString();
        params.put("uname", uname);
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest (Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean msg=response.getBoolean("message");
                            if(msg){
                                ivIconUsername.setImageResource(R.drawable.tcross);
                                tvDone.setEnabled(false);

                            }
                            else if(msg==false){
                                ivIconUsername.setImageResource(R.drawable.ttick);
                                tvDone.setEnabled(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Server side error occured!", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void startRequest() {
        String url = "https://sheltered-tor-47307.herokuapp.com/EditCustomerProfile/";
        Map<String, String> params = new HashMap<String, String>();

        params.put("unamePrevious", customer.getUname());
        params.put("uname", etUName.getText().toString());
        params.put("fname", etFName.getText().toString());
        params.put("lname", etLName.getText().toString());
        params.put("password", etPass.getText().toString());
        params.put("phoneno", etPhone.getText().toString());
        params.put("email", etEmail.getText().toString());
        params.put("longitude", customer.getLongitude());
        params.put("latitude", customer.getLatitude());

        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest postRequest = new JsonObjectRequest (Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean msg=response.getBoolean("message");
                            if(msg){
                                message="ProfileEdited";
                                openNextpage();
                            }

                            else if(msg==false){
                                message="ProfileNotEdited";
                                openNextpage();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Server side error occured!", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    public void openNextpage()
    {
        if(message=="ProfileNotEdited") {
            Toast.makeText(getBaseContext(), "Data is not Saved!", Toast.LENGTH_LONG).show();
        }
        else// if(message=="ProfileEdited")
        {
            if(!password.equals(etPass.getText().toString())){
                Toast.makeText(getBaseContext(), "Password is change. Login again!", Toast.LENGTH_LONG).show();
                final Intent i = new Intent(getBaseContext(), Login.class);
                startActivity(i);
                finish();
            }

            if(password.equals(etPass.getText().toString())){
                customer.setUname(etUName.getText().toString());
                customer.setFname(etFName.getText().toString());
                customer.setLname(etLName.getText().toString());
                customer.setPhno(etPhone.getText().toString());
                customer.setEmail(etEmail.getText().toString());
                Customer.setUserInfo(customer);

                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                editor.putString("uname", customer.getUname());
                editor.commit();

                Toast.makeText(getBaseContext(), "Profile Updated!", Toast.LENGTH_LONG).show();
                //final Intent i = new Intent(getBaseContext(), CustomerProfile.class);
                //i.putExtra("data", customer);
                // startActivity(i);
                finish();
            }
        }
    }

    private boolean validate(){
        if(etUName.getText().toString().trim().equals(""))
            return false;
        else if(etFName.getText().toString().trim().equals(""))
            return false;
        else if(etLName.getText().toString().trim().equals(""))
            return false;
        else if(etEmail.getText().toString().trim().equals(""))
            return false;
        else if(etPass.getText().toString().trim().equals(""))
            return false;
        else if(etPhone.getText().toString().trim().equals(""))
            return false;
        else if(customer.latitude==null || customer.longitude==null){
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
                customer.setLongitude(String.valueOf(data.getExtras().getDouble("longitude")));
                customer.setLatitude(String.valueOf(data.getExtras().getDouble("latitude")));
            }
        }
        else
        {
            customer.latitude=null;
            customer.longitude=null;
        }
    }

}
