package com.belar.fitquest;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


public class CoachFragment extends Fragment
{
    View view;

    CardView cv_Recommended, cv_limitedEdition1;
    ImageView iv_limitedEdition1, iv_Recommended, iv_getStarted1, iv_getStarted2, iv_getStarted3;
    CourseItem limitedEdition1;

    private String mUID;
    private String mUsername;
    private String mProfilePicUrl;


    // firebase properties //
    FirebaseDatabase mRoot = FirebaseDatabase.getInstance();
    DatabaseReference mCourses = mRoot.getReference("courses");
    ////////////////////////



    public CoachFragment(String _UID, String _Username, String _profilePicUrl)
    {
        mUID = _UID;
        mUsername = _Username;
        mProfilePicUrl = _profilePicUrl;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_coach, container, false);

        cv_Recommended = view.findViewById(R.id.cv_recommended);
        cv_limitedEdition1 = view.findViewById(R.id.cv_limitedEdition1);
        iv_limitedEdition1 = view.findViewById(R.id.iv_limitedEdition1);
        iv_Recommended = view.findViewById(R.id.iv_Recommended);
        iv_getStarted1 = view.findViewById(R.id.iv_getStarted1);
        iv_getStarted2 = view.findViewById(R.id.iv_getStarted2);
        iv_getStarted3 = view.findViewById(R.id.iv_getStarted3);


        // retrieve courses from firebase:
        mCourses.child("limitedEdition1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                HashMap<String, Object> value = (HashMap<String, Object>) snapshot.getValue();

                 Picasso.get().load(value.get("courseImageUrl").toString()).fit().into(iv_limitedEdition1);

                limitedEdition1 = new CourseItem(value);
                Log.d(TAG, "TESTING THISTHING: "+ value);
                Log.d(TAG, "TESTING CourseItem DATA: "+ limitedEdition1.content);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.d(TAG, "onCancelled: "+ error);
                
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////


        cv_Recommended.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                HashMap<String, HashMap<String, HashMap<String, String>>> map = new HashMap<String, HashMap<String, HashMap<String, String>>>();
                map.put("training_nutrients",
                        new HashMap<String, HashMap<String, String>>()
                        {{put("day 1",
                                new HashMap<String, String>(){{put("meal 1"," 4 Eggs with green salad");
                                                               put("meal 2", "100g Chicken breasts with green salad");
                                                               put("workout", "1 muscle workout");
                                                               put("meal 3"," 4 Eggs with green salad");
                                                               put("meal 4"," 4 Eggs with green salad");}});
                        put("day 2",
                                new HashMap<String, String>(){{put("meal 1"," 4 Eggs with green salad");
                                                               put("meal 2", "100g Chicken breasts with green salad");
                                                               put("workout", "1 muscle workout");
                                                               put("meal 3"," 4 Eggs with green salad");
                                                               put("meal 4"," 4 Eggs with green salad");}});
                        put("day 3",
                                new HashMap<String, String>(){{put("meal 1"," 4 Eggs with green salad");
                                                               put("meal 2", "100g Chicken breasts with green salad");
                                                               put("workout", "1 muscle workout");
                                                               put("meal 3"," 4 Eggs with green salad");
                                                               put("meal 4"," 4 Eggs with green salad");}});}});

                CourseItem test_courseItem = new CourseItem("courseez","https://firebasestorage.googleapis.com/v0/b/fitquest-68110.appspot.com/o/Tricep-workouts-for-Perfect-Toned.jpg?alt=media&token=3f04267e-0bdc-40f2-93a7-a29e8ba7900c",
                        22, 0, "aggressive shreadding", "cardio", "muscle", "This course is only advised for advanced athlete.Requires high level of stamina and muscle endurance.Proceed with caution",
                        "09/12/2022", map);


                mCourses.child("testingez").setValue(test_courseItem);
            }
        });

        cv_limitedEdition1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {




                ((MainApp)getActivity()).ShowItemInfo(limitedEdition1);
            }
        });



        return view;
    }


}
