package com.karthikb351.mapable.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by karthikbalakrishnan on 04/05/15.
 */
public class APIRuleModel {

    @SerializedName("rule_id")
    int ruleId;
    @SerializedName("rules")
    List<APISimpleRule> ruleList;
    @SerializedName("action_id")
    int actionId;
    @SerializedName("priority")
    int priority;

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public List<APISimpleRule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<APISimpleRule> ruleList) {
        this.ruleList = ruleList;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
