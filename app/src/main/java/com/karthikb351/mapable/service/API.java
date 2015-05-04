package com.karthikb351.mapable.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karthikb351.mapable.models.APIRuleModel;
import com.karthikb351.mapable.models.APISimpleRule;
import com.karthikb351.mapable.models.ActionModel;
import com.karthikb351.mapable.models.BeaconModel;
import com.karthikb351.mapable.models.DistanceBucket;
import com.karthikb351.mapable.models.RuleModel;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.altbeacon.beacon.Beacon;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by karthikbalakrishnan on 04/05/15.
 */
public class API {

    private final static String LOG_TAG = "API";

    private final static String BASE_URL = "https://mapable-dev.appspot.com";

    private final static OkHttpClient client = new OkHttpClient();

    private final static String DUMP_ENDPOINT = BASE_URL + "/api/all";

    private static Context ctx;

    public API(Context applicationContext) {
        this.ctx = applicationContext;
    }

    public static void refreshData() {
        Request request = new Request.Builder()
                .url(DUMP_ENDPOINT)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, final IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO on failure
                    }
                });
            }

            @Override
            public void onResponse(final Response response) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response.toString());
                    List<ActionModel> actions = Arrays.asList(gson.fromJson(obj.optString("actions", "[]"), ActionModel[].class));
                    List<BeaconModel> beacons = Arrays.asList(gson.fromJson(obj.optString("actions", "[]"), BeaconModel[].class));
                    List<APIRuleModel> rules = Arrays.asList(gson.fromJson(obj.optString("actions", "[]"), APIRuleModel[].class));

                    ActionModel.deleteAll(ActionModel.class);
                    BeaconModel.deleteAll(BeaconModel.class);
                    RuleModel.deleteAll(RuleModel.class);

                    for(ActionModel a:actions)
                        a.save();
                    for(BeaconModel b:beacons)
                        b.save();

                    for(APIRuleModel r: rules) {
                        RuleModel rule = new RuleModel();

                        rule.setAction(ActionModel.find(ActionModel.class, "id = ?", r.getActionId()).get(0));

                        rule.setPriority(r.getPriority());

                        List<BeaconModel> beaconModels = new ArrayList<BeaconModel>();
                        List<DistanceBucket> distanceBuckets = new ArrayList<DistanceBucket>();
                        for(APISimpleRule sr:r.getRuleList()) {
                            beaconModels.add(BeaconModel.find(BeaconModel.class,"id = ?", sr.getBeaconUuid()).get(0));
                            switch (sr.getDistanceBucket()){
                                case 0:
                                    distanceBuckets.add(DistanceBucket.NEXT_TO);
                                    break;
                                case 1:
                                    distanceBuckets.add(DistanceBucket.NEAR);
                                    break;
                                case 2:
                                    distanceBuckets.add(DistanceBucket.FAR);
                                    break;
                            }
                        }
                        rule.setBeaconList(beaconModels);
                        rule.setDistanceBuckets(distanceBuckets);

                        rule.save();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO on success
                    }
                });

            }
        });
    }
}
