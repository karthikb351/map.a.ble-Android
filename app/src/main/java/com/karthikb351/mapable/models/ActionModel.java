package com.karthikb351.mapable.models;

import com.orm.SugarRecord;

/**
 * Created by karthikbalakrishnan on 04/05/15.
 */
public class ActionModel extends SugarRecord<ActionModel> {

    String description;
    // 0 - notification and payload is URL
    // 1 - notification and payload is text
    // 2 - contextual data and payload is text

    int type;
    String payload;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
