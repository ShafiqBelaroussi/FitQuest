package com.belar.fitquest;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class ProfileFragment extends Fragment
{

    private static final String TAG = "ProfileFragment";
    /// profile page ///
    Button btn_editProfile;
    ImageView iv_profilePic, iv_profilePic_editprofile;
    ///////////////////

    /// edit profile page //
    RelativeLayout rl_EditProfile;
    TextView tv_back, tv_profilename, tv_firstname, tv_lastname, tv_email, tv_Gender, tv_Height, tv_Weight, tv_facebook, tv_instagram;
    CardView cv_etCard;
    Button btn_confirmET, btn_cancelET, btn_logOut, btn_DeleteAccount;
    EditText et_text;
    /////////////////////////

    // firebase properties //
    FirebaseDatabase mRoot = FirebaseDatabase.getInstance();
    DatabaseReference mUser = mRoot.getReference("users");
    FirebaseAuth auth;
    ////////////////////////

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor mEditor;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public ProfileFragment()
    {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        auth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        iv_profilePic = view.findViewById(R.id.iv_PosterProfilePic);
        iv_profilePic_editprofile = view.findViewById(R.id.iv_profilePic_editprofile);
        rl_EditProfile = view.findViewById(R.id.rl_EditProfile);
        btn_editProfile = view.findViewById(R.id.btn_editProfile);
        tv_back = view.findViewById(R.id.tv_back);
        tv_profilename = view.findViewById(R.id.tv_username);
        tv_firstname = view.findViewById(R.id.tv_firstname);
        tv_lastname = view.findViewById(R.id.tv_lastname);
        tv_email = view.findViewById(R.id.tv_email);
        tv_Gender = view.findViewById(R.id.tv_Gender);
        tv_Height = view.findViewById(R.id.tv_Height);
        tv_Weight = view.findViewById(R.id.tv_Weight);
        tv_facebook = view.findViewById(R.id.tv_facebook);
        tv_instagram = view.findViewById(R.id.tv_instagram);
        cv_etCard = view.findViewById(R.id.cv_etCard);
        btn_confirmET = view.findViewById(R.id.btn_confirmET);
        btn_cancelET = view.findViewById(R.id.btn_cancelET);
        et_text = view.findViewById(R.id.et_text);

        tv_profilename.setText(((MainApp) requireActivity()).mUsername);
        btn_logOut = view.findViewById(R.id.btn_logOut);
        btn_DeleteAccount = view.findViewById(R.id.btn_DeleteAccount);

        btn_editProfile.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v)
            {
                rl_EditProfile.setVisibility(View.VISIBLE);
                rl_EditProfile.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_left_enter));
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                rl_EditProfile.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_exit));
                rl_EditProfile.setVisibility(View.GONE);
            }
        });

        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                /*Intent mainActivity = new Intent(getContext(), MainActivity.class);
                startActivity(mainActivity);
                getActivity().finish();*/
            }
        });

        btn_DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "line 145");

                auth.getCurrentUser().delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void unused)
                            {
                                Log.d(TAG, "line 153");
                                mUser.child(tv_profilename.getText().toString()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Log.d(TAG, "line 158");
                                        auth.signOut();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e)
                            {
                                Log.d(TAG, "Line 165: failed to delete user due to: "+ e);

                            }
                        });

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth)
            {
                if(firebaseAuth.getCurrentUser() == null)
                {


                    Intent mainActivity = new Intent(((MainApp) requireActivity()).profileFragment.requireContext(), MainActivity.class);
                    mainActivity.putExtra("signout", true);
                    startActivity(mainActivity);
                    //requireActivity().finish();
                    Log.d(TAG, "line 185");

                    /*sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                    mEditor = sharedPreferences.edit();
                    mEditor.putBoolean(mUID, true);
                    mEditor.commit();
                    pb_loading.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Intent MainApp = new Intent(MainActivity.this, MainApp.class);
                    MainApp.putExtra("mProfilePicUrl", mProfilePicUrl);
                    MainApp.putExtra("mUsername", mUsername);
                    MainApp.putExtra("mEmail", mEmail);
                    MainApp.putExtra("mGender", mGender);
                    MainApp.putExtra("mHeight", mHeight);
                    MainApp.putExtra("mWeight", mWeight);*/
                }
            }
        };
        auth.addAuthStateListener(mAuthListener);

        try
        {
            tv_firstname.setText(auth.getCurrentUser().getDisplayName().split(" ")[0]);
            tv_lastname.setText(auth.getCurrentUser().getDisplayName().split(" ")[1]);
        }
        catch (Exception e)
        {
            tv_firstname.setText(auth.getCurrentUser().getDisplayName());
            tv_lastname.setText("");
        }
        tv_email.setText(auth.getCurrentUser().getEmail());

        mUser.child(auth.getCurrentUser().getDisplayName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try
                {
                    tv_Gender.setText(snapshot.child("mGender").getValue().toString());
                }
                catch (NullPointerException nullPointerException)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        Picasso.get().load(((MainApp)getActivity()).mProfilePicUrl).placeholder(R.drawable.ic_profile).into(iv_profilePic);
        Picasso.get().load(((MainApp)getActivity()).mProfilePicUrl).placeholder(R.drawable.ic_profile).into(iv_profilePic_editprofile);



        return view;
    }

    private void showET(String _hint) {

        cv_etCard.setVisibility(View.VISIBLE);
        cv_etCard.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.load_up_anim));
        et_text.setHint(_hint);



    }

    void confirmET(TextView view)
    {
        btn_confirmET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                view.setText(et_text.getText().toString());
                cv_etCard.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.load_down_anim));
                cv_etCard.setVisibility(View.GONE);
            }
        });
    }

}