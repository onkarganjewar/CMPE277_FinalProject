package com.example.tourguide.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.tourguide.R;
import com.example.tourguide.business.BusinessModel;
import com.example.tourguide.view.BusinessAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Onkar on 7/30/2016.
 *
 * Activity to display all the cards showing nearby attractions
 */
public class MainActivity extends AppCompatActivity {


    // list of fetched nearby attractions
    private List<BusinessModel> myList = new ArrayList<BusinessModel>();

    // views and layouts to hold and set the results-list
    private RecyclerView recyclerView;
    private BusinessAdapter mAdapter;
    private BusinessModel businessModel;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting the list of results from MapView
        Intent intent = getIntent();
        myList = (intent.<BusinessModel>getParcelableArrayListExtra("MyObj"));
//        locationList = intent.getParcelableArrayListExtra("MyObj");
        // initialize all the elements of the view
        _init();
    }

    private void _init() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Nearby Attractions");

//        recyclerView.setHasFixedSize(true);
        mAdapter = new BusinessAdapter(myList,this.getApplicationContext());
        mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        menu.removeItem(R.id.action_websearch);
        return true;
    }
}
