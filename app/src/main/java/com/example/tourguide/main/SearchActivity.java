package com.example.tourguide.main;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tourguide.business.BusinessModel;
import com.example.tourguide.yelp.YelpController;
import com.project.name.R;
import com.yelp.clientlib.connection.YelpAPI;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    YelpController yelpController;
    private static Button btnGo,btn;
    private static TextView txtSearch;
    private YelpAPI yelpAPI;
    private List<BusinessModel> resultsList;
    private BusinessModel businessModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        yelpController = new YelpController();
        yelpAPI = yelpController.init();
        btnGo = (Button) findViewById(R.id.btnGo);
        txtSearch = (TextView) findViewById(R.id.textView);
        btn = (Button) findViewById(R.id.button);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int returnVal = yelpController.performSearch(yelpAPI);
                Log.d("RETURN","Return val "+returnVal+ "list  "+resultsList);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG","IN ONCLICKLISTENER");
                resultsList = yelpController.getBusinessModelList();
                if(resultsList!= null) {
                    Log.d("RETURN","list  "+resultsList);
                    businessModel.display(resultsList);
                    Intent i = new Intent(SearchActivity.this, MainActivity.class);
                    i.putParcelableArrayListExtra("MyObj", (ArrayList<BusinessModel>) resultsList);
                    startActivity(i);
                }
            }
        });
    }
}