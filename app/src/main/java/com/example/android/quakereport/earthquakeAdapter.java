package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class earthquakeAdapter extends ArrayAdapter<earthquake> {

    public earthquakeAdapter(@NonNull Context context, ArrayList<earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        earthquake currentEarthquake = getItem(position);

        TextView magnitude = listItemView.findViewById(R.id.magnitude);
        TextView date = listItemView.findViewById(R.id.date);
        TextView time = listItemView.findViewById(R.id.time);
        TextView place1 = listItemView.findViewById(R.id.place1);
        TextView place2 = listItemView.findViewById(R.id.place2);

        //setting place of origin
        String original_place = currentEarthquake.getPlace().trim();
        String place1_str, place2_str;
        if(original_place.contains("of")){
            String parts[] = original_place.split("of");
            place1_str = parts[0].trim();
            place2_str = parts[1].trim();
        }else{
            place1_str = "Near the";
            place2_str = original_place;
        }
        place1.setText(place1_str);
        place2.setText(place2_str);


        //setting magnitude
        Double magnitude_original = currentEarthquake.getMagnitude();
        magnitude.setText(formatDecimal(magnitude_original));
        GradientDrawable mag_backg = (GradientDrawable) magnitude.getBackground();
        int magnitudeColor = getColor(magnitude_original);
        mag_backg.setColor(magnitudeColor);


        //setting date and time
        Date dateObject = new Date(currentEarthquake.getTime());
        date.setText(formatDate(dateObject));
        time.setText(formatTime(dateObject));

        return listItemView;
    }

    private int getColor(Double magnitude_original) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude_original);
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


    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.ENGLISH);
        return dateFormat.format(dateObject);
    }


    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        return timeFormat.format(dateObject);
    }

    //decimal formatter
    private String formatDecimal(Double mag){
        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(mag);
        return output;
    }

}
