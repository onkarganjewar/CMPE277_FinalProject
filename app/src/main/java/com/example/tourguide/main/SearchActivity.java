package com.example.tourguide.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tourguide.business.BusinessModel;
import com.example.tourguide.yelp.YelpController;
import com.project.name.R;
import com.yelp.clientlib.connection.YelpAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class SearchActivity extends AppCompatActivity {

    YelpController yelpController;
    private Button btnGo,btn;
    private static TextView txtSearch;
    private YelpAPI yelpAPI;
    private List<BusinessModel> resultsList;
    private BusinessModel businessModel;
    private static final int PROGRESS = 0x1;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private ProgressDialog progress;

//    private android.os.Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        yelpController = new YelpController();
        yelpAPI = yelpController.init();
        btnGo = (Button) findViewById(R.id.btnGo);
        txtSearch = (TextView) findViewById(R.id.textView);
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int returnVal = yelpController.performSearch(yelpAPI,getApplicationContext());
                //progressbar
                progress = new ProgressDialog(v.getContext());
                progress.setTitle("Please Wait!!");
                progress.setMessage("Wait!!");
                progress.setCancelable(true);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
            }
        });
    }
}