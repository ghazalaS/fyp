package com.example.amna.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NewShalimarComputer on 9/20/2016.
 */
public class Ask_newpassword extends AppCompatActivity {
    EditText etNewpass1;
    Button btnOk;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password);
        etNewpass1=(EditText)findViewById(R.id.etNewPass);
        btnOk=(Button)findViewById(R.id.btnSend);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // new HttpAsyncTaskChangePassword().execute("");
                details();
            }
        });
    }

    public void details()
    {
        String json = "";
        String uname= getIntent().getExtras().getString("uname");
        String NewPass=etNewpass1.getText().toString().trim();
        userData=new UserData();

       // UserData userdata=new UserData();

        Map<String, String> postParam= new HashMap<String, String>();
        userData.setUname(uname);
        postParam.put("uname", userData.getUname());
        postParam.put("password", NewPass);
        // userdata.setPassword(etNewpass.getText().toString());


        String url ="https://sheltered-tor-47307.herokuapp.com/changepassword/" ;

        JsonObjectRequest jsObjectRequest=new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean message=false;
                try {
                    message=response.getBoolean("message");

                }catch (JSONException j){
                    j.printStackTrace();
                }
                if(message==false)
                    callToast();
                else
                    openNextpage();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Ask_newpassword.this, "Something went wrong", Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(Ask_newpassword.this).addToRequestQueue(jsObjectRequest);
    }


    public void callToast()
    {
        Toast.makeText(this, "Password not changed!", Toast.LENGTH_LONG).show();
    }

    public void openNextpage()
    {

        finish();
    }

}
