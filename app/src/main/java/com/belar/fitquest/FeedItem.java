package com.belar.fitquest;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class FeedItem
{

    String posterProfilePicUrl, posterProfilename, postImgeUrl, postText, postAge;
    int nLikes, nComments, mID;

    public FeedItem(String posterProfilePicUrl, String posterProfilename, String postImgeUrl, String postText, String postAge, int nLikes, int nComments, int ID) {
        this.posterProfilePicUrl = posterProfilePicUrl;
        this.posterProfilename = posterProfilename;
        this.postImgeUrl = postImgeUrl;
        this.postText = postText;
        this.postAge = postAge;
        this.nLikes = nLikes;
        this.nComments = nComments;
        this.mID = ID;
    }

    public FeedItem(HashMap<String, Object> hashMap)
    {
        this.fromMap(hashMap);
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("posterProfilePicUrl", posterProfilePicUrl);
        result.put("posterProfilename", posterProfilename);
        result.put("postImgeUrl", postImgeUrl);
        result.put("postText", postText);
        result.put("postAge", postAge);
        result.put("nLikes", nLikes);
        result.put("nComments", nComments);
        result.put("mID", mID);

        return result;
    }

    public void fromMap(HashMap<String, Object> hashMap)
    {
        this.posterProfilePicUrl = hashMap.get("posterProfilePicUrl").toString();
        this.posterProfilename = hashMap.get("posterProfilename").toString();
        this.postImgeUrl = hashMap.get("postImgeUrl").toString();
        this.postText = hashMap.get("postText").toString();
        this.postAge = hashMap.get("postAge").toString();
        this.nLikes = Integer.parseInt(hashMap.get("nLikes").toString());
        this.nComments = Integer.parseInt(hashMap.get("nComments").toString());
        this.mID = Integer.parseInt(hashMap.get("mID").toString());
    }



    public String getPosterProfilePicUrl() {
        return posterProfilePicUrl;
    }

    public void setPosterProfilePicUrl(String posterProfilePicUrl) {
        this.posterProfilePicUrl = posterProfilePicUrl;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getPosterProfilename() {
        return posterProfilename;
    }

    public void setPosterProfilename(String posterProfilename) {
        this.posterProfilename = posterProfilename;
    }

    public String getPostImgeUrl() {
        return postImgeUrl;
    }

    public void setPostImgeUrl(String postImgeUrl) {
        this.postImgeUrl = postImgeUrl;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostAge() {
        return postAge;
    }

    public void setPostAge(String postAge) {
        this.postAge = postAge;
    }

    public int getnLikes() {
        return nLikes;
    }

    public void setnLikes(int nLikes) {
        this.nLikes = nLikes;
    }

    public int getnComments() {
        return nComments;
    }

    public void setnComments(int nComments) {
        this.nComments = nComments;
    }
}
