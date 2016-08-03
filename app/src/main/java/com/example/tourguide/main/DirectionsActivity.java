package com.example.tourguide.main;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tourguide.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DirectionsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double destinationLat, destinationLng;
    private String searchedName;
    private Button btnDirections;
    private double currentLng, currentLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnDirections = (Button) findViewById(R.id.btnDirections);
        btnDirections.setOnClickListener(this.getDirections());
    }

        private View.OnClickListener getDirections() {

            View.OnClickListener viewListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uriString = "http://maps.google.com/maps?saddr=" + currentLat + "," + currentLng + "&daddr=" + destinationLat + "," + destinationLng + "&dirflg=d&layer=t";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(uriString));
                    Log.i("DEBUG", uriString);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            };
            return viewListener;
        }

    private void receiveIntents() {
            // TODO: Fix the issue with the back button
            Intent intent = getIntent();
        currentLat = intent.getDoubleExtra("currentLat", 0);
        currentLng = intent.getDoubleExtra("currentLng", 0);

//            this.businessModelList = intent.getParcelableArrayListExtra("MyObj");
            destinationLat = intent.getDoubleExtra("latitude", 0);
            destinationLng = intent.getDoubleExtra("longitude", 0);
            searchedName = intent.getStringExtra("name");
//            searchedAddress = intent.getStringArrayExtra("address");
//            Log.i(TAG, "Address######" + searchedAddress);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        receiveIntents();
        setMarkers(destinationLat,destinationLng,searchedName);
    }


    public void setMarkers(double lat, double lng, String properAddress) {

        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(lat, lng);
        markerOptions.position(latLng);
        markerOptions.title(properAddress);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        Marker tempMarker = mMap.addMarker(markerOptions);

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 18);
        mMap.animateCamera(cameraUpdate);
    }
}
