package com.example.tourguide.business;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Onkar on 7/30/2016.
 */
public class BusinessModel implements Parcelable {

    private String imageUrl;
    private String name;
    private String status;
    private String snippetText;
    private int imageResourceId;
    private ArrayList<String> address;
    private double rating;
    private double latitude;
    private double longitude;
    private String phone;


    public BusinessModel(String name, String desc, String status, String imageUrl, ArrayList<String> address, double rating, String phone, double latitude, double longitude, int imageResourceId) {
        this.address = address;
        this.name = name;
        this.imageUrl = imageUrl;
        this.status = status;
        this.snippetText = desc;
        this.rating = rating;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageResourceId = imageResourceId;
    }


    public BusinessModel(Parcel in) {
/*
        String temp = in.readString();
        char[] cArray = temp.toCharArray();
        for(char c : cArray){
            address.add(String.valueOf(c));
        }*/

//        address = in.readStringList();
        snippetText = in.readString();
        name = in.readString();
        status = in.readString();
        imageUrl = in.readString();
        rating = in.readDouble();
        phone = in.readString();
        imageResourceId = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public static final Creator<BusinessModel> CREATOR = new Creator<BusinessModel>() {
        @Override
        public BusinessModel createFromParcel(Parcel in) {
            return new BusinessModel(in);
        }

        @Override
        public BusinessModel[] newArray(int size) {
            return new BusinessModel[size];
        }
    };

    public ArrayList<String> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<String> address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSnippetText() {
        return snippetText;
    }

    public void setSnippetText(String snippetText) {
        this.snippetText = snippetText;
    }

    public double getRating() {
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public  void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeStringList(address);
        dest.writeString(snippetText);
        dest.writeString(name);
        dest.writeString(status);
        dest.writeString(imageUrl);
        dest.writeDouble(rating);
        dest.writeString(phone);
        dest.writeInt(imageResourceId);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
    
    public static void display(List<BusinessModel> list) {
        for (BusinessModel b: list) {
            Log.d("DEBUG","Name "+b.getName()+"Address "+b.getAddress()+"Rating "+b.getRating()+"Image "+b.getImageUrl()+"Snippet "+b.getSnippetText());
        }
    }
}
