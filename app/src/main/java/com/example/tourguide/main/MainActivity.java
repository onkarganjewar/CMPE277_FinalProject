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
import android.view.SubMenu;

import com.example.tourguide.R;
import com.example.tourguide.business.BusinessModel;
import com.example.tourguide.registration.Login;
import com.example.tourguide.view.BusinessAdapter;
import com.example.tourguide.weather.WeatherActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.log4j.chainsaw.Main;

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
    private double currentLatitude, currentLongitude;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        // getting the list of results from MapView
        Intent intent = getIntent();
        currentLatitude = intent.getDoubleExtra("currentLat",0);
        currentLongitude = intent.getDoubleExtra("currentLng",0);
        myList = (intent.<BusinessModel>getParcelableArrayListExtra("MyObj"));
//        locationList = intent.getParcelableArrayListExtra("MyObj");
        // initialize all the elements of the view
        _init();
    }

    private void _init() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setTitle("Welcome, "+firebaseAuth.getCurrentUser().getEmail().toString());


//        recyclerView.setHasFixedSize(true);
        mAdapter = new BusinessAdapter(myList,this.getApplicationContext(), currentLatitude, currentLongitude);
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

            case R.id.action_settings:
                runOnUiThread(new Runnable() // run on ui thread
                {
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
//                        intent.putExtra("latitude",currentLatitude);
//                        intent.putExtra("longitude",currentLongitude);
                        startActivity(intent);
                    }
                });
                Log.d("DEBUG","SETTINGS CLICKED");
                return true;

            case R.id.action_logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this,Login.class));
                Log.d("DEBUG","LOGOUT CLICKED");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
        // Inflate your main_menu into the menu
//        inflater.inflate(R.menu.options_menu, menu);
//        menu.removeItem(R.id.action_websearch);

        // Find the menuItem to add your SubMenu
        getMenuInflater().inflate(R.menu.options_menu, menu);
//        MenuItem myMenuItem = menu.findItem(R.id.action_settings);
//        getMenuInflater().inflate(R.menu.sub_menu_options, myMenuItem.getSubMenu());

        // Inflating the sub_menu menu this way, will add its menu items
        // to the empty SubMenu you created in the xml
//        getMenuInflater().inflate(R.menu.sub_menu, myMenuItem.getSubMenu());

        return true;
    }
}
