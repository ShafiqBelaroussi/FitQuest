package com.belar.fitquest;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/*class DailyContent
{
    String dayNumber;
    int isRest, isCurrent;
    //ArrayList<WorkoutItem> workoutItems;


*//*    public DailyContent(HashMap<String, Object> hashMap)
    {
        this.fromMap(hashMap);
    }


    void fromMap(HashMap<String, Object> hashMap)
    {
        this.dayNumber = hashMap.get("dayNumber").toString();
        this.isRest = (int) hashMap.get("isRest") != 0;
        //this.isCurrent ()
        //this.workoutItems = (ArrayList<WorkoutItem>) hashMap.get("workoutItems");
    }*//*
    public DailyContent (int _dayN, boolean _isRest, boolean _isCurrent)
    {
        this.dayNumber = "day "+ _dayN;
        this.isRest = _isRest? 1:0;
        this.isCurrent = _isCurrent? 1:0;

    }
}*/

public class CourseItem
{
    String courseHost, courseImageUrl, courseTitle, contains1, contains2, whatToKnow, dateAquired;
    int courseDuration, currentDay;

    HashMap<String, HashMap<String , HashMap<String , String>>> content;
    /*content (String key):
*           day1 (String key):
*               item1 (String key) : xyz xyz (String value)
*               item2: xyz xyz
*           day2:
*               item1: xyz xyz
*               item2: xyz xyz*/


   // HashMap<String, HashMap<String, Object>> WorkoutItems;


    public CourseItem(String _courseHost, String _courseImage, int _courseDuration, int _currentDay, String _courseTitle, String _contains1, String _getContains2, String _whatToKnow, String _dateAquired, HashMap<String, HashMap<String , HashMap<String , String>>> _dailyContents)
    {
        this.courseHost = _courseHost;
        this.courseImageUrl = _courseImage;
        this.courseDuration = _courseDuration;
        this.courseTitle = _courseTitle;
        this.contains1 = _contains1;
        this.contains2 = _getContains2;
        this.whatToKnow = _whatToKnow;
        this.dateAquired = _dateAquired;
        this.currentDay = _currentDay;
        this.content = _dailyContents;

    }

    public CourseItem(String _courseHost, String _courseImage, int _courseDuration, int _currentDay, String _courseTitle, String _contains1, String _getContains2, String _whatToKnow, String _dateAquired)
    {
        this.courseHost = _courseHost;
        this.courseImageUrl = _courseImage;
        this.courseDuration = _courseDuration;
        this.courseTitle = _courseTitle;
        this.contains1 = _contains1;
        this.contains2 = _getContains2;
        this.whatToKnow = _whatToKnow;
        this.dateAquired = _dateAquired;
        this.currentDay = _currentDay;
    }




    public CourseItem (HashMap<String, Object> hashMap)
    {
        this.fromMap(hashMap);
    }


    public void fromMap(HashMap<String, Object> hashMap)
    {
        this.courseHost = hashMap.get("courseHost").toString();
        this.courseImageUrl = hashMap.get("courseImageUrl").toString();
        this.courseTitle = hashMap.get("courseTitle").toString();
        this.contains1 = hashMap.get("contains1").toString();
        this.contains2 = hashMap.get("contains2").toString();
        this.whatToKnow = hashMap.get("whatToKnow").toString();
        this.dateAquired = hashMap.get("dateAquired").toString();
        this.courseDuration = Integer.parseInt(hashMap.get("courseDuration").toString());
        this.currentDay = Integer.parseInt(hashMap.get("currentDay").toString());
        this.content = (HashMap<String, HashMap<String, HashMap<String, String>>>) hashMap.get("content");
        Log.d(TAG, "line 108 CourseItem: "+ this.content);

        /*training_nutrients (String key):
         *           day1 (String key):
         *               item1 (String key) : xyz xyz (String value)
         *               item2: xyz xyz
         *           day2:
         *               item1: xyz xyz
         *               item2: xyz xyz*/
    }


    public HashMap<String, HashMap<String, HashMap<String, String>>> getContent() {
        return content;
    }

    public void setContent(HashMap<String, HashMap<String, HashMap<String, String>>> content) {
        this.content = content;
    }

    public String getDateAquired() {
        return dateAquired;
    }

    public void setDateAquired(String dateAquired) {
        this.dateAquired = dateAquired;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public String getCourseHost() {
        return courseHost;
    }

    public void setCourseHost(String courseHost) {
        this.courseHost = courseHost;
    }

    public String getCourseImageUrl() {
        return courseImageUrl;
    }

    public void setCourseImageUrl(String courseImageUrl) {
        this.courseImageUrl = courseImageUrl;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getContains1() {
        return contains1;
    }

    public void setContains1(String contains1) {
        this.contains1 = contains1;
    }

    public String getContains2() {
        return contains2;
    }

    public void setContains2(String contains2) {
        this.contains2 = contains2;
    }

    public String getWhatToKnow() {
        return whatToKnow;
    }

    public void setWhatToKnow(String whatToKnow) {
        this.whatToKnow = whatToKnow;
    }

    public int getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(int courseDuration) {
        this.courseDuration = courseDuration;
    }
}
