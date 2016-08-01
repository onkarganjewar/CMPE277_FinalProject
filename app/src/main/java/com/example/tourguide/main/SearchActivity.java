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

/**
 *  sample activity to initiate the search
 */
public class SearchActivity extends AppCompatActivity {

    // controller responsible for carrying out the search
    private YelpController yelpController;
    private YelpAPI yelpAPI;

    // UI elements
    private Button btnGo;

    // progress dialog indicating "wait"
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // initialize the yelp controller
        _initController();

        // initialize UI elements
        _init();
    }

    private void _init() {
        // sample button to initiate search
        btnGo = (Button) findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initiate the asynchronous search on background thread
                yelpController.performSearch(yelpAPI,getApplicationContext());

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

    private void _initController() {
        // start the controller
        yelpController = new YelpController();
        yelpAPI = yelpController.init();
    }
}