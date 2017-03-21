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
public class Ask_username extends AppCompatActivity {
    EditText etUname;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_username);
        etUname=(EditText)findViewById(R.id.etUsername);
        btnOk=(Button)findViewById(R.id.btnSend);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //new HttpAsyncTaskSignupGetSecretQue().execute("");
                details();
            }
        });
    }

    public void details()
    {
        String json = "";
        String uname=etUname.getText().toString().trim();
        UserData userdata=new UserData();

        Map<String, String> postParam= new HashMap<String, String>();
        userdata.setUname(etUname.getText().toString());
        postParam.put("uname", userdata.getUname());
       // postParam.put("uname", etUname.getText().toString());

        String url="http://192.168.0.7:8000/getsecretqueans/";


        JsonObjectRequest jsObjectRequest=new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String secQues=null;
                try {
                    secQues=response.getString("SecretQuestion");

                }catch (JSONException j){
                    j.printStackTrace();
                }
                if(secQues.equals(""))
                    callToast();
                else
                    openNextpage(secQues);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Ask_username.this, "Something went wrong", Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(Ask_username.this).addToRequestQueue(jsObjectRequest);


    }

    public void callToast()
    {
        Toast.makeText(this, "Username doesn't exists!", Toast.LENGTH_LONG).show();
    }
    public void openNextpage(String secQues)
    {
        Intent i = new Intent(getBaseContext(),Ask_secret_ans.class);
        i.putExtra("secretQues",secQues);
        i.putExtra("uname",etUname.getText().toString());
        startActivity(i);
    }

}