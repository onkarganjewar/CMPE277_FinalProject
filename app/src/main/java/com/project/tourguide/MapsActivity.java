package com.project.tourguide;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Console;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;
    Context context = MapsActivity.this;
    private LocationManager locationManager;
    private Location location;
    private Double latitude, longitude;
    private boolean isGPSEnabled, isNetworkEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("CREATION","Map created");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("CREATION","Activity permission check");
            LatLng sydney = new LatLng(-34, 151);
    //        mMap.addMarker(new MarkerOptions().position(sydney).title("My Marker"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            Log.d("CREATION","After Sydney");
            mMap.setMyLocationEnabled(true);

            // Get LocationManager object from System Service LOCATION_SERVICE
            locationManager = (LocationManager)   context.getSystemService(context.LOCATION_SERVICE);
            Log.d("CREATION","After location manager");
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                } else {
                    Toast.makeText(
                            context,
                            "Location Null", Toast.LENGTH_SHORT).show();
                }
            }
/*
            // Create a criteria object to retrieve provider
            Criteria criteria = new Criteria();
// List all providers:
            List<String> providers = locationManager.getAllProviders();
            for (String provider : providers) {
                printProvider(provider);
            }
            // Get the name of the best provider
            String provider = locationManager.getBestProvider(criteria, false);
            Log.d("CREATION","Name of the provider"+provider);
            // Get Current Location

            Location myLocation = locationManager.getLastKnownLocation(provider);
            Log.d("CREATION","Value of mylocations");
            if (myLocation!= null)
                locationManager.requestLocationUpdates(provider, 15000, 1, this);
//            onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
            // set map type
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // Get latitude of the current location
            double latitude = myLocation.getLatitude();

            Log.d("ADebugTag", "Value: " + Double.toString(latitude));
            System.out.println("Latitude "+latitude);
            // Get longitude of the current location
            double longitude = myLocation.getLongitude();
            Log.d("tag", "Longitude: " + Double.toString(longitude));
            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Show the current location in Google Map

            // Zoom in the Google Map
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));*/
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            return;
        }
/*
        Log.d("CREATION","Before Sydney");
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("My Marker"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Log.d("CREATION","After Sydney");
        mMap.setMyLocationEnabled(true);
        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.d("CREATION","After location manager");
        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location

        Location myLocation = locationManager.getLastKnownLocation(provider);

        if (myLocation!= null)
            locationManager.requestLocationUpdates(provider, 15000, 1, this);
//            onLocationChanged(location);
        else
            Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
        // set map type
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        Log.d("ADebugTag", "Value: " + Double.toString(latitude));
        System.out.println("Latitude "+latitude);
        // Get longitude of the current location
        double longitude = myLocation.getLongitude();
        Log.d("tag", "Longitude: " + Double.toString(longitude));
        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map

        // Zoom in the Google Map
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
*/



        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }


//        mMap.addMarker(new MarkerOptions().position(sydney).title("My Marker"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    private void printProvider(String provider) {
            LocationProvider info = locationManager.getProvider(provider);
//            output.append(info.toString() + "\n\n");

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }/*
    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
            String units = "imperial";
            String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                    lat, lon, units, APP_ID);
//            new GetWeatherTask(textView).execute(url);
        }
    }*/
}
