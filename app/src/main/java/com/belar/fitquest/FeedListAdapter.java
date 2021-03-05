package com.belar.fitquest;

import android.content.Context;
import android.text.Html;
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

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class FeedListAdapter  extends ArrayAdapter<FeedItem>
{
    private Context mContext;
    int mResources;
    DatabaseReference mFeeds;
    FeedItem feedItem;
    int nPosts;


    public FeedListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FeedItem> objects, DatabaseReference mFeeds, int nPosts) {
        super(context, resource, objects);

        mContext = context;
        mResources = resource;
        this.mFeeds = mFeeds;
        this.nPosts = nPosts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        String posterProfilePicUrl, posterProfilename, postImgeUrl, postText, postAge;
        int nLikes, nComments;

        posterProfilePicUrl = getItem(position).posterProfilePicUrl;
        posterProfilename = getItem(position).posterProfilename;
        postImgeUrl = getItem(position).getPostImgeUrl();
        postText = getItem(position).postText;
        postAge = getItem(position).postAge;
        nLikes = getItem(position).nLikes;
        nComments = getItem(position).nComments;


        feedItem = new FeedItem(posterProfilePicUrl, posterProfilename, postImgeUrl, postText, postAge, nLikes, nComments, nPosts);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResources, parent, false);

        ImageView iv_PosterProfilePic = convertView.findViewById(R.id.iv_PosterProfilePic);
        TextView tv_posterID = convertView.findViewById(R.id.tv_posterID);
        ImageView iv_PostImage = convertView.findViewById(R.id.iv_PostImage);
        TextView tv_posterTxt = convertView.findViewById(R.id.tv_posterTxt);
        TextView tv_postAge = convertView.findViewById(R.id.tv_postAge);
        TextView tv_likes = convertView.findViewById(R.id.tv_likes);
        TextView tv_comments = convertView.findViewById(R.id.tv_comments);
        RelativeLayout rl_likes = convertView.findViewById(R.id.rl_likes);


        tv_posterID.setText(feedItem.getPosterProfilename());
        Picasso.get().load(posterProfilePicUrl).placeholder(R.drawable.ic_profile).into(iv_PosterProfilePic);

        if(postImgeUrl.equals("null")) iv_PostImage.setVisibility(View.GONE);
        else Picasso.get().load(feedItem.postImgeUrl).into(iv_PostImage);

        String sourceString = "<b>" + feedItem.getPosterProfilename() + ": " + "</b> " + feedItem.postText;

        tv_posterTxt.setText(Html.fromHtml(sourceString, 0));
        tv_likes.setText(String.valueOf(feedItem.getnLikes()));
        tv_comments.setText(String.valueOf(feedItem.getnComments()));

        rl_likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFeeds.child(Integer.toString((nPosts-position))).child("nLikes").setValue(feedItem.nLikes+1);
            }
        });

        return convertView;
    }

    @Nullable
    @Override
    public FeedItem getItem(int position) {
        return super.getItem(position);
    }
}
