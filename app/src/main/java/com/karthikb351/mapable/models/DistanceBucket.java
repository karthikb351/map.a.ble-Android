package com.karthikb351.mapable.models;

import android.graphics.Color;

/**
 * Created by Nikhil on 3/27/15.
 */
public enum DistanceBucket {
    NEXT_TO(Color.BLUE),
    NEAR(Color.LTGRAY),
    FAR(Color.DKGRAY);

    int mColor;

    DistanceBucket(int color){
        this.mColor = color;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public static DistanceBucket getDistanceBucketForDistance(double distanceFromBeaconInMeters){
        if(distanceFromBeaconInMeters<0.0){
            return null;
        }
        if(distanceFromBeaconInMeters>0.0&&distanceFromBeaconInMeters<1.0){
            return DistanceBucket.NEXT_TO;
        }
        else if(distanceFromBeaconInMeters>=1.0&&distanceFromBeaconInMeters<3.0){
            return DistanceBucket.NEAR;
        }
        else
            return DistanceBucket.FAR;
    }
}
