package com.karthikb351.mapable.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by karthikbalakrishnan on 04/05/15.
 */
public class APISimpleRule {

    @SerializedName("uuid")
    String beaconUuid;
    @SerializedName("distance")
    int distanceBucket;

    public String getBeaconUuid() {
        return beaconUuid;
    }

    public void setBeaconUuid(String beaconUuid) {
        this.beaconUuid = beaconUuid;
    }

    public int getDistanceBucket() {
        return distanceBucket;
    }

    public void setDistanceBucket(int distanceBucket) {
        this.distanceBucket = distanceBucket;
    }
}
