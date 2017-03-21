package com.example.amna.fyp;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

/**
 * Created by kiransaeed507 on 1/10/2017.
 */
public class CustomerProfile extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private Customer customer,c;
    private Button editButton;
    private Button needHelpButton;
    private TextView tvFName, tvLName, tvUName, tvPhoneNo, tvCNIC, tvEmail, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);
        tvFName=(TextView)findViewById(R.id.tvFName);
        //    tvLName=(TextView)findViewById(R.id.tvLName);
        tvUName=(TextView)findViewById(R.id.tvUName);
        tvPhoneNo=(TextView)findViewById(R.id.tvPhoneNo);
        tvCNIC=(TextView)findViewById(R.id.tvCNIC);
        //   tvExpertise=(TextView)findViewById(R.id.tvExpertise);
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        tvAddress=(TextView)findViewById(R.id.tvAddress);

        customer=(Customer)getIntent().getSerializableExtra("data");

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
                finish();
            }
        });
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.three_buttons_activity);

        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.customer_menu, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                if(itemId==R.id.notification)
                {
                    Intent i = new Intent(getApplicationContext(), Notification.class);
                    startActivity(i);
                }
                switch (itemId) {
                    case R.id.notification:
                        Intent i = new Intent(getBaseContext(), Notification.class);
                        startActivity(i);
                        break;

                    case R.id.favourite:
                        Intent i1 = new Intent(getBaseContext(), EditFavourites.class);
                        i1.putExtra("uname", customer.uname);
                        startActivity(i1);
                        break;
                }
            }
        });

        // Set the color for the active tab. Ignored on mobile when there are more than three tabs.
        bottomBar.setActiveTabColor("#C2185B");

        editButton=(Button)findViewById(R.id.btnEdit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), EditCustomerProfile.class);
                i.putExtra("data", customer);
                startActivity(i);
            }
        });
        needHelpButton=(Button)findViewById(R.id.btnNeedHelp);
        needHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), NeedHelp.class);
                startActivity(i);
            }
        });

        tvFName.setText(customer.getFname()+" "+customer.getLname());
        //   tvLName.setText(customer.getLname());
        tvUName.setText(customer.getUname());
        tvPhoneNo.setText(customer.getPhno());
        tvCNIC.setText(customer.getCnic());
        tvEmail.setText(customer.getEmail());

        Customer.setUserInfo(customer);
    }

    @Override
    protected void onResume(){
        super.onResume();
        customer=Customer.getUserInfo();
        tvFName.setText(customer.getFname()+" "+customer.getLname());
        tvUName.setText(customer.getUname());
        tvPhoneNo.setText(customer.getPhno());
        tvCNIC.setText(customer.getCnic());
        tvEmail.setText(customer.getEmail());
    }

}
