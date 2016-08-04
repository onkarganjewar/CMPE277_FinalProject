package com.example.tourguide.registration;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.tourguide.R;
import com.example.tourguide.yelp.YelpController;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.yelp.clientlib.connection.YelpAPI;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // controller responsible for carrying out the search
    private YelpController yelpController;
    private YelpAPI yelpAPI;
    private GoogleApiClient mGoogleApiClient;
    // UI elements
    private Button btnGo, btnSignup;
    public static int count = 0;

    // Variables to get current location
    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;
    protected Marker mCurrLocationMarker;
    Timer _t;
    // Location update intervals
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */


    // progress dialog indicating "wait"
    private ProgressDialog progress;
    ImageView img;
    final int[] setImg = {R.drawable.cafe_theme, R.drawable.likefb};
    RelativeLayout layout;
    private static int counterInterval = 0;
    int[] drawablearray = new int[]{R.drawable.w4, R.drawable.w3, R.drawable.w1, R.drawable.w5};
    private String TAG = "Search_Activity";
    private String cityName;
    private String provider;
    private double _latitude, _longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layout = (RelativeLayout) findViewById(R.id.myRelativeLayout);
        layout.setBackgroundResource(R.drawable.w3);
        // sample button to initiate search
        btnGo = (Button) findViewById(R.id.Sign_in_button);
        btnSignup = (Button) findViewById(R.id.Create_account_button);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Sign_up.class);
                startActivity(intent);
            }
        });

        _t = new Timer();
        _t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() // run on ui thread
                {
                    public void run() {
//                        for(int j=0; j<8 ; j++) {
                        if (count < drawablearray.length) {
                            layout.setBackgroundResource(drawablearray[count]);
                            setContentView(layout);
                            count = (count + 1) % drawablearray.length;
                        }
                    }
                });
            }
        }, 3000, 3000);

        buildGoogleApiClient();
        // initialize the yelp controller
        _initController();

        performSearch();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

//        mGoogleApiClient.connect();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        mGoogleApiClient.connect();
//        createLocationRequest();
    }
    private void performSearch() {

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initiate the asynchronous search on background thread

                yelpController.performSearch(yelpAPI, getApplicationContext(), cityName, _latitude, _longitude);

                // show progress bar until the action completes
                progress = new ProgressDialog(v.getContext());
                progress.setTitle("Please Wait!!");
                progress.setMessage("Wait!!");
                progress.setCancelable(true);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
            }
        });
    }

    private void getCurrentCity() {
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(_latitude, _longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0)
            cityName = addresses.get(0).getLocality();

        Log.i(TAG, "Address" + cityName);

    }

    private void _initController() {
        // start the controller
        yelpController = new YelpController();
        yelpAPI = yelpController.init();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Create the location request

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            _latitude = mLastLocation.getLatitude();
            _longitude = mLastLocation.getLongitude();
            getCurrentCity();

            Log.d(TAG, "LATITUDE VALUE" + _latitude);
            Log.d(TAG, "LONGITUDE VALUE" + _longitude);
        } else {
            Log.d(TAG,"On connected failed");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,""+connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
