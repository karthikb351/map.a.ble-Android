package com.karthikb351.mapable.models;

import java.util.List;

/**
 * Created by karthikbalakrishnan on 04/05/15.
 */
public class RuleModel {

    List<BeaconModel> beaconList;
    List<DistanceBucket> distanceBuckets;
    ActionModel action;

    public List<BeaconModel> getBeaconList() {
        return beaconList;
    }

    public void setBeaconList(List<BeaconModel> beaconList) {
        this.beaconList = beaconList;
    }

    public List<DistanceBucket> getDistanceBuckets() {
        return distanceBuckets;
    }

    public void setDistanceBuckets(List<DistanceBucket> distanceBuckets) {
        this.distanceBuckets = distanceBuckets;
    }

    public ActionModel getAction() {
        return action;
    }

    public void setAction(ActionModel action) {
        this.action = action;
    }
}
