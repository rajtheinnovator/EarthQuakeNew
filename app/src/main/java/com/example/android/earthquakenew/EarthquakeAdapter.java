package com.example.android.earthquakenew;

import android.graphics.drawable.GradientDrawable;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ABHISHEK RAJ on 9/18/2016.
 */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    // View lookup cache
    private static class ViewHolder {
        TextView quakeMagnitude;
        TextView quakeLocationPrimary;
        TextView quakeDate;
        TextView quakeTime;
        TextView quakeLocationOffset;
    }

    public EarthquakeAdapter(Context context, ArrayList<Earthquake> quakeDetails) {
        super(context, 0, quakeDetails);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Earthquake currentQuake = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.earthquake_list_item, parent, false);
            viewHolder.quakeMagnitude = (TextView) convertView.findViewById(R.id.quakeMagnitude);
            viewHolder.quakeLocationPrimary = (TextView) convertView.findViewById(R.id.quakeLocationPrimary);
            viewHolder.quakeLocationOffset = (TextView) convertView.findViewById(R.id.quakeLocationOffset);
            viewHolder.quakeDate = (TextView) convertView.findViewById(R.id.quakeDate);
            viewHolder.quakeTime = (TextView) convertView.findViewById(R.id.quakeTime);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Double quakeMagnitude = currentQuake.getQuakeMagnitude();
        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(quakeMagnitude);
        viewHolder.quakeMagnitude.setText(output);
        String location = currentQuake.getQuakeLocation();
        if (location.contains(",")) {
            String[] locationParts = location.split(",");
            String locationPrimary = locationParts[0];
            String locationOffset = locationParts[1];
            viewHolder.quakeLocationPrimary.setText(locationPrimary);
            viewHolder.quakeLocationOffset.setText(locationOffset);
        } else {
            viewHolder.quakeLocationPrimary.setText("Near the");
            viewHolder.quakeLocationOffset.setText(location);
        }
        Long timeInMilisecond = currentQuake.getQuakeDate();
        Date dateObject = new Date(timeInMilisecond);
        String formatteddate = formatDate(dateObject);
        viewHolder.quakeDate.setText(formatteddate);
        String formattedTime = formatTime(dateObject);
        viewHolder.quakeTime.setText(formattedTime);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) convertView.findViewById(R.id.quakeMagnitude).getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentQuake.getQuakeMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return convertView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}

