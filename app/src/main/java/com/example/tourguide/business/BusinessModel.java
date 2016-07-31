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

    private static String imageUrl;
    private static String name;
    private static String status;
    private static String snippetText;
    private static int imageResourceId;
    private static ArrayList<String> address;
    private static double rating;
    private static double latitude;
    private static double longitude;
    private static String phone;


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
    }

    public static int getImageResourceId() {
        return imageResourceId;
    }

    public static void setImageResourceId(int imageResourceId) {
        BusinessModel.imageResourceId = imageResourceId;
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

    public static void setAddress(ArrayList<String> address) {
        BusinessModel.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        BusinessModel.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        BusinessModel.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        BusinessModel.status = status;
    }

    public String getSnippetText() {
        return snippetText;
    }

    public void setSnippetText(String snippetText) {
        BusinessModel.snippetText = snippetText;
    }

    public static double getRating() {
        return rating;
    }

    public static void setRating(double rating) {
        BusinessModel.rating = rating;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        BusinessModel.latitude = latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        BusinessModel.longitude = longitude;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        BusinessModel.phone = phone;
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
    }
    
    public static void display(List<BusinessModel> list) {
        for (BusinessModel b: list) {
            Log.d("DEBUG","Name "+b.getName()+"Address "+b.getAddress()+"Rating "+b.getRating()+"Image "+b.getImageUrl()+"Snippet "+b.getSnippetText());
        }
    }
}
