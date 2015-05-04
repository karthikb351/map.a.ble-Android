package com.karthikb351.mapable.models;

import com.orm.SugarRecord;

/**
 * Created by karthikbalakrishnan on 04/05/15.
 */
public class BeaconModel extends SugarRecord<BeaconModel> {

    String nickname;
    String uuid;
    String description;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
