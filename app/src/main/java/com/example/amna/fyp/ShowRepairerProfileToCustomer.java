package com.example.amna.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amna on 1/11/2017.
 */
public class ShowRepairerProfileToCustomer extends AppCompatActivity {
    private TextView tvFName, tvUName, tvExperties, tvEmail, tvAvgRating;
    private Repairer repairer;
    private CoordinatorLayout coordinatorLayout;
    ListView list;
    String[] usernameTitles;
    String[] reviewsDescriptions;
    private ArrayList<String > alExp;
    boolean isFav;
    Customer c;
    Customer c1;
    BottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.repairer_profile_show_to_customer);
        c1 = new Customer();
        tvFName=(TextView)findViewById(R.id.tvFName);
        tvExperties=(TextView)findViewById(R.id.tvExpertise);
        tvUName=(TextView)findViewById(R.id.tvUName);
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        tvAvgRating=(TextView)findViewById(R.id.tvAvgRating);
        repairer=(Repairer)getIntent().getSerializableExtra("data");
        tvFName.setText(repairer.getFname());
        tvEmail.setText(repairer.getEmail());
      //  tvUName.setText(repairer.getUname());
        tvUName.setText(repairer.getUname());
        String avgRating=repairer.getAvgRating();
        if(avgRating.equals("null")) {
            tvAvgRating.setText("Not rated yet!");
        }
        else{
            tvAvgRating.setText(repairer.getAvgRating());
        }
        tvEmail.setText(repairer.getEmail());
        alExp = new ArrayList<String>();
        int l=repairer.expertise.length;
        String experties=android.text.TextUtils.join(",", repairer.expertise);
        tvExperties.setText(experties);



        usernameTitles=new String[getIntent().getExtras().getStringArray("clientList").length];
        usernameTitles=getIntent().getExtras().getStringArray("clientList");
        int len=getIntent().getExtras().getStringArray("clientList").length;
        reviewsDescriptions=new String[repairer.reviews.length];
        reviewsDescriptions=repairer.reviews;
        if(usernameTitles[0].equals(""))
            usernameTitles[0]="No reviews yet";
        isFav=getIntent().getBooleanExtra("isFav",false);

        list= (ListView) findViewById(R.id.listView);
        Adapter adapter=new Adapter(this, usernameTitles, reviewsDescriptions);
        list.setAdapter(adapter);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.three_buttons_activity);

        bottomBar = BottomBar.attach(this, savedInstance);
        if(isFav){
            bottomBar.setItemsFromMenu(R.menu.menu_1, new OnMenuTabSelectedListener() {

                @Override
                public void onMenuItemSelected(int itemId) {
                    switch (itemId) {

                        case R.id.add_favourites:
                            Toast.makeText(getBaseContext(), "Already in your favourites list!", Toast.LENGTH_LONG).show();
                            break;

                        case R.id.home:
                            Details();
                            break;

                    }
                }
            });

    }
        else {
            bottomBar.setItemsFromMenu(R.menu.menu_1, new OnMenuTabSelectedListener() {
                @Override
                public void onMenuItemSelected(int itemId) {
                    switch (itemId) {
                        case R.id.add_favourites:
                            startAddFavouriteRequest();
                            break;

                        case R.id.home:
                            Details();
                            break;
                    }
                }
            });
        }
        if (isFav) {
            bottomBar.setDefaultTabPosition(3);
        } else {
            bottomBar.setDefaultTabPosition(0);
        }
        bottomBar.setActiveTabColor("#C2185B");
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        //View mCustomView = mInflater.inflate(R.layout.logout_action_bar, null);
        View mCustomView = mInflater.inflate(R.layout.logout_back_action_bar, null);
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
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        ImageView ivBack=(ImageView) mCustomView.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
             /*   Intent i=new Intent(getBaseContext(),NeedHelp.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);*/
                finish();
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }
    public void startAddFavouriteRequest(){
        SharedPreferences prefs = getBaseContext().getSharedPreferences("user", 0);
        String cname="";
        cname = prefs.getString("uname", "No name defined");
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            cname = prefs.getString("uname", "No name defined");//"No name defined" is the default value.

        }
        String url = "https://sheltered-tor-47307.herokuapp.com/addfavourite/";
        JSONObject obj = new JSONObject();
        try{
            obj.put("c_username", cname);
            obj.put("r_username",repairer.getUname());
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
                                Toast.makeText(getBaseContext(), "Added to favourites!", Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(getBaseContext()).add(jsObjRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFav) {
            bottomBar.setDefaultTabPosition(3);
        } else {
            bottomBar.setDefaultTabPosition(0);
        }
    }

    public void Details() {
        Map<String, String> postParam = new HashMap<String, String>();

        SharedPreferences prefs = getBaseContext().getSharedPreferences("user", 0);
        String cname1 = prefs.getString("uname", "No name defined");
        String passwd = prefs.getString("password", "No password defined");
        postParam.put("uname", cname1);
        postParam.put("password", passwd);
        String url = "https://sheltered-tor-47307.herokuapp.com/getprofile/";
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        boolean msg = false;
                        try {
                            msg = response.getBoolean("message");
                            if (msg) {

                                c1.setFname(response.getString("fname"));
                                c1.setLname(response.getString("lname"));
                                c1.setUname(response.getString("uname"));
                                c1.setCnic(response.getString("cnic"));
                                c1.setEmail(response.getString("email"));
                                c1.setPhno(response.getString("contactno"));
                                c1.setLongitude(response.getString("longitude"));
                                c1.setLatitude(response.getString("latitude"));
                                JSONArray temp = response.getJSONArray("favourites");
                                int length = temp.length();
                                if (length > 0) {
                                    c1.favourites = new String[length];
                                    for (int i = 0; i < length; i++) {
                                        c1.favourites[i] = temp.getString(i);
                                    }
                                }
                                temp = response.getJSONArray("reviews");
                                length = temp.length();
                                if (length > 0) {
                                    c1.reviews = new String[length];
                                    for (int i = 0; i < length; i++) {
                                        c1.reviews[i] = temp.getString(i);
                                    }
                                }
                                c1.avgRating = response.getString("avgRating");
                            }

                        } catch (JSONException j) {
                            j.printStackTrace();
                        }
                        if (msg) {
                            Intent i1 = new Intent(getBaseContext(), CustomerProfile.class);
                            i1.putExtra("data", c1);
                            i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i1);
                            finish();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowRepairerProfileToCustomer.this, "Something went wrong", Toast.LENGTH_LONG).show();
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

        MySingleton.getInstance(ShowRepairerProfileToCustomer.this).addToRequestQueue(jsObjectRequest);

    }
}

class Adapter extends ArrayAdapter<String>
{
    Context context;
    String[] usernameArray;
    String[] reviewArray;
    Adapter(Context c, String[] usernames, String[] reviews)
    {
        super(c, R.layout.single_review_row, R.id.textView7, usernames );
        this.context=c;
        this.usernameArray=usernames;
        this.reviewArray=reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row= inflater.inflate(R.layout.single_review_row,parent,false);

        TextView myUsername= (TextView) row.findViewById(R.id.textView7);
        TextView myReview= (TextView) row.findViewById(R.id.textView8);

        myUsername.setText(usernameArray[position]);
        myReview.setText(reviewArray[position]);


        return row;
    }

}
