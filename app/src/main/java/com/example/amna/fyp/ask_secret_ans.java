package com.example.amna.fyp;

/**
 * Created by NewShalimarComputer on 9/21/2016.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
public class Ask_secret_ans extends AppCompatActivity {
    TextView tvsecQues;
    EditText etSecAns;
    Button btnOk;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secret_qa);
        etSecAns=(EditText)findViewById(R.id.etSecAns);
        tvsecQues=(TextView)findViewById(R.id.tvSecQues);
        btnOk=(Button)findViewById(R.id.btnSend);
        String secretQues=getIntent().getStringExtra("secretQues");
        tvsecQues.setText(secretQues);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //new HttpAsyncTaskAskSecretAns().execute("");
                details();
            }
        });
    }


    public void details()
    {
        String json = "";
        String uname= getIntent().getExtras().getString("uname");
        userData=new UserData();
        userData.setUname(uname);

        Map<String, String> postParam= new HashMap<String, String>();
        userData.setUname(uname);
        postParam.put("uname", userData.getUname());
        postParam.put("secretAns", etSecAns.getText().toString());

        String url="https://sheltered-tor-47307.herokuapp.com/matchsecretans/";

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
                    openQuespage();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Ask_secret_ans.this, "Something went wrong", Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(Ask_secret_ans.this).addToRequestQueue(jsObjectRequest);

    }


    public void callToast()
    {
        Toast.makeText(this, "Answer doesn't match!", Toast.LENGTH_LONG).show();
    }

    public void openQuespage()
    {
        Intent i = new Intent(getBaseContext(),Ask_newpassword.class);

        i.putExtra("uname",userData.getUname());
        startActivity(i);
    }

}
