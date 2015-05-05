package com.karthikb351.mapable.models;

import com.orm.SugarRecord;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karthikbalakrishnan on 04/05/15.
 */
public class RuleModel extends SugarRecord<RuleModel> {

    String ruleId;
    ActionModel mAction;
    int mPriority;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public ActionModel getAction() {
        return mAction;
    }

    public void setAction(ActionModel action) {
        this.mAction = action;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public boolean isRuleSatisfied(List<Beacon> beaconList) {
        boolean isRuleSatisfied = true;
        List<SimpleRuleModel> simpleRuleModels = SimpleRuleModel.find(SimpleRuleModel.class, "rule_id = ?", this.ruleId);

        // Rewrite to use SimpleRuleModel

//        for (int i = 0; i < mBeaconList.size(); i++) {
//            BeaconModel beaconModel = mBeaconList.get(i);
//            DistanceBucket distanceBucket = mDistanceBucketList.get(i);
//            boolean isBeaconPresent = false;
//            for (Beacon beacon : beaconList) {
//                if (beacon.getBluetoothAddress().equals(beaconModel.getUuid()) && distanceBucket.equals(DistanceBucket.getDistanceBucketForDistance(beacon.getDistance()))) {
//                    isBeaconPresent = true;
//                    break;
//                }
//            }
//            if (!isBeaconPresent) {
//                isRuleSatisfied = false;
//                break;
//            }
//        }

        return isRuleSatisfied;
    }
}
