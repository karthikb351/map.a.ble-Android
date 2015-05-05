package com.karthikb351.mapable.bus.events;

import com.karthikb351.mapable.models.RuleModel;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

/**
 * Created by Nikhil on 5/4/15.
 */
public class BeaconsSatisfiedRules {

    ArrayList<RuleModel> mRuleList;

    public ArrayList<RuleModel> getRuleList() {
        return mRuleList;
    }

    public void setRuleList(ArrayList<RuleModel> mRuleList) {
        this.mRuleList = mRuleList;
    }


    public BeaconsSatisfiedRules(ArrayList<RuleModel> ruleList) {
        mRuleList = ruleList;
    }
}
