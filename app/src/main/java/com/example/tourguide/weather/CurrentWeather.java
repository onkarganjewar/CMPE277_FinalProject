package com.example.tourguide.weather;

import com.example.tourguide.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Onkar on 8/4/2016.
 */
public class CurrentWeather {

    private String Icon;
    private long Time;
    private double Temperature;
    private double PrecipChance;
    private double Humidity;
    private String Summary;
    private String Timezone;

    public String getTimezone() {
        return Timezone;
    }

    public void setTimezone(String timezone) {
        Timezone = timezone;
    }

    public String getIcon() {
        return Icon;

    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public int getTemperature() {
        return (int)Math.round(Temperature);
    }

    public void setTemperature(double temperature) {
        Temperature = temperature;
    }

    public double getPrecipChance() {
        return PrecipChance;
    }

    public void setPrecipChance(double precipChance) {
        PrecipChance = precipChance;
    }

    public double getHumidity() {
        return Humidity;
    }

    public void setHumidity(double humidity) {
        Humidity = humidity;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(Timezone));
        Date dateTime = new Date(Time * 1000);
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public int getIconId(){
        int iconId = R.drawable.clear_day;
        if (Icon.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (Icon.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (Icon.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (Icon.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (Icon.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (Icon.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (Icon.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (Icon.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (Icon.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (Icon.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }
        return iconId;
    }
}
