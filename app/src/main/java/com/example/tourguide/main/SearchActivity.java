package com.example.tourguide.main;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tourguide.business.BusinessModel;
import com.example.tourguide.yelp.YelpController;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.name.R;
import com.yelp.clientlib.connection.YelpAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import java.util.logging.LogRecord;

/**
 *  sample activity to initiate the search
 */
public class SearchActivity extends AppCompatActivity implements LocationListener{

    // controller responsible for carrying out the search
    private YelpController yelpController;
    private YelpAPI yelpAPI;

    // UI elements
    private Button btnGo;
    public static int count=0;

    // Variables to get current location
    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;
    protected Marker mCurrLocationMarker;
    Timer _t;

    // progress dialog indicating "wait"
    private ProgressDialog progress;
    ImageView img;
    final int [] setImg = {R.drawable.cafe_theme,R.drawable.likefb};
    LinearLayout layout;
    private static int counterInterval = 0;
    int[] drawablearray=new int[]{R.drawable.likefb,R.drawable.cafe_theme};
    private String TAG = "Search_Activity";
    private String cityName;
    private String provider;
    private double _latitude, _longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        layout = (LinearLayout) findViewById(R.id.myLinearLayout);

/*
        // Code to blur the images
        LinearLayout linearLayout = new LinearLayout(this);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap blurTemplate = BitmapFactory.decodeResource(getResources(), R.drawable.travel, options);
        Bitmap myBitmap = BlurBuilder.blur(getBaseContext(),blurTemplate);
        BitmapDrawable myD = new BitmapDrawable(getBaseContext().getResources(), myBitmap);
        linearLayout = (LinearLayout) findViewByID(R.id.mainlayout); //It depends of the name that you gave to it
*/

        _t = new Timer();
        _t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() // run on ui thread
                {
                    public void run() {
                        if (count < drawablearray.length) {
                            layout.setBackgroundResource(drawablearray[count]);
                            count = (count + 1) % drawablearray.length;
                        }
                    }
                });
            }
        }, 5000, 5000);

/*


        linearLayout.setBackgroundResource(R.drawable.cafe_theme);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                for (int i = 0; i < 2; ++i) {
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            layout.setBackgroundResource(drawablearray[count]);
                            // or ll.setBackgroundResource(resid) if you want.
                            count += (count+1)%drawablearray.length;
                        }
                    });

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 5000);
*/

/*
        final Handler h = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                Home.this.getWindow().setBackgroundDrawableResource(drawablearray[count]);
                count += (count+1)%drawablearray.length;  //<<< increment counter here
                h.postDelayed(this, 5000);
            }
        };*/

        /*      // changing images in ImageView periodically
        setContentView(R.layout.activity_search);
        final ImageView backgroundImageView = (ImageView) findViewById(R.id.backgroundImage);
        backgroundImageView.postDelayed(new Runnable() {
            public void run() {
                backgroundImageView.setImageResource(
                        counterInterval++ % 2 == 0 ?
                                R.drawable.cafe_theme:
                                R.drawable.likefb);
                backgroundImageView.postDelayed(this, 1000);
            }
        }, 1000);*/


        // initialize the yelp controller
        _initController();

        // initialize UI elements
        _init();

        getCurrentCity();
        performSearch();
    }

    private void performSearch() {

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initiate the asynchronous search on background thread
                Log.i(TAG,String.valueOf(_latitude)+""+String.valueOf(_longitude));
                Log.i(TAG,cityName);

                yelpController.performSearch(yelpAPI,getApplicationContext(),cityName);

                // show progress bar until the action completes
                progress = new ProgressDialog(v.getContext());
                progress.setTitle("Please Wait!!");
                progress.setMessage("Wait!!");
                progress.setCancelable(true);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
            }
        });}

    private void _init() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Creating an empty criteria object
        Criteria criteria = new Criteria();
        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

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
        Location location = locationManager.getLastKnownLocation(provider);

        _latitude = location.getLatitude();
        _longitude= location.getLongitude();

        // sample button to initiate search
        btnGo = (Button) findViewById(R.id.btnGo);

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

        Log.i(TAG,"Address"+cityName);

    }

    private void _initController() {
        // start the controller
        yelpController = new YelpController();
        yelpAPI = yelpController.init();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        Log.d(TAG,"Latitude : "+location.getLatitude());
        Log.d(TAG,"Longitude : "+location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}