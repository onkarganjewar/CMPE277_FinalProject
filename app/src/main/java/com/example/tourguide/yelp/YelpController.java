package com.example.tourguide.yelp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import com.example.tourguide.business.BusinessModel;
import com.example.tourguide.main.MainActivity;
import com.example.tourguide.view.DownloadImageTask;
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
 * Created by Onkar on 7/30/2016.
 */
public class YelpController {

    private static final String yelp_ConsumerKey = "tqaXj_tsVH7mRBjl3E91dQ";
    private static final String yelp_ConsumerSecret = "taU_yGFRW6SEVTy88MAz25Ic1E8";
    private static final String yelp_Token = "pIAut5crBkvPFb5t1_mQPdJYcZGWwdyI";
    private static final String yelp_TokenSecret = "mCWojfqtSnHnRfmuii_6kXz8Gv8";
    private YelpAPI yelpAPI;
    private YelpAPIFactory apiFactory;
    private static final int searchLimit = 10;
    private static final String[] searchFilter= {"active"};
    private static Map<String, String> searchParams = new HashMap<>();
    private static ArrayList<Business> businesses;

    public static List<BusinessModel> getBusinessModelList() {
        return businessModelList;
    }

    private static List<BusinessModel> businessModelList = new ArrayList<BusinessModel>();

    private static String status = "";
    private String phone;

    public YelpController() {

    }
    public YelpAPI init() {
        apiFactory = new YelpAPIFactory(yelp_ConsumerKey,yelp_ConsumerSecret,yelp_Token,yelp_TokenSecret);
        yelpAPI = apiFactory.createAPI();
        return yelpAPI;
    }

    public int performSearch(YelpAPI yelpAPI, final Context context) {
        String userKeywords = "";
        String []someSTringArray = {"active"};
        for(String eachValue : someSTringArray)
        {
            userKeywords = userKeywords + "," + eachValue;
        }

        // general params
        searchParams.put("term", "active");
        searchParams.put("limit", Integer.toString(searchLimit));

        // locale params
        searchParams.put("lang", "en");

        Call<SearchResponse> call = yelpAPI.search("San Francisco", searchParams);
//        Response<SearchResponse> response = call.execute();

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                String name, imageUrl ,description;
                ArrayList<String> address;
                Double rating,latitude,longitude;
                // Update UI text with the searchResponse.
                businesses = searchResponse.businesses();
                Log.d("YELP","Response"+searchResponse);

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
                    int imageResourceId = R.drawable.cafe_theme;
                    BusinessModel newObject = new BusinessModel(name,description,status,imageUrl,address,rating,phone,latitude,longitude,imageResourceId);
                    Log.d("new Object", newObject.getName());
                    businessModelList.add(newObject);

                }
                Intent intent = new Intent(context, MainActivity.class);
                intent.putParcelableArrayListExtra("MyObj", (ArrayList<BusinessModel>) businessModelList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                //Log.d("businessModelList : -- ", ""+businessModelList);
                //BusinessModel.display(businessModelList);
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
