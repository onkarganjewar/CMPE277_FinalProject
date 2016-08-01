package com.example.tourguide.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.tourguide.business.BusinessModel;
import com.example.tourguide.view.BusinessAdapter;
import com.example.tourguide.view.DownloadImageTask;
import com.project.name.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Onkar on 7/30/2016.
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<BusinessModel> locationList = new ArrayList<BusinessModel>();
    private BusinessAdapter mAdapter;
    private BusinessModel businessModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("View Ratings");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
        Intent intent = getIntent();
        ArrayList<BusinessModel> myList = (intent.<BusinessModel>getParcelableArrayListExtra("MyObj"));
        locationList = intent.getParcelableArrayListExtra("MyObj");
        Log.i("DEBUG","ReceivedLOCATIONLIST!!!! :- "+myList.get(1).getName());
        BusinessModel.display(myList);
        mAdapter = new BusinessAdapter(myList,this.getApplicationContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
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
