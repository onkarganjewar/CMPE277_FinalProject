package com.example.tourguide.yelp;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.tourguide.business.BusinessModel;
import com.example.tourguide.main.MainActivity;
import com.example.tourguide.R;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class to communicate with the YelpAPI
 * Created by Onkar on 7/30/2016.
 */

public class YelpController {

    // Yelp credentials
    private static final String yelp_ConsumerKey = "tqaXj_tsVH7mRBjl3E91dQ";
    private static final String yelp_ConsumerSecret = "taU_yGFRW6SEVTy88MAz25Ic1E8";
    private static final String yelp_Token = "pIAut5crBkvPFb5t1_mQPdJYcZGWwdyI";
    private static final String yelp_TokenSecret = "mCWojfqtSnHnRfmuii_6kXz8Gv8";

    // yelpApi library elements
    private YelpAPI yelpAPI;
    private YelpAPIFactory apiFactory;

    // search limit for nearby attractions
    private final int searchLimit = 20;

    // currently hardcoded as "active"
    // but can be filtered as per the user request, in future
    private final String[] searchFilter= {"active"};

    // search parameters: "key" --> "value"
    //                    "term" --> "active"
    private Map<String, String> searchParams = new HashMap<>();

    // list to hold the search results
    private ArrayList<Business> businesses;

    public List<BusinessModel> getBusinessModelList() {
        return businessModelList;
    }

    private List<BusinessModel> businessModelList = new ArrayList<BusinessModel>();

    // sample strings
    private String status = "";
    private String phone;

    public YelpController() {

    }
    // initialize the yelpApi library elements
    public YelpAPI init() {
        apiFactory = new YelpAPIFactory(yelp_ConsumerKey,yelp_ConsumerSecret,yelp_Token,yelp_TokenSecret);
        yelpAPI = apiFactory.createAPI();
        return yelpAPI;
    }


    public int performSearch(YelpAPI yelpAPI, final Context context, String currentCity, double _latitude, double _longitude) {

        // method to include multiple keywords for search
        String userKeywords = "beaches";
        String []someSTringArray = {""};
        String[] filter = {","};
        String[] temp = {"beaches","skydiving","artmuseums","galleries","lakes","hiking","waterparks","amusementparks"};
        for(String eachValue : temp)
        {
            userKeywords = userKeywords + "," + eachValue;
        }

        // general params
        searchParams.put("category_filter", "beaches");
        for (int j =0 ; j<temp.length; j++) {
            Log.d("DEBUG","Length: "+temp.length);
            searchParams.clear();
            searchParams.put("category_filter", temp[j]);
            Log.d("DEBUG",temp[j]);
            // locale params
            searchParams.put("lang", "en");
            final double tempLatitude = _latitude;
            final double tempLongitude = _longitude;

            Call<SearchResponse> call = yelpAPI.search(currentCity, searchParams);
            // asynch http request for search
            Callback<SearchResponse> callback = new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    SearchResponse searchResponse = response.body();

                    // local variables to hold the search results
                    String name, imageUrl ,description, formattedUrl;
                    int len;
                    ArrayList<String> address;
                    Double rating,latitude,longitude;

                    // Update UI text with the searchResponse.
                    businesses = searchResponse.businesses();
                    int size = searchResponse.businesses().size();
                    Log.d("DEBUG","SIZE: "+size);
                    for (int i=0; i<3; i++) {
                        name = businesses.get(i).name();
                        Log.d("DEBUG",name);
                        if (name.equalsIgnoreCase("Billy Beez"))
                            continue;
                        rating = businesses.get(i).rating();
                        if (businesses.get(i).displayPhone() != "") {
                            phone = businesses.get(i).phone();
                        }
                        latitude = businesses.get(i).location().coordinate().latitude();
                        longitude = businesses.get(i).location().coordinate().longitude();
                        imageUrl = businesses.get(i).imageUrl();
                        boolean flag = businesses.get(i).isClosed();

                        if (flag) {
                            status = "closed";
                        } else {
                            status = "open";
                        }
                        address = businesses.get(i).location().displayAddress();
                        description = businesses.get(i).snippetText();

                        if (imageUrl!= null) {
                            len = imageUrl.length();
                            formattedUrl = imageUrl.substring(0, len - 6) + "o.jpg";
                            imageUrl = formattedUrl;
                            Log.i("DEBUG",formattedUrl);
                        }// not able to implement it yet
                        int imageResourceId = R.drawable.cafe_theme;

                        // create the model for every location
                        BusinessModel newObject = new BusinessModel(name,description,status,imageUrl,address,rating,phone,latitude,longitude,imageResourceId);
                        businessModelList.add(newObject);
                    }

                    // start the new activity to display cards
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putParcelableArrayListExtra("MyObj", (ArrayList<BusinessModel>) businessModelList);
                    intent.putExtra("currentLat",tempLatitude);
                    intent.putExtra("currentLng",tempLongitude);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    // HTTP error happened, do something to handle it.
                    // Error
                    t.printStackTrace();
                    Log.d("DEBUG","SEARCH FAILED");
                    return;
                }
            };
            call.enqueue(callback);
        }
        return 0;
    }

    public int performSearch1(YelpAPI yelpAPI, final Context context, String currentCity, double _latitude, double _longitude) {

        // method to include multiple keywords for search
        String userKeywords = "beaches";
        String []someSTringArray = {""};
        String[] filter = {","};
        String[] temp = {"beaches","skydiving","artmuseums","galleries","lakes","hiking","waterparks","amusementparks"};
        for(String eachValue : temp)
        {
            userKeywords = userKeywords + "," + eachValue;
        }

        // general params
        searchParams.put("category_filter", "beaches");
        for (int j =0 ; j<temp.length; j++) {
            Log.d("DEBUG","Length: "+temp.length);
            searchParams.clear();
            searchParams.put("category_filter", temp[j]);
            Log.d("DEBUG",temp[j]);
            // locale params
            searchParams.put("lang", "en");
            final double tempLatitude = _latitude;
            final double tempLongitude = _longitude;

            Call<SearchResponse> call = yelpAPI.search(currentCity, searchParams);
            // asynch http request for search
            Callback<SearchResponse> callback = new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    SearchResponse searchResponse = response.body();

                    // local variables to hold the search results
                    String name, imageUrl ,description, formattedUrl;
                    int len;
                    ArrayList<String> address;
                    Double rating,latitude,longitude;

                    // Update UI text with the searchResponse.
                    businesses = searchResponse.businesses();
                    int size = searchResponse.businesses().size();
                    Log.d("DEBUG","SIZE: "+size);
                    for (int i=0; i<3; i++) {
                        name = businesses.get(i).name();
                        Log.d("DEBUG",name);
                        if (name.equalsIgnoreCase("Billy Beez"))
                            continue;
                        rating = businesses.get(i).rating();
                        if (businesses.get(i).displayPhone() != "") {
                            phone = businesses.get(i).phone();
                        }
                        latitude = businesses.get(i).location().coordinate().latitude();
                        longitude = businesses.get(i).location().coordinate().longitude();
                        imageUrl = businesses.get(i).imageUrl();
                        boolean flag = businesses.get(i).isClosed();

                        if (flag) {
                            status = "closed";
                        } else {
                            status = "open";
                        }
                        address = businesses.get(i).location().displayAddress();
                        description = businesses.get(i).snippetText();

                        if (imageUrl!= null) {
                            len = imageUrl.length();
                            formattedUrl = imageUrl.substring(0, len - 6) + "o.jpg";
                            imageUrl = formattedUrl;
                            Log.i("DEBUG",formattedUrl);
                        }// not able to implement it yet
                        int imageResourceId = R.drawable.cafe_theme;

                        // create the model for every location
                        BusinessModel newObject = new BusinessModel(name,description,status,imageUrl,address,rating,phone,latitude,longitude,imageResourceId);
                        businessModelList.add(newObject);
                    }
                }
                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    // HTTP error happened, do something to handle it.
                    // Error
                    t.printStackTrace();
                    Log.d("DEBUG","SEARCH FAILED");
                    return;
                }
            };
            call.enqueue(callback);
        }
        return 0;
    }
}
