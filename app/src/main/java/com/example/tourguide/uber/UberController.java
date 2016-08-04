package com.example.tourguide.uber;

import com.example.tourguide.R;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.BuildConfig;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import static com.uber.sdk.android.core.utils.Preconditions.checkNotNull;
import static com.uber.sdk.android.core.utils.Preconditions.checkState;


import java.util.Arrays;

/**
 * Created by Onkar on 8/1/2016.
 */
public class UberController {

    private String DROPOFF_ADDR = "One Embarcadero Center, San Francisco";
    private Double DROPOFF_LAT = 37.795079;
    private Double DROPOFF_LONG = -122.397805;
    private String DROPOFF_NICK = "Embarcadero";
    private String ERROR_LOG_TAG = "UberSDK-SampleActivity";
    private String PICKUP_ADDR = "1455 Market Street, San Francisco";
    private Double PICKUP_LAT = 37.775304;
    private Double PICKUP_LONG = -122.417522;
    private String PICKUP_NICK = "Uber HQ";
    private final String UBERX_PRODUCT_ID = "a1111c8c-c720-46c3-8534-2fcdd730040d";
    private final int WIDGET_REQUEST_CODE = 1234;

    private static final String CLIENT_ID = "1EkCP3cRwDjBQdkaFk-N4xGqrOKeZI0P";
    private static final String REDIRECT_URI = "https://www.google.com/";
    private static final String SERVER_TOKEN = "oVWIxS9Z5vnnbUbCHL9igEN9grXvqSHi1poWfI_Y";
    private static Double startLat, startLng, destLat, destLng;
    private String startName, startAddress;
    public static void initialize(SessionConfiguration config ) {
        validateConfiguration(config);
        ServerTokenSession session = new ServerTokenSession(config);

/*
        RideParameters rideParametersForProduct = new RideParameters.Builder()
                .setProductId(UBERX_PRODUCT_ID)
                .setPickupLocation(startLat, startLng, startName, startAddress)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LONG, DROPOFF_NICK, DROPOFF_ADDR)
                .build();

        // This button demonstrates deep-linking to the Uber app (default button behavior).
        blackButton = (RideRequestButton) findViewById(R.id.uber_button_black);
        blackButton.setRideParameters(rideParametersForProduct);
        blackButton.setSession(session);
        blackButton.setCallback(this);
        blackButton.loadRideInformation();


        RideParameters rideParametersCheapestProduct = new RideParameters.Builder()
                .setPickupLocation(PICKUP_LAT, PICKUP_LONG, PICKUP_NICK, PICKUP_ADDR)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LONG, DROPOFF_NICK, DROPOFF_ADDR)
                .build();*/
    }
    /**
     * Validates the local variables needed by the Uber SDK used in the sample project
     * @param configuration
     */
    protected static void validateConfiguration(SessionConfiguration configuration) {
        String nullError = "%s must not be null";
        String sampleError = "Please update your %s in the gradle.properties of the project before " +
                "using the Uber SDK Sample app. For a more secure storage location, " +
                "please investigate storing in your user home gradle.properties ";

        checkNotNull(configuration, String.format(nullError, "SessionConfiguration"));
        checkNotNull(configuration.getClientId(), String.format(nullError, "Client ID"));
        checkNotNull(configuration.getRedirectUri(), String.format(nullError, "Redirect URI"));
        checkNotNull(configuration.getServerToken(), String.format(nullError, "Server Token"));
        checkState(!configuration.getClientId().equals("insert_your_client_id_here"),
                String.format(sampleError, "Client ID"));
        checkState(!configuration.getRedirectUri().equals("insert_your_redirect_uri_here"),
                String.format(sampleError, "Redirect URI"));
        checkState(!configuration.getRedirectUri().equals("insert_your_server_token_here"),
                String.format(sampleError, "Server Token"));
    }

//This is a convenience method and will set the default config to be used in other components without passing it directly.
//    UberSdk.initialize(config);
}
