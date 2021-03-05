package com.belar.fitquest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainApp extends AppCompatActivity
{

    private static final String TAG = "MainApp";
    BottomNavigationView bottomNav;

    boolean is_InfoPageOpen = false;

    /// Fragments ///
    FeedFragment feedFragment;
    CoachFragment coachFragment;
    ProfileFragment profileFragment;
    CoursesFragment coursesFragment;
    private Fragment fragment;
    ////////////////

    /// Item Info Page ///
    RelativeLayout rl_ItemInfoPage;
    ImageView iv_ItemImage;
    Button btn_closeItemInfoPage, btn_showSubsInfo;
    TextView tv_itemLength, tv_ItemName, tv_itemContains1, tv_itemContains2, tv_itemRequirement;
    ///////////////////////////////

    /// Item Billing Page ///
    RelativeLayout rl_SubsInfoPage;
    Button btn_closeBillingInfoPage;
    TabLayout tl_subsInfoTabs;
    ViewPager vp_BillingFragmentContainer;
    ViewpagerAdapter viewpagerAdapter;
    ////////////////////////

    public String mUID, mUsername, mProfilePicUrl, mEmail, mGender, mHeight, mWeight;

    PurchasesUpdatedListener purchasesUpdatedListener;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;
    private BillingClient billingClient;
    ArrayList<String> skuList = new ArrayList<>();

    /// subs ///
    SkuDetails SKUdetail_training_nutrients_1m;
    SkuDetails SKUdetail_training_nutrients_3m;
    SkuDetails SKUdetail_training_nutrients_6m;
    SkuDetails SKUdetail_training_1m;
    SkuDetails SKUdetail_training_3m;
    SkuDetails SKUdetail_training_6m;
    static final String TRAINING_NUTRIENTS_1M = "training_nutrients_1m";
    static final String TRAINING_NUTRIENTS_3M = "training_nutrients_3m";
    static final String TRAINING_NUTRIENTS_6M = "training_nutrients_6m";
    static final String TRAINING_1M = "training_1m";
    static final String TRAINING_3M = "training_3m";
    static final String TRAINING_6M = "training_6m";
    ///////////

    public CourseItem selectedCourseItem;

    // firebase properties //
    FirebaseDatabase mRoot = FirebaseDatabase.getInstance();
    DatabaseReference mUser = mRoot.getReference("users");
    ////////////////////////


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart:");
        /*List<Purchase> purchasesList = billingClient.queryPurchases(BillingClient.SkuType.SUBS).getPurchasesList();
        if(purchasesList != null && purchasesList.size() > 0)
        {
            for (Purchase purchase: purchasesList)
            {
                if(purchase.getSku().equals(TRAINING_1M) || purchase.getSku().equals(TRAINING_3M) || purchase.getSku().equals(TRAINING_6M))
                {
                    // workout only
                    Toast.makeText(getApplicationContext(), "Submsctiption unavailable", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(purchase.getSku().equals(TRAINING_NUTRIENTS_1M) || purchase.getSku().equals(TRAINING_NUTRIENTS_3M) || purchase.getSku().equals(TRAINING_NUTRIENTS_6M))
                {
                    // workout plus nutrients
                    Log.d(TAG, "onClick: already subed to training and nutrients");
                    Toast.makeText(getApplicationContext(), "already subed to training and nutrients", Toast.LENGTH_SHORT).show();
                    //Log.d(TAG, "onClick: title: "+ selectedCourseItem.courseHost);

                    // check if selected course already aquired by the user:
                    mUser.child(mUsername).child("courses").child(selectedCourseItem.courseHost).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                Log.d(TAG, "onDataChange: we have it ");
                                Log.d(TAG, "onDataChange: snapshot: "+ snapshot);

                                *//*bottomNav.setVisibility(View.VISIBLE);
                                bottomNav.setSelectedItemId(R.id.btm_menu_Courses);
                                rl_ItemInfoPage.setVisibility(View.GONE);
                                getSupportFragmentManager().beginTransaction().addToBackStack("prev frag").replace(R.id.fl_fragmentContainer, coursesFragment).commit();*//*
                            }
                            else
                            {
                                Log.d(TAG, "onDataChange: we dont have it ");
                                Log.d(TAG, "onDataChange: snapshot: "+ snapshot);


                                selectedCourseItem.setDateAquired(aquireCurrentDate());
                                mUser.child(mUsername).child("courses").child(selectedCourseItem.courseHost).setValue(selectedCourseItem);
                                bottomNav.setVisibility(View.VISIBLE);
                                bottomNav.setSelectedItemId(R.id.btm_menu_Courses);
                                rl_ItemInfoPage.setVisibility(View.GONE);
                                getSupportFragmentManager().beginTransaction().addToBackStack("prev frag").replace(R.id.fl_fragmentContainer, coursesFragment).commit();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else
                {
                    // not subscribed
                    ShowSubsPage();
                }
            }
        }

        else
        {
            mUser.child(mUsername).child("courses").setValue(null);
            ShowSubsPage();
        }*/

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "line 192: onBillingSetupFinished: OK");

                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                    billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                            // Process the result.
                            for (SkuDetails skuDetail : skuDetailsList) {
                                switch (skuDetail.getSku()) {
                                    case TRAINING_NUTRIENTS_1M:
                                        SKUdetail_training_nutrients_1m = skuDetail;
                                        Log.d(TAG, "onSkuDetailsResponse: switch: sku id: " + SKUdetail_training_nutrients_1m.getSku());
                                        break;
                                    case TRAINING_NUTRIENTS_3M:
                                        SKUdetail_training_nutrients_3m = skuDetail;
                                        Log.d(TAG, "onSkuDetailsResponse: switch: sku id: " + SKUdetail_training_nutrients_3m.getSku());
                                        break;
                                    case TRAINING_NUTRIENTS_6M:
                                        SKUdetail_training_nutrients_6m = skuDetail;
                                        Log.d(TAG, "onSkuDetailsResponse: switch: sku id: " + SKUdetail_training_nutrients_6m.getSku());
                                        break;
                                    case TRAINING_1M:
                                        SKUdetail_training_1m = skuDetail;
                                        Log.d(TAG, "onSkuDetailsResponse: switch: sku id: " + SKUdetail_training_1m.getSku());
                                        break;
                                    case TRAINING_3M:
                                        SKUdetail_training_3m = skuDetail;
                                        Log.d(TAG, "onSkuDetailsResponse: switch: sku id: " + SKUdetail_training_3m.getSku());
                                        break;
                                    case TRAINING_6M:
                                        SKUdetail_training_6m = skuDetail;
                                        Log.d(TAG, "onSkuDetailsResponse: switch: sku id: " + SKUdetail_training_6m.getSku());
                                        break;

                                }
                            }


                            viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());

                            viewpagerAdapter.addFragment(new Billing_1m(SKUdetail_training_nutrients_1m, SKUdetail_training_1m), "1 MONTH");
                            viewpagerAdapter.addFragment(new Billing_3m(SKUdetail_training_nutrients_3m, SKUdetail_training_3m), "3 MONTHS");
                            viewpagerAdapter.addFragment(new Billing_6m(SKUdetail_training_nutrients_6m, SKUdetail_training_6m), "6 MONTHS");
                            vp_BillingFragmentContainer.setAdapter(viewpagerAdapter);
                            tl_subsInfoTabs.setupWithViewPager(vp_BillingFragmentContainer, true);


                        }
                    });
                    billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, new PurchaseHistoryResponseListener() {
                        @Override
                        public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> list) {
                            Log.d(TAG, "Line 345: onPurchaseHistoryResponse: " + list);

                        }
                    });

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                billingClient.startConnection(this);
            }
        });
        List<Purchase> purchasesList = billingClient.queryPurchases(BillingClient.SkuType.SUBS).getPurchasesList();
        Log.d(TAG, "Line 156: onClick: purchase list: "+ purchasesList);
        if(purchasesList.size() > 0)
        {
            Log.d(TAG, "line 265: ");
        }
        else
        {
            Log.d(TAG, "line 269: ");
            if(!(fragment instanceof FeedFragment))
            {
                ShowSubsPage();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        Log.d(TAG, "onCreate:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        mUID = getIntent().getStringExtra("mUID");
        mUsername = getIntent().getStringExtra("mUsername");
        mProfilePicUrl = getIntent().getStringExtra("mProfilePicUrl");
        mEmail = getIntent().getStringExtra("mEmail");

        /*Log.d(TAG, "line 205: onCreate: height: "+ mHeight);
        Log.d(TAG, "line 205: onCreate: weight: "+ mWeight);*/

        //// fragments ////
        feedFragment = new FeedFragment();
        coachFragment = new CoachFragment(getIntent().getStringExtra("UID"), getIntent().getStringExtra("userName"), getIntent().getStringExtra("ProfilePicUrl"));
        profileFragment = new ProfileFragment();
        coursesFragment = new CoursesFragment(mUsername);
        //////////////////

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavClicked);
        bottomNav.setSelectedItemId(R.id.btm_menu_Coach);

        /// Item Info Page ///
        rl_ItemInfoPage = findViewById(R.id.rl_ItemInfoPage);
        btn_closeItemInfoPage = findViewById(R.id.btn_closeItemInfoPage);
        btn_showSubsInfo = findViewById(R.id.btn_showSubsInfo);
        iv_ItemImage = findViewById(R.id.iv_ItemImage);
        tv_itemLength = findViewById(R.id.tv_ItemLength);
        tv_ItemName = findViewById(R.id.tv_ItemName);
        tv_itemContains1 = findViewById(R.id.tv_contains1);
        tv_itemContains2 = findViewById(R.id.tv_contains2);
        tv_itemRequirement = findViewById(R.id.tv_ItemRequirement);

        btn_showSubsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                List<Purchase> purchasesList = billingClient.queryPurchases(BillingClient.SkuType.SUBS).getPurchasesList();
                Log.d(TAG, "Line 156: onClick: purchase list: "+ purchasesList);
                if(purchasesList.size() > 0)
                {
                    for (Purchase purchase: purchasesList)
                    {
                        if(purchase.getSku().equals(TRAINING_1M) || purchase.getSku().equals(TRAINING_3M) || purchase.getSku().equals(TRAINING_6M))
                        {
                            // workout only
                            Log.d(TAG, "This Subscription is not available at the moment. please select Training & Nutrients");


                            break;
                        }
                        else if(purchase.getSku().equals(TRAINING_NUTRIENTS_1M) || purchase.getSku().equals(TRAINING_NUTRIENTS_3M) || purchase.getSku().equals(TRAINING_NUTRIENTS_6M))
                        {
                            // workout plus nutrients
                            Log.d(TAG, "onClick: already subed to training and nutrients");
                            Log.d(TAG, "onClick: title: "+ selectedCourseItem.courseHost);

                            // check if selected course already aquired by the user:
                            mUser.child(mUsername).child("courses").child(selectedCourseItem.courseHost).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists())
                                    {
                                        Log.d(TAG, "onDataChange: we have it ");
                                        Log.d(TAG, "onDataChange: snapshot: "+ snapshot);

                                        bottomNav.setVisibility(View.VISIBLE);
                                        bottomNav.setSelectedItemId(R.id.btm_menu_Courses);
                                        rl_ItemInfoPage.setVisibility(View.GONE);
                                        getSupportFragmentManager().beginTransaction().addToBackStack("prev frag").replace(R.id.fl_fragmentContainer, coursesFragment).commit();
                                        Btn_closeSubsPage();
                                        Btn_closeItemInfoPage();
                                    }
                                    else
                                    {
                                        Log.d(TAG, "onDataChange: we dont have it ");
                                        Log.d(TAG, "onDataChange: snapshot: "+ snapshot);


                                        selectedCourseItem.setDateAquired(aquireCurrentDate());
                                        mUser.child(mUsername).child("courses").child(selectedCourseItem.courseHost).setValue(selectedCourseItem);
                                        bottomNav.setVisibility(View.VISIBLE);
                                        bottomNav.setSelectedItemId(R.id.btm_menu_Courses);
                                        rl_ItemInfoPage.setVisibility(View.GONE);
                                        getSupportFragmentManager().beginTransaction().addToBackStack("prev frag").replace(R.id.fl_fragmentContainer, coursesFragment).commit();
                                        Btn_closeSubsPage();
                                        Btn_closeItemInfoPage();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Btn_closeItemInfoPage();
                                }
                            });
                        }
                        else
                        {
                            // not subscribed
                            ShowSubsPage();
                        }
                    }
                }
                else
                {
                    ShowSubsPage();
                }



            }
        });
        Btn_closeItemInfoPage();
        ///////////////////////
        /// Billing info Page ///
        rl_SubsInfoPage = findViewById(R.id.rl_SubsInfoPage);
        btn_closeBillingInfoPage = findViewById(R.id.btn_closeBillingInfoPage);
        btn_closeBillingInfoPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Btn_closeSubsPage();
            }
        });

        tl_subsInfoTabs = findViewById(R.id.tl_subsInfoTabs);
        vp_BillingFragmentContainer = findViewById(R.id.vp_BillingFragmentContainer);



        ////////////////////////


        /// billing ///
        skuList.add(TRAINING_NUTRIENTS_1M);
        skuList.add(TRAINING_NUTRIENTS_3M);
        skuList.add(TRAINING_NUTRIENTS_6M);
        skuList.add(TRAINING_1M);
        skuList.add(TRAINING_3M);
        skuList.add(TRAINING_6M);

        purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
           public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases)
            {

                Log.d(TAG, " line 341: Purchase list: "+ purchases);

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                    Log.d(TAG, "onPurchasesUpdated: purchase list != null?");
                    for (Purchase purchase : purchases)
                    {
                        handlePurchase(purchase);
                    }
                }
                else
                {
                    Log.d(TAG, "onPurchasesUpdated: PurchaseList = null");
                }
            }
        };
        acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult)
            {
                Log.d(TAG, "onAcknowledgePurchaseResponse: acknowledged");
            }
        };

        billingClient = BillingClient.newBuilder(this).setListener(purchasesUpdatedListener).enablePendingPurchases().build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult)
            {
                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK)
                {
                    Log.d(TAG, "onBillingSetupFinished: OK");

                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                    billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                        // Process the result.
                        for (SkuDetails skuDetail: skuDetailsList) {
                            switch (skuDetail.getSku())
                            {
                                case TRAINING_NUTRIENTS_1M:
                                    SKUdetail_training_nutrients_1m = skuDetail;
                                    Log.d(TAG, "onSkuDetailsResponse: switch: sku id: "+ SKUdetail_training_nutrients_1m.getSku());
                                    break;
                                case TRAINING_NUTRIENTS_3M:
                                    SKUdetail_training_nutrients_3m = skuDetail;
                                    Log.d(TAG, "onSkuDetailsResponse: switch: sku id: "+ SKUdetail_training_nutrients_3m.getSku());
                                    break;
                                case TRAINING_NUTRIENTS_6M:
                                    SKUdetail_training_nutrients_6m = skuDetail;
                                    Log.d(TAG, "onSkuDetailsResponse: switch: sku id: "+ SKUdetail_training_nutrients_6m.getSku());
                                    break;
                                case TRAINING_1M:
                                    SKUdetail_training_1m = skuDetail;
                                    Log.d(TAG, "onSkuDetailsResponse: switch: sku id: "+ SKUdetail_training_1m.getSku());
                                    break;
                                case TRAINING_3M:
                                    SKUdetail_training_3m = skuDetail;
                                    Log.d(TAG, "onSkuDetailsResponse: switch: sku id: "+ SKUdetail_training_3m.getSku());
                                    break;
                                case TRAINING_6M:
                                    SKUdetail_training_6m = skuDetail;
                                    Log.d(TAG, "onSkuDetailsResponse: switch: sku id: "+ SKUdetail_training_6m.getSku());
                                    break;

                            }
                        }


                            viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());

                            viewpagerAdapter.addFragment(new Billing_1m(SKUdetail_training_nutrients_1m, SKUdetail_training_1m), "1 MONTH");
                            viewpagerAdapter.addFragment(new Billing_3m(SKUdetail_training_nutrients_3m, SKUdetail_training_3m), "3 MONTHS");
                            viewpagerAdapter.addFragment(new Billing_6m(SKUdetail_training_nutrients_6m, SKUdetail_training_6m), "6 MONTHS");
                            vp_BillingFragmentContainer.setAdapter(viewpagerAdapter);
                            tl_subsInfoTabs.setupWithViewPager(vp_BillingFragmentContainer, true);


                        }
                    });
                    billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, new PurchaseHistoryResponseListener() {
                        @Override
                        public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> list)
                        {
                            Log.d(TAG, "Line 345: onPurchaseHistoryResponse: "+ list);

                        }
                    });

                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                billingClient.startConnection(this);
            }
        });




        //////////////

    }


    private String aquireCurrentDate() {

        return new SimpleDateFormat("dd:MM:yyyy").format(new Date());
    }

    /*private long CalDaysDiff(String d1, String d2)
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

            long diff = (date1.getTime() - date2.getTime()) / 86400000;
            return diff;

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }

    }*/

    private void handlePurchase(Purchase purchase)
    {
        Log.d(TAG, "handlePurchase: called");
        if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED || purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE)
        {
            Log.d(TAG, "handlePurchase: "+ purchase.getPurchaseState());

            if(!purchase.isAcknowledged())
            {
                Log.d(TAG, "handlePurchase: acknowledging now");
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }
            else
            {
                Log.d(TAG, "handlePurchase: already acknowledged");
            }
        }
    }


    public int startBillingFlow(SkuDetails _sku_selected)
    {
        // An activity reference from which the billing flow will be launched.
        Activity activity = this;

// Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(_sku_selected)
                .build();
        int responseCode = billingClient.launchBillingFlow(activity, billingFlowParams).getResponseCode();
        Log.d(TAG, "startBillingFlow: repose code from billing: "+ responseCode);



// Handle the result.
        return responseCode;
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavClicked = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {

            fragment = null;

            switch (item.getItemId())
            {
                case R.id.btm_menu_Feed:
                    fragment = feedFragment;
                    break;

                case R.id.btm_menu_Coach:
                    fragment = coachFragment;
                    break;

                case R.id.btm_menu_Profile:
                    fragment = profileFragment;
                    break;
                case R.id.btm_menu_Courses:
                    fragment = coursesFragment;


            }

            getSupportFragmentManager().beginTransaction().addToBackStack("prev frag").replace(R.id.fl_fragmentContainer, fragment).commit();

            return true;
        }
    };


    public void ShowItemInfo(CourseItem _courseItem)
    {
        selectedCourseItem = _courseItem;

        /*Uri uri = Uri.parse(selectedCourseItem.courseImageUrl);
        Log.d(TAG, "ShowItemInfo: uri: "+ uri);*/
        Picasso.get().load((selectedCourseItem.courseImageUrl)).placeholder(R.drawable.ic_coach).into(iv_ItemImage);
        tv_itemLength.setText(String.valueOf(selectedCourseItem.courseDuration));
        tv_ItemName.setText(selectedCourseItem.courseTitle);
        tv_itemContains1.setText(selectedCourseItem.contains1);
        tv_itemContains2.setText(selectedCourseItem.contains2);
        tv_itemRequirement.setText(selectedCourseItem.whatToKnow.replace(".", "\n"));


        rl_ItemInfoPage.setVisibility(View.VISIBLE);
        rl_ItemInfoPage.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_up_anim));
        bottomNav.setVisibility(View.GONE);
        is_InfoPageOpen = true;
    }

    public void ShowSubsPage()
    {
        rl_SubsInfoPage.setVisibility(View.VISIBLE);
        rl_SubsInfoPage.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_up_anim));
        bottomNav.setVisibility(View.GONE);

    }

    private void Btn_closeItemInfoPage()
    {
        btn_closeItemInfoPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rl_ItemInfoPage.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_down_anim));
                rl_ItemInfoPage.setVisibility(View.GONE);
                bottomNav.setVisibility(View.VISIBLE);
                is_InfoPageOpen = false;
            }
        });
    }

    public void Btn_closeSubsPage()
    {
        rl_SubsInfoPage.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load_down_anim));
        rl_SubsInfoPage.setVisibility(View.GONE);

        if(!is_InfoPageOpen) bottomNav.setVisibility(View.VISIBLE);

    }

    public String getmUID() {
        return mUID;
    }

    public String getmUsername() {
        return mUsername;
    }

    public String getmProfilePicUrl() {
        return mProfilePicUrl;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public String getmHeight() {
        return mHeight;
    }

    public void setmHeight(String mHeight) {
        this.mHeight = mHeight;
    }

    public String getmWeight() {
        return mWeight;
    }

    public void setmWeight(String mWeight) {
        this.mWeight = mWeight;
    }
}
