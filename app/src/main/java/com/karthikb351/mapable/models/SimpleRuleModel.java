package com.karthikb351.mapable.models;

import com.orm.SugarRecord;

/**
 * Created by karthikbalakrishnan on 05/05/15.
 */
public class SimpleRuleModel extends SugarRecord<SimpleRuleModel>{

    String ruleId;
    BeaconModel beacon;
    int distanceBucket;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public BeaconModel getBeacon() {
        return beacon;
    }

    public void setBeacon(BeaconModel beacon) {
        this.beacon = beacon;
    }

    public int getDistanceBucket() {
        return distanceBucket;
    }

    public void setDistanceBucket(int distanceBucket) {
        this.distanceBucket = distanceBucket;
    }
}
