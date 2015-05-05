package com.karthikb351.mapable;

import android.content.Context;

import com.karthikb351.mapable.models.BeaconModel;
import com.karthikb351.mapable.models.RuleModel;

/**
 * Created by Nikhil on 5/5/15.
 */
public class AppDataManager {

    private static AppDataManager INSTANCE;
    private Context mContext;

    AppDataManager(Context context) {
        mContext = context;
    }

    public static AppDataManager getInstance(Context context) {
        if (INSTANCE == null) {
            return new AppDataManager(context);
        } else {
            return INSTANCE;
        }
    }

    private void loadInitialData () {
        if (RuleModel.listAll(RuleModel.class).size()==0) {
            BeaconModel b1 = new BeaconModel();
            b1.setDescription("1");
            b1.setNickname("1");
            b1.setUuid("blaa");
            b1.save();
        }
    }


}
