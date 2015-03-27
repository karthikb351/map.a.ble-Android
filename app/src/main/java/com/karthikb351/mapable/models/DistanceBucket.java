package com.karthikb351.mapable.models;

/**
 * Created by Nikhil on 3/27/15.
 */
public enum DistanceBucket {
    NEXT_TO,
    NEAR,
    FAR;

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
