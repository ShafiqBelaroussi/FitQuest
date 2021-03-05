package com.belar.fitquest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CourseContentAdapter  extends ArrayAdapter<ContentItem>
{
    private static final String TAG = "CourseContentAdapter";
    private Context mContext;
    int mResources;
    int currentDay = 0;



    public CourseContentAdapter(@NonNull Context context, int resource, @NonNull List<ContentItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResources = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Log.d(TAG, "CourseContentAdapter is working");

        String dayNumber, dateAquired;
        int dayN;

        dayNumber = getItem(position).getDayNumber();
        dateAquired = getItem(position).getDateAquired();

        ContentItem contentItem = new ContentItem(dayNumber, dateAquired);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResources, parent, false);

        TextView tv_day = convertView.findViewById(R.id.tv_day);
        RelativeLayout rl_content_item = convertView.findViewById(R.id.rl_content_item);

        tv_day.setText(contentItem.getDayNumber());


        int diff = CalDaysDiff(dateAquired, aquireCurrentDate());

        if(CalDaysDiff(dateAquired, aquireCurrentDate()) == position)
        {
            rl_content_item.setBackgroundColor(mContext.getColor(R.color.SecondaryBlue));
            setCurrentDay(diff+1);
            Log.d(TAG, "line 67, currentDay: "+ currentDay);
        }
        else
        {
            rl_content_item.setBackgroundColor(mContext.getColor(R.color.bgBlack));
            setCurrentDay(diff+1);
        }





        return convertView;
    }



    private String aquireCurrentDate() {

        return new SimpleDateFormat("dd:MM:yyyy").format(new Date());
    }

    private int CalDaysDiff(String d1, String d2)
    {
        int d1_day = Integer.parseInt(d1.split(":")[0]);
        int d1_Month = Integer.parseInt(d1.split(":")[1]);
        int d1_Year = Integer.parseInt(d1.split(":")[2]);
        Log.d(TAG, "d1_year: "+ d1_Year);

        int d2_day = Integer.parseInt(d2.split(":")[0]);
        int d2_Month = Integer.parseInt(d2.split(":")[1]);
        int d2_Year = Integer.parseInt(d2.split(":")[2]);
        Log.d(TAG, "d2_year: "+ d2_Year);

        try {
            Date date1 = new SimpleDateFormat("dd:MM:yyyy").parse(d1);
            Date date2 = new SimpleDateFormat("dd:MM:yyyy").parse(d2);

            int diff = (int) (date1.getTime() - date2.getTime()) / 86400000;
            return Math.abs(diff);

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }

    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }
}
