package com.example.amna.fyp;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by kiransaeed507 on 1/10/2017.
 */
public class CustomerProfile extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private Customer customer,c;
    private Button editButton;
    private Button needHelpButton;
    private TextView tvFName, tvLName, tvUName, tvPhoneNo, tvCNIC, tvEmail, tvAddress;
    BottomBar bottomBar;

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
                editor.apply();
                Intent i = new Intent(getBaseContext(), Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.three_buttons_activity);

        bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setItemsFromMenu(R.menu.customer_menu, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {

                switch (itemId) {
                    case R.id.notification:
                        Intent i = new Intent(getBaseContext(), Notification.class);
                        //   startActivity(i);
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
        bottomBar.setDefaultTabPosition(0);
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

        String latitude = customer.getLatitude();
        String longitude = customer.getLongitude();
        double lat = Double.parseDouble(latitude);
        double log = Double.parseDouble(longitude);

        String add = getCompleteAddressString(lat, log);

        /*Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try
        {
            addresses = geocoder.getFromLocation(lat, log, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current address", "Canont get Address!");
        }*/


        tvFName.setText(customer.getFname()+" "+customer.getLname());
        //   tvLName.setText(customer.getLname());
        tvUName.setText(customer.getUname());
        tvPhoneNo.setText(customer.getPhno());
        tvCNIC.setText(customer.getCnic());
        tvEmail.setText(customer.getEmail());

        tvAddress.setText(add);
        //tvAddress.setText(addresses);

        Customer.setUserInfo(customer);
    }

    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        //String strAdd = "";
        StringBuilder result = new StringBuilder();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            /*if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current address", "Canont get Address!");
        }
        return strAdd;*/

            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                if (address.getLocality() != null && address.getLocality().length() > 0) {
                    result.append(address.getLocality()).append(",");
                }
                if (address.getAdminArea() != null && address.getAdminArea().length() > 0) {
                    result.append(address.getAdminArea()).append("\n");
                }
                if (address.getCountryCode() != null && address.getCountryCode().length() > 0) {
                    result.append(address.getCountryCode()).append(",");
                }
                if (address.getCountryName() != null && address.getCountryName().length() > 0) {
                    result.append(address.getCountryName()).append(",");
                }
                if (address.getFeatureName() != null && address.getFeatureName().length() > 0) {
                    result.append(address.getFeatureName()).append("\n");
                }
                if (address.getPostalCode() != null && address.getPostalCode().length() > 0) {
                    result.append(address.getPostalCode());
                }

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();


    }

    @Override
    protected void onResume(){
        super.onResume();
        bottomBar.setDefaultTabPosition(0);
        customer=Customer.getUserInfo();
        tvFName.setText(customer.getFname()+" "+customer.getLname());
        tvUName.setText(customer.getUname());
        tvPhoneNo.setText(customer.getPhno());
        tvCNIC.setText(customer.getCnic());
        tvEmail.setText(customer.getEmail());

        String latitude = customer.getLatitude();
        String longitude = customer.getLongitude();
        double lat = Double.parseDouble(latitude);
        double log = Double.parseDouble(longitude);

        String add = getCompleteAddressString(lat, log);

        tvAddress.setText(add);

    }

}
