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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CourseItemAdapter  extends ArrayAdapter<CourseItem>
{
    private Context mContext;
    int mResources;
    List<CourseItem> courseItems;

    public CourseItemAdapter(@NonNull Context context, int resource, @NonNull List<CourseItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResources = resource;
        this.courseItems = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        Log.d(TAG, "line 40 CourseItem Adapter: working");

        String courseHost, courseImageUrl, courseTitle, contains1, contains2, whatToKnow, dateAquired;
        int courseDuration, currentDay;
        HashMap<String, HashMap<String,HashMap<String, String>>> content;


        courseHost = getItem(position).courseHost;
        courseImageUrl = getItem(position).courseImageUrl;
        courseTitle = getItem(position).courseTitle;
        contains1 = getItem(position).contains1;
        contains2 = getItem(position).contains2;
        whatToKnow = getItem(position).whatToKnow;
        dateAquired = getItem(position).dateAquired;

        courseDuration = getItem(position).courseDuration;
        currentDay = getItem(position).currentDay;

        content = getItem(position).content;





        CourseItem courseItem = new CourseItem(courseHost, courseImageUrl, courseDuration, currentDay, courseTitle, contains1, contains2, whatToKnow, dateAquired, content);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResources, parent, false);

        RelativeLayout rl_courseItem = convertView.findViewById(R.id.rl_courseItem);
        ImageView iv_courseImage = convertView.findViewById(R.id.iv_courseImage);
        TextView tv_courseName = convertView.findViewById(R.id.tv_courseName);

        Picasso.get().load(courseItem.getCourseImageUrl()).fit().into(iv_courseImage);
        tv_courseName.setText(courseItem.getCourseTitle());





        return convertView;
    }

    @Override
    public int getCount() {
        return courseItems.size();
    }
}
