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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.repairer_profile_show_to_customer);
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

        BottomBar bottomBar = BottomBar.attach(this, savedInstance);
        if(isFav){
            bottomBar.setItemsFromMenu(R.menu.menu_1, new OnMenuTabSelectedListener() {
                @Override
                public void onMenuItemSelected(int itemId) {
                    switch (itemId) {

                        case R.id.add_favourites:
                            Toast.makeText(getBaseContext(), "Already in your favourites list!", Toast.LENGTH_LONG).show();

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

                    }
                }
            });
        }
        bottomBar.setActiveTabColor("#C2185B");

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
        String url = "http://192.168.10.10:8081/addfavourite/";
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
