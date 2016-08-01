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
 */
public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.MyViewHolder> {

    private final Context context;
    private List<BusinessModel> locationList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vSurname;
        protected TextView vEmail;
        protected TextView vTitle;
        private CardView cardView;
        protected ImageView coverImageView, likeImageView, shareImageView;


        public MyViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vSurname = (TextView)  v.findViewById(R.id.txtSurname);
            vEmail = (TextView)  v.findViewById(R.id.txtEmail);
            vTitle = (TextView) v.findViewById(R.id.title);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            cardView = (CardView) v.findViewById(R.id.card_view);

            likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
            shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
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
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final BusinessModel businessModel = locationList.get(position);

        holder.coverImageView.setImageResource(businessModel.getImageResourceId());
        new ImageLoadTask(businessModel.getImageUrl(), holder.coverImageView).execute();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement onClick
                Intent intent = new Intent(context,MapActivity.class);
                intent.putExtra("name",businessModel.getName());
                Log.d("BusinessAdapter","Latitude"+businessModel.getLatitude());
                Log.d("BusinessAdapter","Longitude"+businessModel.getLongitude());
                intent.putExtra("latitude",businessModel.getLatitude());
                intent.putExtra("longitude",businessModel.getLongitude());

                intent.putParcelableArrayListExtra("MyObj", (ArrayList<BusinessModel>) locationList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.vName.setText(businessModel.getName());
        holder.vSurname.setText(businessModel.getSnippetText());
        holder.vEmail.setText(businessModel.getPhone());
        holder.vTitle.setText(""+businessModel.getRating());
//        holder.coverImageView.setTag(list.get(position).getImageResourceId());
        holder.likeImageView.setTag(R.drawable.ic_like);
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
