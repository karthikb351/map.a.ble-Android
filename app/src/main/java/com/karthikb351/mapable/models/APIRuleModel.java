package com.karthikb351.mapable.models;

import java.util.List;

/**
 * Created by karthikbalakrishnan on 04/05/15.
 */
public class APIRuleModel {

    List<APISimpleRule> ruleList;
    String actionId;

    public List<APISimpleRule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<APISimpleRule> ruleList) {
        this.ruleList = ruleList;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
}
