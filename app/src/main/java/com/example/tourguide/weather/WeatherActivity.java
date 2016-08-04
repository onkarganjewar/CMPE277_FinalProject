package com.example.tourguide.weather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourguide.R;
import com.example.tourguide.weather.CurrentWeather;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private final String ApiKey = "0cb31db209ed3a9d21971845cb2e212b";
    CurrentWeather mcurrentWeather;
    TextView mTemperatureLabel;
    TextView mTimeLabel;
    TextView mHumidityValue;
    TextView mPrecipValue;
    TextView mSummaryLabel;
    ImageView mIconImageView;
    private double lat, lng;
    private String TAG = "Weather_Activity";

    // Variables to get current location
    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;
    // Location update intervals
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
//        ButterKnife.bind(this);
        buildGoogleApiClient();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        mTemperatureLabel = (TextView) findViewById(R.id.temperatureView);
        mTimeLabel = (TextView) findViewById(R.id.timeView);
        mHumidityValue = (TextView) findViewById(R.id.humidityView);
        mPrecipValue = (TextView) findViewById(R.id.precipView);
        mSummaryLabel = (TextView) findViewById(R.id.summaryView);
        mIconImageView = (ImageView) findViewById(R.id.iconView);


//        https://api.forecast.io/forecast/0cb31db209ed3a9d21971845cb2e212b/37.8267,-122.423


        if (isNetworkAvailable() && lat != 0.0) {
        }/*else {
            Toast.makeText(this, "Network is unavailable", Toast.LENGTH_SHORT).show();
        }*/
    }

    private CurrentWeather getCurrentDetails(String jsonObject) throws JSONException {
        JSONObject weather = new JSONObject(jsonObject);
        String timezone = weather.getString("timezone");
        Log.i(TAG, "JSON timezone:" + timezone);

        JSONObject currently = weather.getJSONObject("currently");
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTimezone(weather.getString("timezone"));

        Log.v(TAG, currentWeather.getFormattedTime());
        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUsersAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mcurrentWeather.getTemperature() + "");
        mTimeLabel.setText(mcurrentWeather.getFormattedTime() + "");
        mHumidityValue.setText(mcurrentWeather.getHumidity() + "");
        mPrecipValue.setText(mcurrentWeather.getPrecipChance() + "");
        mSummaryLabel.setText(mcurrentWeather.getSummary() + "");
        mIconImageView.setImageResource(mcurrentWeather.getIconId());
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
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
            Log.d(TAG, "LATITUDE VALUE111" + lat);
            Log.d(TAG, "LONGITUDE VALUE" + lng);
            getWeather(lat, lng);
        } else {
            Log.d(TAG, "On connected failed");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private void getWeather(double lat, double lng) {
        String ForecastURL = "https://api.forecast.io/forecast/" + ApiKey + "/" + lat + "," + lng;
        Log.d(TAG, ForecastURL);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(ForecastURL).build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String JSONObject = response.body().string();
                    Log.v(TAG, JSONObject);
                    if (response.isSuccessful()) {
                        mcurrentWeather = getCurrentDetails(JSONObject);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateDisplay();
                            }
                        });

                    }
                } catch (IOException e) {
                    Log.v(TAG, "Exception:", e);

                } catch (JSONException e) {
                    Log.v(TAG, "Exception:", e);
                }

            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

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
}
