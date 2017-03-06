package com.example.amna.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amna on 9/11/2016.
 */
public class Login extends AppCompatActivity {
    TextView tvForgetPass;
    EditText etUname;
    EditText etPassword;
    Button btnSignup;
    Button btnLogin;
    UserData userData;
    SharedPreferences editor;
    Customer c;
    Repairer r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /*    SharedPreferences prefs = getBaseContext().getSharedPreferences("user", 0);
        String cname1= prefs.getString("uname", "No name defined");
        if(!cname1.equals(""))
            Details();*/
        setContentView(R.layout.login);
        userData=new UserData();


        c=new Customer();
        r=new Repairer();

        tvForgetPass=(TextView)findViewById(R.id.tvForget_Pass);
        btnSignup=(Button)findViewById(R.id.btnSignup);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        etUname=(EditText)findViewById(R.id.etUName);
        etPassword=(EditText)findViewById(R.id.etPass);
        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ask_username.class);
                startActivity(intent);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),signup1.class);
                startActivity(i);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new HttpAsyncTaskGetProfile().execute("");
                Details();
                /*Intent i = new Intent(getBaseContext(),getProfile.class);
                i.putExtra("userInfo", userData);
                startActivity(i);*/
            }
        });

    }

    public void Details() {

        String json = "";
        String uname = etUname.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();


        // 3. build jsonObject
       // JSONObject jsonObject = new JSONObject();

        Map<String, String> postParam= new HashMap<String, String>();
        userData.setUname(etUname.getText().toString());
      /*  SharedPreferences prefs = getBaseContext().getSharedPreferences("user", 0);
        String cname1= prefs.getString("uname", "No name defined");
        if(!cname1.equals("")) {
            postParam.put("uname", prefs.getString("uname", ""));
            postParam.put("password", prefs.getString("password",""));
        }else {*/
            postParam.put("uname", userData.getUname());
            postParam.put("password", pass);
       // }


            String url = "http://192.168.10.10:8081/getprofile/";
            JsonObjectRequest jsObjectRequest=new JsonObjectRequest(Request.Method.POST, url,new JSONObject(postParam),new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            boolean msg = false;
                            try {
                                msg=response.getBoolean("message");
                                if(msg) {
                                    userData.setCategory(response.getString("category"));
                                    if(userData.category.equals("Customer")) {c.setFname(response.getString("fname"));
                                        c.setLname(response.getString("lname"));
                                        c.setUname(response.getString("uname"));
                                        c.setCnic(response.getString("cnic"));
                                        c.setEmail(response.getString("email"));
                                        c.setPhno(response.getString("contactno"));
										
										c.setLongitude(response.getString("longitude"));
										c.setLatitude(response.getString("latitude"));

                                        JSONArray temp = response.getJSONArray("favourites");
                                        int length = temp.length();
                                        if (length > 0) {
                                            c.favourites = new String[length];
                                            for (int i = 0; i < length; i++) {
                                                c.favourites[i] = temp.getString(i);
                                            }
                                        }
                                        temp = response.getJSONArray("reviews");
                                        length = temp.length();
                                        if (length > 0) {
                                            c.reviews= new String[length];
                                            for (int i = 0; i < length; i++) {
                                                c.reviews[i] = temp.getString(i);
                                            }
                                        }
                                        c.avgRating=response.getString("avgRating");

                                    }
                                    else {
                                        r.setFname(response.getString("fname"));
                                        r.setLname(response.getString("lname"));
                                        r.setUname(response.getString("uname"));
                                        r.setCnic(response.getString("cnic"));
                                        r.setEmail(response.getString("email"));
                                        r.setPhno(response.getString("contactno"));
                                        r.setShop(response.getString("shop"));
                                        r.setLongitude(response.getString("longitude"));
                                        r.setLatitude(response.getString("latitude"));

                                        JSONArray temp = response.getJSONArray("expertise");
                                        int length = temp.length();
                                        if (length > 0) {
                                            r.expertise = new String[length];
                                            for (int i = 0; i < length; i++) {
                                                r.expertise[i] = temp.getString(i);
                                            }
                                        }
                                        temp = response.getJSONArray("reviews");
                                        length = temp.length();
                                        if (length > 0) {
                                            r.reviews= new String[length];
                                            for (int i = 0; i < length; i++) {
                                                r.reviews[i] = temp.getString(i);
                                            }
                                        }
                                        r.setShop(response.getString("shop"));
                                        r.avgRating=response.getString("avgRating");
                                    }

                                }
                            }catch (JSONException j){
                                j.printStackTrace();
                            }
                            if(msg==false)
                                callToast();
                            else
                                openNextpage();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

            };

            MySingleton.getInstance(Login.this).addToRequestQueue(jsObjectRequest);

        }


    public void callToast()
    {
        Toast.makeText(this, "Username or password is incorrect!", Toast.LENGTH_LONG).show();
    }



    public void openNextpage()
    {
       // Toast.makeText(this, "Done!", Toast.LENGTH_LONG).show();
        if(userData.category.equals("Repairer")) {
            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
            editor.putString("uname", r.getUname());
            editor.putString("password", etPassword.getText().toString().trim());
            editor.putString("category", "repairer");
            editor.commit();
            Intent i = new Intent(getBaseContext(), RepairerProfile.class);
            i.putExtra("data", r);
            startActivity(i);
        }
        else {
            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
            editor.putString("uname", c.getUname());
			editor.putString("password", etPassword.getText().toString().trim());
            editor.putString("category", "client");
            editor.commit();
            Intent i = new Intent(getBaseContext(), CustomerProfile.class);
            i.putExtra("data", c);
            startActivity(i);
        }

    }
}

