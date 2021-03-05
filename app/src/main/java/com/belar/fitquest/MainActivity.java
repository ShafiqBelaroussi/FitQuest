package com.belar.fitquest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.*;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 999;
    VideoView IntroVideo;

    String mUID = "mUID", mUsername = "mUsername", mProfilePicUrl = "mProfilePicUrl", mEmail = "mEmail";
    String mGender = "Male", mGoal1 = "N/A", mGoal2 = "N/A", mGoal3 = "N/A", mFitnessLvl = "0", mDOB = "N/A", mWeight = "N/A", mHeight = "N/A";
    ArrayList <String> foodAlergiesList = new ArrayList<String>();

    // firebase properties //
    FirebaseDatabase mRoot = FirebaseDatabase.getInstance();
    DatabaseReference mUsers = mRoot.getReference("users");
    ////////////////////////

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor mEditor;


    /*SignupFragment signupFragment;
    SigninFragment signinFragment;*/

    /// Signup Form ////
    CardView btn_Startnow;
    RelativeLayout rl_SignupForm;
    Button btn_back_Signup, btn_agreementAccept_register, btn_agreementDecline_register;
    SignInButton btnGoogle_Signup;
    RelativeLayout rl_AgreementTerms_register;

    ///////////////////


    // / Sigin Form ////
    CardView btn_Login;
    RelativeLayout rl_SigninForm;
    Button btn_back_Signin, btn_agreementAccept_signin, btn_agreementDecline_signin;
    SignInButton btn_GoogleSignIn;
    RelativeLayout rl_AgreementTerms_signin;

    ///////////////////

    /// Profile Gender Form ///
    RelativeLayout rl_ProfileGender;
    Button btn_next1;
    TextView tv_back1;
    LinearLayout cv_male, cv_female;
    ImageView iv_male, iv_female;
    TextView tv_male, tv_female;
    //////////////////////////

    /// top 3 goals Form ///
    RelativeLayout rl_TopThreeGoals;
    Button btn_next2;
    TextView tv_back2;
    Button btn_gainStrength, btn_loseWeight, btn_increaseEndurance, btn_eatBetter, btn_improveFitness;
    TextView tv_goal1, tv_goal2, tv_goal3;
    TextView[] arr_btnGoals;
    int nGoalsSet;
    //////////////////////////

    /// Fitness level Form ///
    RelativeLayout rl_FitnessLevel;
    Button btn_next3;
    TextView tv_back3;
    Hashtable<Integer, String> hashtable_fitnessLvl = new Hashtable<Integer, String>() {{ put(0, "I tire just by standing up"); put(1, "I workout once in a while"); put(2, "I workout regularly"); put(3, "Oh trust me, I'm really fit!");}};
    TextView tv_fitnessLevelTxt;
    SeekBar sb_fitnessLvl;
    //////////////////////////

    /// Final Details Form ///
    RelativeLayout rl_FinalDetails;
    Button btn_next4;
    TextView tv_back4;
    TextView tv_dob_select, tv_weight_select, tv_height_select, tv_FoodAlergies_select;
    EditText et_foodAlergiesAdd;
    CardView cv_datepicker, cv_weightpicker, cv_heightpicker, cv_foodAlergiesCard;
    Button  btn_cancelDatePicker, btn_confirmDatePicker, btn_cancelWeightPicker, btn_confirmWeightPicker,
            btn_cancelHeightPicker, btn_confirmHeightPicker, btn_cancelFoodAlergie, btn_confirmFoodAlergie, btn_addFoodAlergy;
    DatePicker datePicker;
    NumberPicker weightPicker, weightUnitPicker, heightPicker, heightUnitPicker;
    ListView lv_foodList;
    ArrayAdapter foodAlergiesListAdapter;


    /////////////////////////

    ProgressBar pb_loading;

    /// 4-page array ///
    RelativeLayout[] FourPageArray;
    ///////////////////



    //// Firebase fields /////
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthListener;

    /////////////////////////

    @Override
    protected void onStart()
    {
        super.onStart();

        /// Firebase Methods ////
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                // allows auto login
                if(firebaseAuth.getCurrentUser() != null)
                {
                    Log.d(TAG, "onAuthStateChanged: current user not null: "+ firebaseAuth.getCurrentUser().getUid());

                    Intent MainApp = new Intent(getApplicationContext(), MainApp.class);
                    mUID = firebaseAuth.getCurrentUser().getUid();
                    mUsername = firebaseAuth.getCurrentUser().getDisplayName();
                    mProfilePicUrl = Objects.requireNonNull(firebaseAuth.getCurrentUser().getPhotoUrl()).toString();
                    mEmail = Objects.requireNonNull(firebaseAuth.getCurrentUser().getEmail());

                    if(sharedPreferences.getBoolean(firebaseAuth.getCurrentUser().getUid(), false))
                    {
                        MainApp.putExtra("mUID", mUID);
                        MainApp.putExtra("mUsername", mUsername);
                        MainApp.putExtra("mProfilePicUrl", mProfilePicUrl);
                        MainApp.putExtra("mEmail", mEmail);
                        startActivity(MainApp);
                        finish();
                    }
                    else
                    {
                        navigate_4_pageArray(1, 1, '>');
                    }
                }
                else
                {

                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = sharedPreferences.edit();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /////
        //if(getIntent().getBooleanExtra("signout", false)) mGoogleSignInClient.signOut();
        /////

        setContentView(R.layout.activity_main);


        /// signup fields ////////
        btn_Startnow = findViewById(R.id.cv_StartnowBtn);
        rl_SignupForm = findViewById(R.id.rl_SignupForm);
        btn_back_Signup = findViewById(R.id.btn_back_Signup);
        btnGoogle_Signup = findViewById(R.id.btn_GoogleSignup);
        rl_AgreementTerms_register = findViewById(R.id.agreement_terms_register);
        btn_agreementAccept_register = findViewById(R.id.btn_agreementAccept_register);
        btn_agreementDecline_register = findViewById(R.id.btn_agreementDecline_register);
        //////////////////////////////////////////////////
        /// signin fields ////////
        btn_Login = findViewById(R.id.cv_loginBtn);
        rl_SigninForm = findViewById(R.id.rl_SigninForm);
        btn_back_Signin = findViewById(R.id.btn_back_signin);
        btn_GoogleSignIn = findViewById(R.id.btn_GoogleSignin);
        rl_AgreementTerms_signin = findViewById(R.id.agreement_terms_signin);
        btn_agreementAccept_signin = findViewById(R.id.btn_agreementAccept_signin);
        btn_agreementDecline_signin = findViewById(R.id.btn_agreementDecline_signin);
        /////////////////////////////////////////////////

        /// Profile Gender Form ///
        rl_ProfileGender = findViewById(R.id.rl_ProfileGender);
        btn_next1 = findViewById(R.id.btn_Next1);
        tv_back1 = findViewById(R.id.tv_back1);
        cv_female = findViewById(R.id.cv_female);
        cv_male = findViewById(R.id.cv_male);
        tv_female = findViewById(R.id.tv_female);
        tv_male = findViewById(R.id.tv_male);
        iv_female = findViewById(R.id.iv_female);
        iv_male = findViewById(R.id.iv_male);
        //////////////////////////

        /// top 3 goals Form ///
        rl_TopThreeGoals = findViewById(R.id.rl_TopThreeGoals);
        btn_next2 = findViewById(R.id.btn_Next2);
        tv_back2 = findViewById(R.id.tv_back2);
        btn_gainStrength = findViewById(R.id.btn_gainStrength);
        btn_loseWeight = findViewById(R.id.btn_LoseWeight);
        btn_increaseEndurance = findViewById(R.id.btn_increaseEndurance);
        btn_eatBetter = findViewById(R.id.btn_eatBetter);
        btn_improveFitness = findViewById(R.id.btn_improveFitness);
        tv_goal1 = findViewById(R.id.tv_goal1);
        tv_goal2 = findViewById(R.id.tv_goal2);
        tv_goal3 = findViewById(R.id.tv_goal3);
        arr_btnGoals = new TextView[] {tv_goal1, tv_goal2, tv_goal3};
        nGoalsSet = 0;
        //////////////////////////

        /// Fitness level Form ///
        rl_FitnessLevel = findViewById(R.id.rl_FitnessLevel);
        btn_next3 = findViewById(R.id.btn_Next3);
        tv_back3 = findViewById(R.id.tv_back3);
        tv_fitnessLevelTxt = findViewById(R.id.tv_fitnessLevelTxt);
        sb_fitnessLvl = findViewById(R.id.sb_fitnessLvl);
        //////////////////////////

        /// Final Details Form ///
        rl_FinalDetails = findViewById(R.id.rl_FinalDetails);
        btn_next4 = findViewById(R.id.btn_Next4);
        tv_back4 = findViewById(R.id.tv_back4);
        tv_dob_select = findViewById(R.id.tv_dob_select);
        cv_datepicker = findViewById(R.id.cv_datepicker);
        btn_cancelDatePicker = findViewById(R.id.btn_cancelDatePicker);
        btn_confirmDatePicker = findViewById(R.id.btn_confirmDatePicker);
        datePicker = findViewById(R.id.datepicker);
        tv_weight_select = findViewById(R.id.tv_Weight_select);
        cv_weightpicker = findViewById(R.id.cv_weightpicker);
        btn_cancelWeightPicker = findViewById(R.id.btn_cancelWeightPicker);
        btn_confirmWeightPicker = findViewById(R.id.btn_confirmWeightPicker);
        weightPicker = findViewById(R.id.weightPicker);
        weightUnitPicker = findViewById(R.id.weightUnitPicker);
        tv_height_select = findViewById(R.id.tv_Height_select);
        cv_heightpicker = findViewById(R.id.cv_heighttpicker);
        btn_cancelHeightPicker = findViewById(R.id.btn_cancelHeighttPicker);
        btn_confirmHeightPicker = findViewById(R.id.btn_confirmHeightPicker);
        heightPicker = findViewById(R.id.heightPicker);
        heightUnitPicker = findViewById(R.id.heightUnitPicker);
        et_foodAlergiesAdd = findViewById(R.id.et_foodAlergiesAdd);
        cv_foodAlergiesCard = findViewById(R.id.cv_foodAlergiesCard);
        btn_addFoodAlergy = findViewById(R.id.btn_addFoodAlergy);
        btn_cancelFoodAlergie = findViewById(R.id.btn_cancelFoodAlergies);
        btn_confirmFoodAlergie = findViewById(R.id.btn_confirmFoodAlergies);
        tv_FoodAlergies_select = findViewById(R.id.tv_tv_FoodAlergies_select);
        lv_foodList = findViewById(R.id.lv_foodList);
        pb_loading = findViewById(R.id.pb_loading);
        /////////////////////////////////////////////////////////////////////////

        weightPicker.setMinValue(40);
        weightPicker.setMaxValue(150);
        weightPicker.setWrapSelectorWheel(false);
        weightUnitPicker.setMinValue(0);
        weightUnitPicker.setMaxValue(1);
        weightUnitPicker.setWrapSelectorWheel(false);
        weightUnitPicker.setDisplayedValues(new String[] {"kg", "lb"});

        heightPicker.setMinValue(130);
        heightPicker.setMaxValue(250);
        heightPicker.setWrapSelectorWheel(false);
        heightUnitPicker.setMinValue(0);
        heightUnitPicker.setMaxValue(0);
        heightUnitPicker.setWrapSelectorWheel(false);
        heightUnitPicker.setDisplayedValues(new String[] {"cm"});

        /////////////////////////

        /// 4-page array ///
        FourPageArray = new RelativeLayout [] {rl_ProfileGender, rl_TopThreeGoals, rl_FitnessLevel, rl_FinalDetails};
        ///////////////////

        IntroVideo = findViewById(R.id.vv_IntroVideo);

        String videoPath = "android.resource://" + getPackageName() + "/" +  R.raw.introvideo;
        Uri uri = Uri.parse(videoPath);
        IntroVideo.setVideoURI(uri);

        IntroVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                IntroVideo.start();
            }
        });

        IntroVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                IntroVideo.start();
            }
        });

        /// signup btns methods ///
        SignupButton(); Btn_backSignup();
        BtnGoogleSignup();
        ////sigin btns methods///
        SigninButton(); Btn_backSignin();
        BtnGoogleSignin();
        /// register Agreement terms methods///
        BtnAgreementAccept_register();
        BtnAgreementDecline_register();
        /// signin Agreement terms methods///
        BtnAgreementAccept_signin();
        BtnAgreementDecline_signin();
        createRequest_Google();
        ///////////////////////

        // 4-page navigations //
        tv_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                navigate_4_pageArray(1, -1, '<');
            }
        });
        btn_next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                navigate_4_pageArray(1, 2, '>');
            }
        });

        tv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                navigate_4_pageArray(2, -1, '<');
            }
        });
        btn_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mGoal1 = tv_goal1.getText().length() >1? tv_goal1.getText().toString() : "N/A";
                mGoal2 = tv_goal2.getText().length() >1? tv_goal2.getText().toString() : "N/A";
                mGoal3 = tv_goal3.getText().length() >1? tv_goal3.getText().toString() : "N/A";
                if(!(mGoal1.equals("N/A") && mGoal2.equals("N/A") && mGoal3.equals("N/A"))) navigate_4_pageArray(2, 3, '>');
                else Toast.makeText(getApplicationContext(), "Select your primary goal", Toast.LENGTH_SHORT).show();
            }
        });

        tv_back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                navigate_4_pageArray(3, -1, '<');
            }
        });
        btn_next3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mFitnessLvl = String.valueOf(sb_fitnessLvl.getProgress());

                navigate_4_pageArray(3, 4, '>');
            }
        });

        tv_back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                navigate_4_pageArray(4, -1, '<');
            }
        });

        btn_next4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!mDOB.equals("N/A") && !mWeight.equals("N/A") && !mHeight.equals("N/A"))
                /// first profile setup
                {
                    firebaseUsers user = new firebaseUsers(mUID, mUsername, mEmail, mGender, mGoal1, mGoal2, mGoal3, mFitnessLvl, mDOB, mWeight, mHeight, foodAlergiesList, new ArrayList<>());
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    pb_loading.setVisibility(View.VISIBLE);

                    mUsers.child(user.getmUsername()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
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
                            MainApp.putExtra("mWeight", mWeight);
                            startActivity(MainApp);
                            finish();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Profile data incomplete", Toast.LENGTH_SHORT).show();
                }

            }
        });





        ///////////////////////

        cv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ManageCardView(cv_female, cv_male, iv_female, iv_male, tv_female, tv_male, true);
                mGender = "Female";
            }
        });
        cv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ManageCardView(cv_male, cv_female, iv_male, iv_female, tv_male, tv_female, false);
                mGender = "Male";
            }
        });
        btn_gainStrength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                nGoalsSet = ManageTopGoals(btn_gainStrength, nGoalsSet);
            }
        });
        btn_loseWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                nGoalsSet = ManageTopGoals(btn_loseWeight, nGoalsSet);
            }
        });
        btn_increaseEndurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                nGoalsSet = ManageTopGoals(btn_increaseEndurance, nGoalsSet);
            }
        });
        btn_eatBetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                nGoalsSet = ManageTopGoals(btn_eatBetter, nGoalsSet);
            }
        });
        btn_improveFitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                nGoalsSet = ManageTopGoals(btn_improveFitness, nGoalsSet);
            }
        });
        sb_fitnessLvl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                tv_fitnessLevelTxt.setText(hashtable_fitnessLvl.get(sb_fitnessLvl.getProgress()));
                mFitnessLvl = String.valueOf(sb_fitnessLvl.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        tv_dob_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Calendar today  = Calendar.getInstance();
                int year = today.get(Calendar.YEAR);
                int month = today.get(Calendar.MONTH);
                int day = today.get(Calendar.DAY_OF_MONTH);

                datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tv_dob_select.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                    }
                });

                cv_datepicker.setVisibility(View.VISIBLE);
                cv_datepicker.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.load_up_anim));
            }
        });
        btn_confirmDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                tv_dob_select.setText(datePicker.getDayOfMonth() +"/"+ datePicker.getMonth() +"/"+ datePicker.getYear());
                mDOB = tv_dob_select.getText().toString();

                cv_datepicker.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.load_down_anim));
                cv_datepicker.setVisibility(View.GONE);
            }
        });
        btn_cancelDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                tv_dob_select.setText("select >");
                mDOB = "N/A";
                cv_datepicker.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.load_down_anim));
                cv_datepicker.setVisibility(View.GONE);
            }
        });

        tv_weight_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                cv_weightpicker.setVisibility(View.VISIBLE);
                cv_weightpicker.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.load_up_anim));
            }
        });
        btn_cancelWeightPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                tv_weight_select.setText("select >");
                mWeight = "N/A";
                cv_weightpicker.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.load_down_anim));
                cv_weightpicker.setVisibility(View.GONE);
            }
        });
        btn_confirmWeightPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                tv_weight_select.setText(String.valueOf(weightPicker.getValue()) + String.valueOf(weightUnitPicker.getDisplayedValues()[weightUnitPicker.getValue()]));
                mWeight = tv_weight_select.getText().toString();

                cv_weightpicker.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.load_down_anim));
                cv_weightpicker.setVisibility(View.GONE);
            }
        });
        weightUnitPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {
                if (newVal == 1) {weightPicker.setMinValue(85); weightPicker.setMaxValue(350);}
                else {weightPicker.setMinValue(40); weightPicker.setMaxValue(150);}
            }
        });

        tv_height_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                cv_heightpicker.setVisibility(View.VISIBLE);
                cv_heightpicker.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.load_up_anim));
            }
        });
        btn_cancelHeightPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                tv_height_select.setText("select >");
                mHeight = "N/A";

                cv_heightpicker.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.load_down_anim));
                cv_heightpicker.setVisibility(View.GONE);
            }
        });
        btn_confirmHeightPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                tv_height_select.setText(String.valueOf(heightPicker.getValue()) + String.valueOf(heightUnitPicker.getDisplayedValues()[heightUnitPicker.getValue()]));
                mHeight = tv_height_select.getText().toString();

                cv_heightpicker.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.load_down_anim));
                cv_heightpicker.setVisibility(View.GONE);
            }
        });

        tv_FoodAlergies_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                cv_foodAlergiesCard.setVisibility(View.VISIBLE);
                cv_foodAlergiesCard.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.load_up_anim));
            }
        });
        btn_cancelFoodAlergie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                foodAlergiesList.clear();
                foodAlergiesListAdapter = new ArrayAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, foodAlergiesList);
                lv_foodList.setAdapter(foodAlergiesListAdapter);

                cv_foodAlergiesCard.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.load_down_anim));
                cv_foodAlergiesCard.setVisibility(View.GONE);
            }
        });
        btn_confirmFoodAlergie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                cv_foodAlergiesCard.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.load_down_anim));
                cv_foodAlergiesCard.setVisibility(View.GONE);
            }
        });
        btn_addFoodAlergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (et_foodAlergiesAdd.getText().toString().length() > 0)
                {
                    foodAlergiesList.add(et_foodAlergiesAdd.getText().toString());
                    et_foodAlergiesAdd.setText("");
                    foodAlergiesListAdapter = new ArrayAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, foodAlergiesList);
                    lv_foodList.setAdapter(foodAlergiesListAdapter);
                }

            }
        });

        et_foodAlergiesAdd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                switch (actionId)
                {
                    case EditorInfo.IME_ACTION_DONE:
                        if (et_foodAlergiesAdd.getText().toString().length() > 0)
                        {
                            foodAlergiesList.add(et_foodAlergiesAdd.getText().toString());
                            et_foodAlergiesAdd.setText("");
                            foodAlergiesListAdapter = new ArrayAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, foodAlergiesList);
                            lv_foodList.setAdapter(foodAlergiesListAdapter);
                        }
                }
                return false;
            }
        });
        lv_foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                foodAlergiesList.remove(position);
                lv_foodList.invalidateViews();
            }
        });


    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void ManageCardView(LinearLayout _cardView1, LinearLayout _cardView2, ImageView _imageView1, ImageView _imageView2, TextView _textView1, TextView _textView2, boolean isFemale) {

        /// views1 are the selected ones
        _cardView1.setBackgroundColor(getColor(R.color.White)); _cardView2.setBackgroundColor(getColor(R.color.TxtGray));
        if (isFemale) {
            _imageView1.setBackground(getDrawable(R.drawable.female_selected));
            _imageView2.setBackground(getDrawable(R.drawable.male_unselected));
        } else {
            _imageView1.setBackground(getDrawable(R.drawable.male_selected));
            _imageView2.setBackground(getDrawable(R.drawable.female_unselected));
        }
        _textView1.setTextColor(getColor(R.color.SecondaryBlue)); _textView2.setTextColor(getColor(R.color.ItemGray));
    }

    private int ManageTopGoals (Button _clickedButton, int _goalNum) {
        arr_btnGoals[_goalNum].setText(_clickedButton.getText()); _clickedButton.setClickable(false);
        if(_goalNum > 2) return 2; else return _goalNum +1;
    }

    private void createRequest_Google()
    {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn_Google()
    {
        if(getIntent().getBooleanExtra("signout", false)) mGoogleSignInClient.signOut();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }
            catch (ApiException e)
            {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            mUID = user.getUid();
                            mUsername = user.getDisplayName();
                            mProfilePicUrl = Objects.requireNonNull(user.getPhotoUrl()).toString();
                            mEmail = Objects.requireNonNull(user.getEmail());


                            if(sharedPreferences.getBoolean(user.getUid(), false))
                            {
                                Intent MainApp = new Intent(getApplicationContext(), MainApp.class);


                                MainApp.putExtra("mUID", mUID);
                                MainApp.putExtra("mUsername", mUsername);
                                MainApp.putExtra("mProfilePicUrl", mProfilePicUrl);
                                MainApp.putExtra("mEmail", mEmail);
                                MainApp.putExtra("mGender", mGender);
                                MainApp.putExtra("mHeight", mHeight);
                                MainApp.putExtra("mWeight", mWeight);
                                startActivity(MainApp);
                                finish();

                            }
                            else
                            {

                                navigate_4_pageArray(1, 1, '>');
                            }
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.

                        }

                        // ...
                    }
                });
    }


    private void navigate_4_pageArray(int currentPage, int destinationPage, char AnimationMode)
    {
        if(AnimationMode == '>')
        {
            FourPageArray[destinationPage -1].setVisibility(View.VISIBLE);
            FourPageArray[destinationPage -1].setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_enter));
        }
        else
        {
            FourPageArray[currentPage -1].setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_exit));
            FourPageArray[currentPage -1].setVisibility(View.GONE);
        }
    }

    private void BtnAgreementDecline_register()
    {
        btn_agreementDecline_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rl_AgreementTerms_register.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_down_anim));
                rl_AgreementTerms_register.setVisibility(View.GONE);
            }
        });
    }

    private void BtnAgreementAccept_register()
    {
        btn_agreementAccept_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rl_AgreementTerms_register.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_down_anim));
                rl_AgreementTerms_register.setVisibility(View.GONE);

                signIn_Google();
            }
        });
    }

    private void BtnAgreementDecline_signin()
    {
        btn_agreementDecline_signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rl_AgreementTerms_signin.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_down_anim));
                rl_AgreementTerms_signin.setVisibility(View.GONE);
            }
        });
    }

    private void BtnAgreementAccept_signin()
    {
        btn_agreementAccept_signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rl_AgreementTerms_signin.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_down_anim));
                rl_AgreementTerms_signin.setVisibility(View.GONE);

                signIn_Google();
            }
        });
    }


    private void BtnGoogleSignup()
    {
        btnGoogle_Signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rl_AgreementTerms_register.setVisibility(View.VISIBLE);
                rl_AgreementTerms_register.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_up_anim));
            }
        });
    }

    private void BtnGoogleSignin()
    {
        btn_GoogleSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rl_AgreementTerms_signin.setVisibility(View.VISIBLE);
                rl_AgreementTerms_signin.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_up_anim));
            }
        });
    }

    private void Btn_backSignup()
    {
        btn_back_Signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                rl_SignupForm.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_down_anim));
                rl_SignupForm.setVisibility(View.GONE);
            }
        });
    }

    private void Btn_backSignin()
    {
        btn_back_Signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rl_SigninForm.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_down_anim));
                rl_SigninForm.setVisibility(View.GONE);
            }
        });
    }



    private void SignupButton()
    {
        btn_Startnow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rl_SignupForm.setVisibility(View.VISIBLE);
                rl_SignupForm.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_up_anim));
            }
        });
    }

    private void SigninButton()
    {
        btn_Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rl_SigninForm.setVisibility(View.VISIBLE);
                rl_SigninForm.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_up_anim));
            }
        });
    }


}