package com.belar.fitquest;

import java.util.ArrayList;

public class firebaseUsers
{
    private String mUID, mUsername, mEmail;
    private String mGender, mGoal1 = "N/A", mGoal2 = "N/A", mGoal3 = "N/A", mFitnessLvl = "0", mDOB = "xx/xx/xxxx", mWeight = "N/A", mHeight = "N/A";
    private ArrayList<String> foodAlergiesList = new ArrayList<String>();
    private ArrayList<CourseItem> courseItems = new ArrayList<CourseItem>();


    public firebaseUsers(String mUID, String mUsername, String mEmail, String mGender, String mGoal1, String mGoal2, String mGoal3, String mFitnessLvl, String mDOB, String mWeight, String mHeight, ArrayList<String> foodAlergiesList, ArrayList<CourseItem> _courseItems) {
        this.mUID = mUID;
        this.mUsername = mUsername;
        this.mEmail = mEmail;
        this.mGender = mGender;
        this.mGoal1 = mGoal1;
        this.mGoal2 = mGoal2;
        this.mGoal3 = mGoal3;
        this.mFitnessLvl = mFitnessLvl;
        this.mDOB = mDOB;
        this.mWeight = mWeight;
        this.mHeight = mHeight;
        this.foodAlergiesList = foodAlergiesList;
        this.courseItems = _courseItems;
    }

    public String getmWeight() {
        return mWeight;
    }

    public void setmWeight(String mWeight) {
        this.mWeight = mWeight;
    }

    public String getmHeight() {
        return mHeight;
    }

    public void setmHeight(String mHeight) {
        this.mHeight = mHeight;
    }

    public ArrayList<CourseItem> getCourseItems() {
        return courseItems;
    }

    public void setCourseItems(ArrayList<CourseItem> courseItems) {
        this.courseItems = courseItems;
    }

    public String getmDOB() {
        return mDOB;
    }

    public void setmDOB(String mDOB) {
        this.mDOB = mDOB;
    }

    public String getmUID() {
        return mUID;
    }

    public void setmUID(String mUID) {
        this.mUID = mUID;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public String getmGoal1() {
        return mGoal1;
    }

    public void setmGoal1(String mGoal1) {
        this.mGoal1 = mGoal1;
    }

    public String getmGoal2() {
        return mGoal2;
    }

    public void setmGoal2(String mGoal2) {
        this.mGoal2 = mGoal2;
    }

    public String getmGoal3() {
        return mGoal3;
    }

    public void setmGoal3(String mGoal3) {
        this.mGoal3 = mGoal3;
    }

    public String getmFitnessLvl() {
        return mFitnessLvl;
    }

    public void setmFitnessLvl(String mFitnessLvl) {
        this.mFitnessLvl = mFitnessLvl;
    }

    public ArrayList<String> getFoodAlergiesList() {
        return foodAlergiesList;
    }

    public void setFoodAlergiesList(ArrayList<String> foodAlergiesList) {
        this.foodAlergiesList = foodAlergiesList;
    }
}




