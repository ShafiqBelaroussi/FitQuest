package com.belar.fitquest;

public class WorkoutItem
{
    String mWorkoutKey, mWorkoutValue;

    public WorkoutItem (String workoutKey, String workoutValue)
    {
        this.mWorkoutKey = workoutKey;
        this.mWorkoutValue = workoutValue;
    }

    public String getmWorkoutKey() {
        return mWorkoutKey;
    }

    public void setmWorkoutKey(String mWorkoutKey) {
        this.mWorkoutKey = mWorkoutKey;
    }

    public String getmWorkoutValue() {
        return mWorkoutValue;
    }

    public void setmWorkoutValue(String mWorkoutValue) {
        this.mWorkoutValue = mWorkoutValue;
    }
}
