package com.belar.fitquest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class CoursesFragment extends Fragment
{

    ListView lv_coursesList, lv_courseContent, lv_workoutContentList;
    ArrayList<CourseItem> courseItemList = new ArrayList<CourseItem>();
    CourseItemAdapter courseItemAdapter;

    public String mUsername;

    // firebase properties //
    FirebaseDatabase mRoot = FirebaseDatabase.getInstance();
    DatabaseReference mUser = mRoot.getReference("users");
    ////////////////////////
    private CourseItem selectedCourseItem;


    public CoursesFragment(String _mUsername)
    {
        this.mUsername = _mUsername;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_courses, container, false);

        lv_coursesList = view.findViewById(R.id.lv_coursesList);
        lv_courseContent = view.findViewById(R.id.lv_courseContent);
        lv_workoutContentList = view.findViewById(R.id.lv_workoutContentListFF);

        try
        {
            mUser.child(mUsername).child("courses").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    try
                    {
                        courseItemList.clear();
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot snap: children)
                        {
                            Log.d(TAG, "onDataChange: snap: "+ snap);
                            CourseItem courseItem = new CourseItem((HashMap<String, Object>) snap.getValue());
                            courseItemList.add(courseItem);
                            Log.d(TAG, "loop line 78: "+ courseItem.getContent());
                        }
                        courseItemAdapter = new CourseItemAdapter(getContext(), R.layout.course_item, courseItemList);
                        lv_coursesList.setAdapter(courseItemAdapter);
                        lv_coursesList.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.load_up_anim));
                    }
                    catch (Exception e )
                    {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }
        catch (Exception e){}

        lv_coursesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                CourseContentAdapter courseContentAdapter;
                Log.d(TAG, "onItemClick: " + courseItemList.get(position).getCourseTitle());
                CourseItem courseItem = courseItemList.get(position);
                ArrayList<ContentItem> contentItems = new ArrayList<ContentItem>();
                Log.d(TAG, "courseItemList size: "+ courseItem.getContent().get("training_nutrients").size());
                for(int i=0; i< courseItem.getContent().get("training_nutrients").size(); i++)
                {
                    contentItems.add(new ContentItem("Day "+(i+1),courseItem.getDateAquired()));
                }

                courseContentAdapter = new CourseContentAdapter(getContext(), R.layout.content_item, contentItems);
                lv_courseContent.setAdapter(courseContentAdapter);
                Log.d(TAG, "currentDay:"+ courseContentAdapter.getCurrentDay());


                Log.d(TAG, "should show");
                lv_coursesList.setVisibility(View.GONE);
                lv_courseContent.setVisibility(View.VISIBLE);
                lv_courseContent.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.load_up_anim));
                selectedCourseItem = courseItem;

            }
        });

        lv_courseContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "onItemClick: line 127: "+ selectedCourseItem.content);
                Log.d(TAG, "onItemClick: line 128: "+ selectedCourseItem.content.get("training_nutrients").get("day 1"));
                Log.d(TAG, "128: "+ position);
                HashMap<String, String> workoutItemList = new HashMap<>();

                for(int i =0; i < selectedCourseItem.content.get("training_nutrients").get("day "+Integer.toString(position+1)).size() -1; i++)
                {
                    workoutItemList.put("meal "+Integer.toString(i +1), selectedCourseItem.content.get("training_nutrients").get("day "+Integer.toString(position+1)).get("meal "+Integer.toString(i+1)));
                    Log.d(TAG, "134: "+ (selectedCourseItem.content.get("training_nutrients").get("day "+Integer.toString(position+1)).get("meal "+Integer.toString(i+1))));
                }
                workoutItemList.put("workout", selectedCourseItem.content.get("training_nutrients").get("day "+Integer.toString(position+1)).get("workout"));
                Log.d(TAG, "137: "+ selectedCourseItem.content.get("training_nutrients").get("day "+Integer.toString(position+1)).get("workout"));



                WorkoutItemAdapter workoutItemAdapter = new WorkoutItemAdapter(getContext(), R.layout.workout_item, workoutItemList);
                lv_workoutContentList.setAdapter(workoutItemAdapter);

                lv_courseContent.setVisibility(View.GONE);
                lv_coursesList.setVisibility(View.GONE);
                lv_workoutContentList.setVisibility(View.VISIBLE);
                lv_workoutContentList.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.load_up_anim));

            }
        });



        return view;
    }

    private long CalDaysDiff(String d1, String d2)
    {
        try
        {
            Date date1 = new SimpleDateFormat("dd:MM:yyyy").parse(d1);
            Date date2 = new SimpleDateFormat("dd:MM:yyyy").parse(d2);

            return (date1.getTime() - date2.getTime()) / 86400000;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return -1;
        }

    }
    

}