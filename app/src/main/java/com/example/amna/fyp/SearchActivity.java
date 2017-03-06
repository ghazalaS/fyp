package com.example.amna.fyp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

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

/**
 * Created by amna on 1/11/2017.
 */
public class SearchActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    ListView list1;
    String[] usernameTitles;
    String[] ratingsDescriptions;
    public Repairer r;
    boolean isFav;
    String cname1;
    TextView tvNoResult;
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.search_results);
        tvNoResult=(TextView)findViewById(R.id.noResult);

        usernameTitles=getIntent().getStringArrayExtra("rname");
        ratingsDescriptions=getIntent().getStringArrayExtra("rating");
        SharedPreferences prefs = getBaseContext().getSharedPreferences("user", 0);
        cname1= prefs.getString("uname", "No name defined");

        if(usernameTitles.length==0) {
            tvNoResult.setText("No results found.");
        }

        list1= (ListView) findViewById(R.id.listView2);
        SearchAdapter adapter=new SearchAdapter(this, usernameTitles, ratingsDescriptions);
        list1.setAdapter(adapter);
        

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


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.three_buttons_activity);

        BottomBar bottomBar = BottomBar.attach(this, savedInstance);
        bottomBar.setItemsFromMenu(R.menu.menu_2, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                switch (itemId) {
                    case R.id.notification:
                        Intent i = new Intent(getBaseContext(), Notification.class);
                        startActivity(i);
                        break;
                    case R.id.add_favourites:
                        Intent i1 = new Intent(getBaseContext(), EditFavourites.class);
                        i1.putExtra("uname",cname1);
                        startActivity(i1);
                        break;

                }
            }
        });
        bottomBar.setActiveTabColor("#C2185B");

    }
}


class SearchAdapter extends ArrayAdapter<String> {
    Context context;
    String[] usernameArray;
    String[] ratingArray;
    int[] images;
    Repairer r;
    String[] clientList=null;
    boolean isFav;

    SearchAdapter(Context c, String[] usernames, String[] ratings)
    {
        super(c, R.layout.single_search_row, R.id.textView10, usernames);
        this.context = c;
        this.usernameArray = usernames;
        this.ratingArray = ratings;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row= inflater.inflate(R.layout.single_search_row,parent,false);

        RatingBar rb=(RatingBar)row.findViewById(R.id.ratingbar);
      //  Drawable drawable = rb.getProgressDrawable();
      //  drawable.setColorFilter(Color.parseColor("#0064A8"), PorterDuff.Mode.SRC_ATOP);
        final TextView myUsername= (TextView) row.findViewById(R.id.textView10);
        TextView myRating= (TextView) row.findViewById(R.id.textView11);

        myUsername.setText(usernameArray[position]);
        if(ratingArray[position].equals("null")) {
            rb.setStepSize(0.1f);
            rb.setRating(0);
            myRating.setText("Not rated yet!");
        }
        else {
            float f=Float.parseFloat(ratingArray[position]);
            float f1=(float)Math.round(f * 10) / 10;
            rb.setStepSize(0.1f);
            rb.setRating(f1);
            myRating.setText(String.valueOf(f1));
        }


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRequestForProfile(usernameArray[position]);
            }
        });
      //  myImage.setImageResource(images[position]);


        return row;
    }
    public void startRequestForProfile(String r_username){

        SharedPreferences prefs = getContext().getSharedPreferences("user", 0);
        String cname="";
        cname = prefs.getString("uname", "No name defined");
        String url = "http://192.168.10.10:8081/showrepairerprofile/";
        JSONObject obj = new JSONObject();
        try{
            obj.put("c_username", cname);
            obj.put("r_username",r_username);
        }catch (JSONException je){
            je.printStackTrace();
        }
        isFav=false;
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {

                            boolean message=jsonObject.getBoolean("message");
                            if(message)
                            {
                                r=new Repairer();
                                r.setFname(jsonObject.getString("fname"));
                                r.setLname(jsonObject.getString("lname"));
                                r.setUname(jsonObject.getString("uname"));
                                r.setCnic(jsonObject.getString("cnic"));
                                r.setEmail(jsonObject.getString("email"));
                                r.setPhno(jsonObject.getString("contactno"));

                                JSONArray temp = jsonObject.getJSONArray("expertise");
                                int length = temp.length();
                                if (length > 0) {
                                    r.expertise = new String[length];
                                    for (int i = 0; i < length; i++) {
                                        r.expertise[i] = temp.getString(i);
                                    }
                                }
                                temp = jsonObject.getJSONArray("reviews");
                                length = temp.length();
                                if (length > 0) {
                                    r.reviews= new String[length];
                                    for (int i = 0; i < length; i++) {
                                        r.reviews[i] = temp.getString(i);
                                    }
                                }

                                temp = jsonObject.getJSONArray("clientList");
                                length = temp.length();
                                if (length > 0) {
                                    clientList= new String[length];
                                    for (int i = 0; i < length; i++) {
                                        clientList[i] = temp.getString(i);
                                    }
                                }
                                r.setShop(jsonObject.getString("shop"));
                                r.avgRating=jsonObject.getString("avgRating");
                                isFav=jsonObject.getBoolean("isFav");
                                openNextPage();
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
        Volley.newRequestQueue(getContext()).add(jsObjRequest);
    }
    public void openNextPage()
    {
        Intent i = new Intent(getContext(), ShowRepairerProfileToCustomer.class);
        i.putExtra("data", r);
        i.putExtra("isFav",isFav);
        if(clientList!=null){
            i.putExtra("clientList", clientList);
        }
        else {
            i.putExtra("clientList", "");
        }
        context.startActivity(i);
    }


}
