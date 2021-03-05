package com.belar.fitquest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class WorkoutItemAdapter extends ArrayAdapter<WorkoutItem> {

    int mResources;
    Context mContext;
    HashMap<String, String> mWorkoutItemList;

    public WorkoutItemAdapter(@NonNull Context context, int resource, HashMap<String, String> workoutItemList)
    {
        super(context, resource);
        this.mResources = resource;
        this.mContext = context;
        this.mWorkoutItemList = workoutItemList;
        Log.d(TAG, "WorkoutItemAdapter: line 31: WorkoutItemAdapter Constructed");

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Log.d(TAG, "line 39 WorkoutItemAdapterAdapter: working");

        String mWorkoutKey, mWorkoutValue;
        if( position == mWorkoutItemList.size() -1)
        {
            mWorkoutKey = "workout";
            mWorkoutValue = mWorkoutItemList.get(mWorkoutKey);
        }
        else
        {
            mWorkoutKey = "meal "+(position+1);
            mWorkoutValue = mWorkoutItemList.get(mWorkoutKey);
        }

        WorkoutItem workoutItem = new WorkoutItem(mWorkoutKey, mWorkoutValue);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResources, parent, false);

        TextView tv_workoutItem = convertView.findViewById(R.id.tv_workoutItem);
        TextView tv_workoutItemType = convertView.findViewById(R.id.tv_workoutItemType);

        tv_workoutItem.setText(mWorkoutValue);
        tv_workoutItemType.setText(mWorkoutKey);

        return convertView;

    }

    @Override
    public int getCount()
    {
        return mWorkoutItemList.size();
    }

    @Override
    public int getPosition(@Nullable WorkoutItem item) {
        return super.getPosition(item);
    }
}
