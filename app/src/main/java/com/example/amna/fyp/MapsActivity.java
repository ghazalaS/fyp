package com.example.amna.fyp;

/**
 * Created by NewShalimarComputer on 9/23/2016.
 */
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by NewShalimarComputer on 9/22/2016.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    MarkerOptions markerOptions;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /*supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting a reference to the map
        supportMapFragment.getMapAsync(this);

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);
            }
        });*/

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        setUpMap();
        // Enabling MyLocation Layer of Google Map
        // googleMap.setMyLocationEnabled(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (googleMap == null) {

            // Try to obtain the map from the SupportMapFragment.

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(MapsActivity.this, "Getting your location...", Toast.LENGTH_LONG).show();
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                }
            }

            supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(this);
        }
    }
    public void goToSignup(LatLng loc){
        Intent data = new Intent();
        data.putExtra("latitude",loc.latitude);
        data.putExtra("longitude", loc.longitude);
// Activity finished ok, return the data
        setResult(RESULT_OK, data);
        finish();

    }
    private void setUpMap() {
         LatLng loc;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setMapToolbarEnabled(false);


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                //remove previously placed Marker
                if (marker != null) {
                    marker.remove();
                }



                //place marker where user just clicked
                marker = googleMap.addMarker(new MarkerOptions().position(point).title("Marker")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                marker.setTitle(point.latitude + " : " + point.longitude);
                markerOptions = new MarkerOptions();
                // Setting the position for the marker
                markerOptions.position(point);
                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(point.latitude + " : " + point.longitude);
                // Clears the previously touched position
                googleMap.clear();
                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(point));
                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);
                goToSignup(point);
            }
        });

    }
}
