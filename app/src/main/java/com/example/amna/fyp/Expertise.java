package com.example.amna.fyp;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amna on 2/21/2017.
 */
public class Expertise extends AppCompatActivity implements View.OnClickListener {

    private CheckBox checkBoxPainter;
    private CheckBox checkBoxConstructor;
    private CheckBox checkBoxApplianceRepairer;
    private CheckBox checkBoxCleaningService;
    private CheckBox checkBoxWoodwork;
    private CheckBox checkBoxLandscaping;
    private CheckBox checkBoxMasonary;
    private CheckBox checkBoxElectricalService;
    private CheckBox checkBoxHomeImprovement;
    private CheckBox checkBoxPlumber;
    private EditText etOther;
    private EditText etShop;
    private Button btnSignup;

    private ArrayAdapter<String> arExp;
    private ArrayList<String> alExp = new ArrayList<String>();
    private UserData userdata;
    Repairer repairer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expertise);

        userdata = new UserData();
        repairer = new Repairer();

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();

        checkBoxPainter = (CheckBox) findViewById(R.id.checkBoxPainter);
        checkBoxConstructor = (CheckBox) findViewById(R.id.checkBoxConstructor);
        checkBoxApplianceRepairer = (CheckBox) findViewById(R.id.checkBoxApplianceRepairer);
        checkBoxCleaningService = (CheckBox) findViewById(R.id.checkBoxCleaningService);
        checkBoxWoodwork = (CheckBox) findViewById(R.id.checkBoxWoodwork);
        checkBoxLandscaping = (CheckBox) findViewById(R.id.checkBoxLandscaping);
        checkBoxMasonary = (CheckBox) findViewById(R.id.checkBoxMasonary);
        checkBoxElectricalService = (CheckBox) findViewById(R.id.checkBoxElectricalService);
        checkBoxHomeImprovement = (CheckBox) findViewById(R.id.checkBoxHomeImprovement);
        checkBoxPlumber = (CheckBox) findViewById(R.id.checkBoxPlumber);
        etOther = (EditText) findViewById(R.id.etOther);
        etShop = (EditText) findViewById(R.id.etShop);
        btnSignup = (Button) findViewById(R.id.btnReg);

        checkBoxPainter.setOnClickListener(this);
        checkBoxConstructor.setOnClickListener(this);
        checkBoxApplianceRepairer.setOnClickListener(this);
        checkBoxCleaningService.setOnClickListener(this);
        checkBoxWoodwork.setOnClickListener(this);
        checkBoxLandscaping.setOnClickListener(this);
        checkBoxMasonary.setOnClickListener(this);
        checkBoxElectricalService.setOnClickListener(this);
        checkBoxHomeImprovement.setOnClickListener(this);
        checkBoxPlumber.setOnClickListener(this);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String totalExp = "";
                if (!validate())
                    Toast.makeText(getBaseContext(), "Fill all the fields!", Toast.LENGTH_LONG).show();
                    // call AsynTask to perform network operation on separate thread
                else {
                    // userdata = new UserData();

                    userdata.setUname(repairer.getUname());
                    userdata.setFname(repairer.getFname());
                    userdata.setLname(repairer.getLname());
                    userdata.setEmail(repairer.getEmail());
                    userdata.setPassword(repairer.getPassword());
                    userdata.setPhno(repairer.getPhno());
                    userdata.setCnic(repairer.getCnic());
                    userdata.setSecretque(repairer.getSecretque());
                    userdata.setSecretans(repairer.getSecretans());
                    userdata.setShop(etShop.getText().toString());
                    userdata.setCategory(repairer.getCategory());
                    userdata.setLatitude(repairer.getLatitude());
                    userdata.setLongitude(repairer.getLongitude());

                    String other = etOther.getText().toString().trim();
                    if (other.equals("")) {
                        for (String AlExp : alExp) {
                            totalExp = totalExp + AlExp + ",";
                        }

                    } else {
                        for (String AlExp : alExp) {
                            totalExp = totalExp + AlExp + ",";
                        }
                        totalExp = totalExp + other+",";
                    }

                    String[] temp = new String[10];
                    temp[0]=totalExp;

                    userdata.setExpertise(temp);
                    taskSignup(userdata);
                }
            }
            // }
        });


        repairer = (Repairer) getIntent().getSerializableExtra("data");


    }

    @Override
    public void onClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkBoxApplianceRepairer:
                if (checked)
                    alExp.add("Appliance Repairer");
                else
                    alExp.remove("Appliance Repairer");
                break;

            case R.id.checkBoxConstructor:
                if (checked)
                    alExp.add("Constructor");
                else
                    alExp.remove("Constructor");
                break;

            case R.id.checkBoxElectricalService:
                if (checked)
                    alExp.add("Electrical Service");
                else
                    alExp.remove("Electrical Service");
                break;

            case R.id.checkBoxHomeImprovement:
                if (checked)
                    alExp.add("Home Improvement");
                else
                    alExp.remove("Home Improvement");
                break;

            case R.id.checkBoxLandscaping:
                if (checked)
                    alExp.add("Landscaping");
                else
                    alExp.remove("Landscaping");
                break;

            case R.id.checkBoxPainter:
                if (checked)
                    alExp.add("Painter");
                else
                    alExp.remove("Painter");
                break;

            case R.id.checkBoxPlumber:
                if (checked)
                    alExp.add("Plumber");
                else
                    alExp.remove("Plumber");
                break;

            case R.id.checkBoxMasonary:
                if (checked)
                    alExp.add("Masonry");
                else
                    alExp.remove("Masonry");
                break;

            case R.id.checkBoxCleaningService:
                if (checked)
                    alExp.add("Cleaning Service");
                else
                    alExp.remove("Cleaning Service");
                break;

            case R.id.checkBoxWoodwork:
                if (checked)
                    alExp.add("Wood Work");
                else
                    alExp.remove("Wood Work");
                break;
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

    public void taskSignup(UserData userdata)
    {
        String json = "";
        String shop= etShop.getText().toString().trim();

        // 3. build jsonObject
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("uname", userdata.getUname());
        postParam.put("lname", userdata.getLname());
        postParam.put("fname", userdata.getFname());
        postParam.put("password", userdata.getPassword());
        postParam.put("email",userdata.getEmail());
        postParam.put("phoneno", userdata.getPhno());
        postParam.put("expertise", userdata.expertise[0]);
        postParam.put("shop", userdata.getShop());
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
                Toast.makeText(Expertise.this, "Something went wrong", Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(Expertise.this).addToRequestQueue(jsObjectRequest);


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

    public boolean validate()
    {

        if(checkBoxCleaningService.isChecked()==false && checkBoxMasonary.isChecked()==false &&
                checkBoxElectricalService.isChecked()==false && checkBoxPlumber.isChecked()==false &&
                checkBoxHomeImprovement.isChecked()==false && checkBoxApplianceRepairer.isChecked()==false &&
                checkBoxConstructor.isChecked()==false && checkBoxLandscaping.isChecked()==false &&
                checkBoxPainter.isChecked()==false && checkBoxWoodwork.isChecked()==false )
            return false;

        else if(etShop.getText().toString().trim().equals(""))
            return  false;
        else
            return true;
    }

}
