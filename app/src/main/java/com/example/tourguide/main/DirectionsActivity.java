package com.example.tourguide.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourguide.R;
import com.example.tourguide.uber.UberController;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.error.ApiError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import javax.annotation.Nonnull;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DirectionsActivity extends FragmentActivity implements OnMapReadyCallback, RideRequestButtonCallback {

    private GoogleMap mMap;
    private double destinationLat, destinationLng;
    private String searchedName;
    private Button btnDirections;
    private double currentLng, currentLat;
    RelativeLayout layout;
    private SessionConfiguration configuration;
    private String UBERX_PRODUCT_ID = "a1111c8c-c720-46c3-8534-2fcdd730040d";
    private int WIDGET_REQUEST_CODE = 1234;
    private Button txtEstimate;


    private static final String CLIENT_ID = "1EkCP3cRwDjBQdkaFk-N4xGqrOKeZI0P";
    private static final String REDIRECT_URI = "https://www.google.com/";
    private static final String SERVER_TOKEN = "oVWIxS9Z5vnnbUbCHL9igEN9grXvqSHi1poWfI_Y";
    private String DROPOFF_ADDR = "One Embarcadero Center, San Francisco";
    private Double DROPOFF_LAT = 37.795079;
    private Double DROPOFF_LONG = -122.397805;
    private String DROPOFF_NICK = "Embarcadero";
    private String ERROR_LOG_TAG = "UberSDK-SampleActivity";
    private String PICKUP_ADDR = "1455 Market Street, San Francisco";
    private Double PICKUP_LAT = 37.775304;
    private Double PICKUP_LONG = -122.417522;
    private String PICKUP_NICK = "Uber HQ";
    private RideRequestButton requestButton;
    private Request request;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txtEstimate = (Button) findViewById(R.id.txtEstimate);
        btnDirections = (Button) findViewById(R.id.btnDirections);

        /*
        requestButton = new RideRequestButton(getApplicationContext());
        btnDirections = (Button) findViewById(R.id.btnDirections);
        layout = (RelativeLayout) findViewById(R.id.directionsLayout);
        configuration = new SessionConfiguration.Builder()
                .setClientId(CLIENT_ID) //This is necessary
                .setRedirectUri(REDIRECT_URI) //This is necessary if you'll be using implicit grant
                .setEnvironment(SessionConfiguration.Environment.SANDBOX) //Useful for testing your app in the sandbox environment
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.RIDE_WIDGETS)) //Your scopes for authentication here
                .build();
        UberSdk.initialize(configuration);
//        loadRideInformation();
        btnDirections.setOnClickListener(this.getDirections());
        loadRideInformation();*/
        btnDirections.setOnClickListener(this.getDirections());

        txtEstimate.setClickable(false);
        txtEstimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.ubercab");
                if (intent  == null) {
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                } else {
                    try {

                        getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ubercab")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                    catch (android.content.ActivityNotFoundException anfe) {
                        getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.ubercab")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }*/

                PackageManager pm = getPackageManager();
                try {
                    pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
                    String uri = "uber://?action=setPickup&pickup=my_location";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ubercab")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.ubercab")));
                    }
                }




            }
        });

    }
/*
    private void buildQueryString(double startLat, double startLng, double endLat, double endLng) {
        String uri = "https://sandbox-api.uber.com/v1/estimates/price?server_token="+SERVER_TOKEN+"&start_latitude="+startLat+"&start_longitude="+startLng+"&end_latitude="+endLat+"&end_longitude="+endLng;
    }
*/


    private void doGetRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        request = new Request.Builder().url(url).build();
        // prices[1].estimate
        client.newCall(request).enqueue(new Callback() {
            public String estimate = "";

            @Override
            public void onFailure(final Call call, IOException e) {
                // Error
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // For the example, you can show an error dialog or a toast
                        // on the main UI thread
                        Log.d("JSON", "FAILED");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) {

                try {
                    String res = response.body().string();
                    JSONObject Jobject = new JSONObject(res);
                    JSONArray Jarray = Jobject.getJSONArray("prices");
                    int limit = Jarray.length();
                    JSONObject object = Jarray.getJSONObject(1);
                    estimate = object.getString("estimate");
                    Log.d("DEBUG", "ESTIMATE: " + estimate);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtEstimate.setText(estimate);
                            // Place nearby cafe pins on map
//                            placePins(resultsList);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadRideInformation() {

        RideParameters rideParams = new RideParameters.Builder()
                .setPickupLocation(37.775304, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                .setDropoffLocation(37.795079, -122.4397805, "Embarcadero", "One Embarcadero Center, San Francisco") // Price estimate will only be provided if this is provided.
                .setProductId(UBERX_PRODUCT_ID) // Optional. If not provided, the cheapest product will be used.
                .build();

//        configuration = new SessionConfiguration.Builder().setServerToken("YOUR_SERVER_TOKEN").build();
        ServerTokenSession session = new ServerTokenSession(configuration);

        RideRequestButtonCallback callback = new RideRequestButtonCallback() {

            @Override
            public void onRideInformationLoaded() {
                Log.d("DEBUG", "ON RIDE INFORMATION");
            }

            @Override
            public void onError(ApiError apiError) {

                Log.d("DEBUG", "APIERROR" + apiError.getMeta() + "CLIENT ERROR  " + apiError.getClientErrors());
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("DEBUG", "THROWABLE ERROR");
            }
        };

        requestButton.setRideParameters(rideParams);
        requestButton.setSession(session);
        requestButton.setCallback(callback);
        requestButton.loadRideInformation();
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
        mMap.getUiSettings().setMapToolbarEnabled(false);
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
        mMap.setMyLocationEnabled(true);
        receiveIntents();
        uri = "https://sandbox-api.uber.com/v1/estimates/price?server_token="+SERVER_TOKEN+"&start_latitude="+currentLat+"&start_longitude="+currentLng+"&end_latitude="+destinationLat+"&end_longitude="+destinationLng;
        doGetRequest(uri);
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

    @Override
    public void onRideInformationLoaded() {
        Toast.makeText(this, "Estimates have been refreshed", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onError(ApiError apiError) {
        Toast.makeText(this, apiError.getClientErrors().get(0).getTitle(), Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onError(Throwable throwable) {
        Log.e("SampleActivity", "Error obtaining Metadata", throwable);
        Toast.makeText(this, "Connection error", Toast.LENGTH_LONG).show();
    }

}
