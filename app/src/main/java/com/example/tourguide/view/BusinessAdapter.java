package com.example.tourguide.view;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourguide.business.BusinessModel;
import com.project.name.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by Onkar on 7/30/2016.
 *
 * class resposible to hold the RecyclerView and inside CardView
 */
public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.MyViewHolder> {

    private final Context context;
    private List<BusinessModel> locationList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public class MyViewHolder extends RecyclerView.ViewHolder {

        // sample textviews to hold the results
        protected TextView vName;
        protected TextView vSurname;
        protected TextView vEmail;
        protected TextView vTitle;

        // images and cards to display results
        private CardView cardView;
        // location image and like image
        protected ImageView coverImageView, likeImageView;


        public MyViewHolder(View v) {
            super(v);

            initView(v);

            initActionListeners();


        }

        private void initActionListeners() {

            // instagram like button functionality
            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int)likeImageView.getTag();
                    if( id == R.drawable.ic_like){
                        likeImageView.setTag(R.drawable.ic_liked);
                        likeImageView.setImageResource(R.drawable.ic_liked);
                    }else{
                        likeImageView.setTag(R.drawable.ic_like);
                        likeImageView.setImageResource(R.drawable.ic_like);
//                        Toast.makeText(getActivity(),titleTextView.getText()+" removed from favourites",Toast.LENGTH_SHORT).show();
                    }
                }
            });
/*
            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(coverImageView.getId())
                            + '/' + "drawable" + '/' + getResources().getResourceEntryName((int)coverImageView.getTag()));


                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                }
            });*/
        }


        private void initView(View v) {
            vName =  (TextView) v.findViewById(R.id.txtName);
            vSurname = (TextView)  v.findViewById(R.id.txtSurname);
            vEmail = (TextView)  v.findViewById(R.id.txtEmail);
            vTitle = (TextView) v.findViewById(R.id.title);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            cardView = (CardView) v.findViewById(R.id.card_view);

            likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
//            shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
        }
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final BusinessModel businessModel = locationList.get(position);

        // fetch the image from yelp url
        new ImageLoadTask(businessModel.getImageUrl(), holder.coverImageView).execute();

        // holders for like image and location image
        holder.likeImageView.setTag(R.drawable.ic_like);
        holder.coverImageView.setImageResource(businessModel.getImageResourceId());

        // show the MapView on click event of Cards
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start MapView
                Intent intent = new Intent(context,MapActivity.class);

                // information to set the marker options
                intent.putExtra("name",businessModel.getName());
                intent.putExtra("latitude",businessModel.getLatitude());
                intent.putExtra("longitude",businessModel.getLongitude());

                // pass the result list as well ( to be retrieved back later; onclick of back button in MapView)
                // kind of a workaround for application failing on back button
                intent.putParcelableArrayListExtra("MyObj", (ArrayList<BusinessModel>) locationList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        // holders to set ratings, description, phone, name respectively
        // TODO: change the variable names
        holder.vName.setText(businessModel.getName());
        holder.vSurname.setText(businessModel.getSnippetText());
        holder.vEmail.setText(businessModel.getPhone());
        holder.vTitle.setText(""+businessModel.getRating());
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BusinessAdapter(List<BusinessModel> myDataset, Context applicationContext) {
        locationList = myDataset;
        context = applicationContext;
    }

    @Override
    public BusinessAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }
}
