package com.example.android.earthquakenew;

/**
 * Created by ABHISHEK RAJ on 9/18/2016.
 */
public class Earthquake {
    private Double mQuakeMagnitude;
    private String mQuakeLocation;
    private Long mQuakeDate;
    private String mUrl;

    Earthquake(Double quakeMagnitude, String quakeLocation, Long quakeDate, String url) {
        mQuakeMagnitude = quakeMagnitude;
        mQuakeLocation = quakeLocation;
        mQuakeDate = quakeDate;
        mUrl = url;
    }

    public Double getQuakeMagnitude() {
        return mQuakeMagnitude;
    }

    public String getQuakeLocation() {
        return mQuakeLocation;
    }

    public Long getQuakeDate() {
        return mQuakeDate;
    }
    public String getUrl(){return mUrl;}

}

