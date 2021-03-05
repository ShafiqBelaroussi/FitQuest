package com.belar.fitquest;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.INPUT_METHOD_SERVICE;


public class FeedFragment extends Fragment
{

    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_INTENT_REQ_CODE = 102;
    FirebaseDatabase mRoot = FirebaseDatabase.getInstance();
    DatabaseReference mFeeds = mRoot.getReference("feeds");
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ArrayList<FeedItem> feedItems = new ArrayList<FeedItem>();
    FeedListAdapter feedListAdapter;

    Button btn_AddPost, btn_SharePoste, btn_closeNewFeedPage;
    EditText et_WhatsInYourMind;
    ListView lv_FeedList;
    RelativeLayout rl_AddPost;
    private int nPosts;
    private boolean mFeed_DATACHANGE_FIRSTCALL = true;

    ImageView iv_PostImage, iv_PosterProfilePic;
    Uri captureUri;
    String imageFilePath;
    private String currentPhotoPath;
    private Uri imageUri;
    private boolean image_with_post = false;


    public FeedFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        btn_AddPost = view.findViewById(R.id.btn_AddPost);
        btn_SharePoste = view.findViewById(R.id.btn_SharePoste);
        btn_closeNewFeedPage = view.findViewById(R.id.btn_closeNewFeedPage);
        et_WhatsInYourMind = view.findViewById(R.id.et_WhatsInYourMind);
        lv_FeedList = view.findViewById(R.id.lv_FeedList);
        rl_AddPost = view.findViewById(R.id.rl_AddPost);
        iv_PostImage = view.findViewById(R.id.iv_PostImage);
        iv_PosterProfilePic = view.findViewById(R.id.iv_PosterProfilePic);

        Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).placeholder(R.drawable.ic_profile).fit().into(iv_PosterProfilePic);



        btn_closeNewFeedPageClicked();
        btn_postFeedClicked();

        hideSoftKeyboardOnOutsideClick(view);

        et_WhatsInYourMind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() >0)
                {
                    btn_SharePoste.setBackgroundColor(getResources().getColor(R.color.SecondaryBlue));
                    btn_SharePoste.setClickable(true);
                }
                else
                {
                    if(!image_with_post)
                    {
                        btn_SharePoste.setBackgroundColor(getResources().getColor(R.color.ItemGray));
                        btn_SharePoste.setClickable(false);
                    }
                }
            }
        });

        btn_SharePoste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //posting new post
                btn_SharePoste.setClickable(false);
                hideSoftKeyboard(getActivity());

                if(image_with_post)
                {
                    StorageReference filePath = storageReference.child("posts").child(imageUri.getLastPathSegment());
                    filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Log.d(TAG, "line 177: "+ uri.toString());

                                    FeedItem feedItem = new FeedItem(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), uri.toString(), et_WhatsInYourMind.getText().toString().trim(), "postAge", 0, 0,nPosts+1);

                                    mFeeds.child(String.valueOf(nPosts +1)).setValue(feedItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                mFeeds.child("nFeed").setValue(nPosts+=1);
                                                Toast.makeText(getContext(), "Posted successfully", Toast.LENGTH_SHORT).show();
                                                rl_AddPost.setClickable(true);
                                                et_WhatsInYourMind.setText("");
                                                rl_AddPost.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.load_down_anim));
                                                rl_AddPost.setVisibility(View.GONE);

                                                // resetting addPost Page
                                                iv_PostImage.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_camera));
                                                image_with_post = false;
                                            }
                                        }
                                    });
                                }
                            });

                        }
                    });
                }
                else
                {
                    FeedItem feedItem = new FeedItem(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), "null", et_WhatsInYourMind.getText().toString().trim(), "postAge", 0, 0,nPosts+1);

                    mFeeds.child(String.valueOf(nPosts +1)).setValue(feedItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                mFeeds.child("nFeed").setValue(nPosts+=1);
                                Toast.makeText(getContext(), "Posted successfully", Toast.LENGTH_SHORT).show();
                                rl_AddPost.setClickable(true);
                                et_WhatsInYourMind.setText("");
                                rl_AddPost.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.load_down_anim));
                                rl_AddPost.setVisibility(View.GONE);

                            }
                        }
                    });
                }



            }
        });


        mFeeds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                int temp_nPost = Integer.parseInt(snapshot.child("nFeed").getValue().toString());
                boolean newPosts = false;
                if(nPosts <= temp_nPost) {
                    nPosts = temp_nPost;
                    //Toast.makeText(getContext(), "new posts", Toast.LENGTH_SHORT).show();
                    newPosts = true;
                    feedItems.clear();
                }
                Iterable<DataSnapshot> children = snapshot.getChildren();
                if (snapshot.getChildrenCount() > 0) {

                    for (DataSnapshot snap: children)
                    {
                        if(!Objects.equals(snap.getKey(), "nFeed"))
                        {

                            if(newPosts) {
                                feedItems.add(0, new FeedItem((HashMap<String, Object>) snap.getValue()));
                                lv_FeedList.invalidateViews();
                                continue;
                            }

                            feedItems.clear();
                            for (DataSnapshot snap2: children)
                            {
                                if(!Objects.equals(snap2.getKey(), "nFeed"))
                                {
                                    feedItems.add(0, new FeedItem((HashMap<String, Object>) snap2.getValue()));
                                    lv_FeedList.invalidateViews();
                                }
                            }
                            break;
                        }
                    }

                }

                feedListAdapter = new FeedListAdapter(getContext(), R.layout.feed_item, feedItems, mFeeds, nPosts);
                lv_FeedList.setAdapter(feedListAdapter);

                if(nPosts > 0)
                {
                    btn_closeNewFeedPage.setVisibility(View.VISIBLE);
                    rl_AddPost.setVisibility(View.GONE);
                    btn_SharePoste.setClickable(false);
                }
                else
                {
                    btn_closeNewFeedPage.setVisibility(View.GONE);
                    rl_AddPost.setVisibility(View.VISIBLE);
                    btn_SharePoste.setClickable(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        iv_PostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });




        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        Log.d(TAG, "line 234");

        if(requestCode == CAMERA_PERMISSION_CODE)
        {
            Log.d(TAG, "line 238");
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d(TAG, "granted");
                openCamera();
            }
            else
            {
                Log.d(TAG, "line 243: denied");
                Toast.makeText(getContext(), "Camera permission is needed", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_INTENT_REQ_CODE && resultCode == RESULT_OK)
        {
            /*Bitmap image = (Bitmap) data.getExtras().get("data");
            iv_PostImage.setImageBitmap(image);
            uploadImage(image);

            captureUri = (Uri) data.getData();
            Log.d(TAG, "line 276: "+ data.getExtras());
            Log.d(TAG, "line 277: "+ captureUri);*/
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            iv_PostImage.setImageBitmap(bitmap);
            image_with_post = true;

            btn_SharePoste.setClickable(true);
            btn_SharePoste.setBackgroundColor(getResources().getColor(R.color.SecondaryBlue));
        }
        else
        {
            image_with_post = false;

            btn_SharePoste.setClickable(false);
            btn_SharePoste.setBackgroundColor(getResources().getColor(R.color.ItemGray));
        }
    }

    private void uploadImage(Bitmap bitmap) {


    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = new File((Environment.DIRECTORY_PICTURES));
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void askCameraPermission()
    {
        if(ContextCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "line 222");
            Toast.makeText(getContext(), "line 222", Toast.LENGTH_SHORT).show();

            // ask permission in real-time
            ActivityCompat.requestPermissions(this.requireActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        else
        {
            Log.d(TAG, "line 228");
            openCamera();
        }


    }


    private void openCamera() {
        Toast.makeText(getContext(), "Camera Open Reuqest", Toast.LENGTH_SHORT).show();

        String fileName = "photo";
        File storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try
        {
            File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
            currentPhotoPath = imageFile.getAbsolutePath();

            imageUri = FileProvider.getUriForFile(requireActivity(), "com.belar.fitquest.fileprovider", imageFile);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, CAMERA_INTENT_REQ_CODE);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }






    }

    private void hideSoftKeyboardOnOutsideClick(View view) {
        view.findViewById(R.id.rl_AddPost).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try
                {
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                catch (Exception e)
                {
                    return false;
                }
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        try
        {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e)
        {

        }
    }

    private void btn_closeNewFeedPageClicked()
    {
        btn_closeNewFeedPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_AddPost.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.load_down_anim));
                rl_AddPost.setVisibility(View.GONE);

                //Toast.makeText(getContext(), "clicked the close feed post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void btn_postFeedClicked() {

        btn_AddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                rl_AddPost.setVisibility(View.VISIBLE);
                rl_AddPost.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.load_up_anim));
            }
        });
    }


}
