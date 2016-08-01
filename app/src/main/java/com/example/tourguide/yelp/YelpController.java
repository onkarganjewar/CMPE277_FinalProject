package com.example.tourguide.yelp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.tourguide.business.BusinessModel;
import com.example.tourguide.main.MainActivity;
import com.project.name.R;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private static final int searchLimit = 10;

    // currently hardcoded as "active"
    // but can be filtered as per the user request, in future
    private static final String[] searchFilter= {"active"};

    // search parameters: "key" --> "value"
    //                    "term" --> "active"
    private static Map<String, String> searchParams = new HashMap<>();

    // list to hold the search results
    private static ArrayList<Business> businesses;

    public static List<BusinessModel> getBusinessModelList() {
        return businessModelList;
    }

    private static List<BusinessModel> businessModelList = new ArrayList<BusinessModel>();

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


    public int performSearch(YelpAPI yelpAPI, final Context context) {
        /*
        // method to include multiple keywords for search
        String userKeywords = "";
        String []someSTringArray = {"active"};
        for(String eachValue : someSTringArray)
        {
            userKeywords = userKeywords + "," + eachValue;
        }*/

        // general params
        searchParams.put("term", "active");
        searchParams.put("limit", Integer.toString(searchLimit));

        // locale params
        searchParams.put("lang", "en");

        // currently set the value to San Francisco
        // TODO: update the value as per the current location
        Call<SearchResponse> call = yelpAPI.search("San Francisco", searchParams);

        // asynch http request for search
        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();

                // local variables to hold the search results
                String name, imageUrl ,description;
                ArrayList<String> address;
                Double rating,latitude,longitude;

                // Update UI text with the searchResponse.
                businesses = searchResponse.businesses();

                for (int i=0; i<searchLimit; i++) {
                    name = businesses.get(i).name();
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

                    // not able to implement it yet
                    int imageResourceId = R.drawable.cafe_theme;

                    // create the model for every location
                    BusinessModel newObject = new BusinessModel(name,description,status,imageUrl,address,rating,phone,latitude,longitude,imageResourceId);
                    businessModelList.add(newObject);
                }
                // start the new activity to display cards
                Intent intent = new Intent(context, MainActivity.class);
                intent.putParcelableArrayListExtra("MyObj", (ArrayList<BusinessModel>) businessModelList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
                // Error
            Log.d("DEBUG","SEARCH FAILED");
            return;
            }
        };
        call.enqueue(callback);
        return 0;
    }
}
