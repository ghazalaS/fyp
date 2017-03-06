package com.example.amna.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by NewShalimarComputer on 1/9/2017.
 */
public class EditExperties extends AppCompatActivity {
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

    private ArrayAdapter<String> arExp;
    private ArrayList<String> alExp = new ArrayList<String>();
    private ArrayList<String> otherExp = new ArrayList<String>();
    Repairer repairer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_delete_exp);

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

     /*   checkBoxPainter.setOnClickListener(this);
        checkBoxConstructor.setOnClickListener(this);
        checkBoxApplianceRepairer.setOnClickListener(this);
        checkBoxCleaningService.setOnClickListener(this);
        checkBoxWoodwork.setOnClickListener(this);
        checkBoxLandscaping.setOnClickListener(this);
        checkBoxMasonary.setOnClickListener(this);
        checkBoxElectricalService.setOnClickListener(this);
        checkBoxHomeImprovement.setOnClickListener(this);
        checkBoxPlumber.setOnClickListener(this);*/

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
                if(!validate())
                    Toast.makeText(getBaseContext(), "Fill all the fields!", Toast.LENGTH_LONG).show();
                else
                    getAllExp();
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
        String expertise[]=null;
        expertise=getIntent().getStringArrayExtra("expertise");
        for(int i=0;i<expertise.length;i++){
            if(expertise[i].equals("Painter"))
                checkBoxPainter.setChecked(true);
            if(expertise[i].equals("Constructor"))
                checkBoxConstructor.setChecked(true);
            if(expertise[i].equals("Appliance Repairer"))
                checkBoxApplianceRepairer.setChecked(true);
            if(expertise[i].equals("Cleaning Service"))
                checkBoxCleaningService.setChecked(true);
            if (expertise[i].equals("Wood Work"))
                checkBoxWoodwork.setChecked(true);
            if(expertise[i].equals("Landscaping"))
                checkBoxLandscaping.setChecked(true);
            if(expertise[i].equals("Masonry"))
                checkBoxMasonary.setChecked(true);
            if(expertise[i].equals("Electrical Service"))
                checkBoxElectricalService.setChecked(true);
            if(expertise[i].equals("Home Improvement"))
                checkBoxHomeImprovement.setChecked(true);
            if(expertise[i].equals("Plumber"))
                checkBoxPlumber.setChecked(true);
            if(!expertise[i].equals("Plumber")&&!expertise[i].equals("Home Improvement")&&!expertise[i].equals("Electrical Service")&&!expertise[i].equals("Masonry")&&!expertise[i].equals("Landscaping")&&!expertise[i].equals("Wood Work")&&!expertise[i].equals("Cleaning Service")&&!expertise[i].equals("Appliance Repairer")&&!expertise[i].equals("Constructor")&&!expertise[i].equals("Painter"))
                otherExp.add(expertise[i]);
        }
        String s=android.text.TextUtils.join(",", otherExp);
        etOther.setText(s);
    }
  /*  @Override
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
                    alExp.add("Electric Service");
                else
                    alExp.remove("Electric Service");
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

    }*/
    void getAllExp(){
        if(checkBoxPainter.isChecked())
            alExp.add("Painter");
        if(checkBoxPlumber.isChecked())
            alExp.add("Plumber");
        if(checkBoxApplianceRepairer.isChecked())
        alExp.add("Appliance Repairer");
        if(checkBoxConstructor.isChecked())
        alExp.add("Constructor");
        if(checkBoxElectricalService.isChecked())
        alExp.add("Electrical Service");
        if(checkBoxHomeImprovement.isChecked())
        alExp.add("Home Improvement");
        if(checkBoxLandscaping.isChecked())
        alExp.add("Landscaping");
        if(checkBoxMasonary.isChecked())
        alExp.add("Masonry");
        if(checkBoxCleaningService.isChecked())
        alExp.add("Cleaning Service");
        if(checkBoxWoodwork.isChecked())
        alExp.add("Wood Work");

        String totalExp = "";
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
        startRequest(temp);
    }

    public boolean validate()
    {

        if(checkBoxCleaningService.isChecked()==false && checkBoxMasonary.isChecked()==false &&
                checkBoxElectricalService.isChecked()==false && checkBoxPlumber.isChecked()==false &&
                checkBoxHomeImprovement.isChecked()==false && checkBoxApplianceRepairer.isChecked()==false &&
                checkBoxConstructor.isChecked()==false && checkBoxLandscaping.isChecked()==false &&
                checkBoxPainter.isChecked()==false && checkBoxWoodwork.isChecked()==false )
            return false;

        else
            return true;
    }
    public void startRequest(String []arr){
        SharedPreferences prefs = getBaseContext().getSharedPreferences("user", 0);
        String rname="";
        rname = prefs.getString("uname", "No name defined");
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            rname = prefs.getString("uname", "No name defined");//"No name defined" is the default value.

        }
        String url = "http://192.168.10.10:8081/addDelExpertise/";
        JSONObject obj = new JSONObject();
        try{
            obj.put("r_username",rname);
            obj.put("expertise",arr[0]);
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
                                Toast.makeText(getBaseContext(), "Expertise updated", Toast.LENGTH_LONG).show();
                                final Intent intent = new Intent(getBaseContext(), RepairerProfile.class);
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
