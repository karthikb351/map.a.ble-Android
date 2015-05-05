package com.karthikb351.mapable.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karthikb351.mapable.adapters.BeaconListAdapter;
import com.karthikb351.mapable.R;
import com.karthikb351.mapable.adapters.MainActivityPagerAdapter;
import com.karthikb351.mapable.bus.BusProvider;
import com.karthikb351.mapable.bus.events.BeaconServiceState;
import com.karthikb351.mapable.bus.events.BeaconsFoundInRange;
import com.karthikb351.mapable.models.APIRuleModel;
import com.karthikb351.mapable.models.APISimpleRule;
import com.karthikb351.mapable.models.ActionModel;
import com.karthikb351.mapable.models.BeaconModel;
import com.karthikb351.mapable.models.DistanceBucket;
import com.karthikb351.mapable.models.RuleModel;
import com.karthikb351.mapable.models.SimpleRuleModel;
import com.karthikb351.mapable.service.BeaconService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.altbeacon.beacon.Beacon;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    TextView mBluetoothDisabledText;
    private Bus mBus = BusProvider.getInstance();
    ViewPager mViewPager;
    MainActivityPagerAdapter mainActivityPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.vp_tab);
        mainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mainActivityPagerAdapter);
        tabStrip.setShouldExpand(true);
        tabStrip.setViewPager(mViewPager);
        mBluetoothDisabledText = (TextView) findViewById(R.id.bluetooth_disabled_text);
        checkBluetoothStatus();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mBus.register(this);
        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBus.register(this);
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem toggleItem = menu.findItem(R.id.action_toggle_service);
        if (BeaconService.isInstanceCreated()) {
            toggleItem.setTitle("Stop Service");
        } else {
            toggleItem.setTitle("Start Service");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_toggle_service) {
            if (BeaconService.isInstanceCreated()) {
                stopBeaconService();
            } else {
                startBeaconService();
            }
            invalidateOptionsMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int i = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                        == BluetoothAdapter.STATE_OFF) {
                    handleBluetoothDisabled();
                } else if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                        == BluetoothAdapter.STATE_ON) {
                    mViewPager.setVisibility(View.VISIBLE);
                    mBluetoothDisabledText.setVisibility(View.GONE);
                }
                // Bluetooth is disconnected, do handling here
            }

        }

    };

    private void checkBluetoothStatus() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            handleBluetoothDisabled();
        }
        else if (mBluetoothAdapter == null && mBluetoothAdapter.isEnabled()) {
            mViewPager.setVisibility(View.VISIBLE);
            mBluetoothDisabledText.setVisibility(View.GONE);
        }
    }

    private void handleBluetoothDisabled() {
        stopBeaconRanging();
        mViewPager.setVisibility(View.GONE);
        mBluetoothDisabledText.setVisibility(View.VISIBLE);
        mBluetoothDisabledText.setText("Bluetooth is currently disabled. Please turn it on to start ranging");
    }

    private void startBeaconRanging() {
        mBus.post(new BeaconServiceState(true));
    }

    private void startBeaconService() {
        startService(new Intent(this, BeaconService.class));
    }

    private void stopBeaconService() {
        stopService(new Intent(this, BeaconService.class));
    }

    private void stopBeaconRanging() {
        mBus.post(new BeaconServiceState(false));
    }

    private class RefreshDataTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... urls) {

            try {
                HttpClient client = new DefaultHttpClient();
                URI website = new URI("https://mapable-dev.appspot.com/api/sample");
                HttpGet request = new HttpGet();
                request.setURI(website);
                HttpResponse response = client.execute(request);

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response.toString());
                    List<ActionModel> actions = Arrays.asList(gson.fromJson(obj.optString("actions", "[]"), ActionModel[].class));
                    List<BeaconModel> beacons = Arrays.asList(gson.fromJson(obj.optString("beacons", "[]"), BeaconModel[].class));
                    List<APIRuleModel> rules = Arrays.asList(gson.fromJson(obj.optString("rules", "[]"), APIRuleModel[].class));

                    ActionModel.deleteAll(ActionModel.class);
                    BeaconModel.deleteAll(BeaconModel.class);
                    RuleModel.deleteAll(RuleModel.class);

                    for(ActionModel a:actions)
                        a.save();
                    for(BeaconModel b:beacons)
                        b.save();

                    for(APIRuleModel r: rules) {
                        RuleModel rule = new RuleModel();

                        rule.setAction(ActionModel.find(ActionModel.class, "action_id = ?", String.valueOf(r.getActionId())).get(0));

                        rule.setPriority(r.getPriority());
                        for(APISimpleRule sr:r.getRuleList()) {
                            SimpleRuleModel simpleRule = new SimpleRuleModel();
                            simpleRule.setBeacon(BeaconModel.find(BeaconModel.class,"uuid = ?", sr.getBeaconUuid()).get(0));
                            simpleRule.setDistanceBucket(sr.getDistanceBucket());
                            simpleRule.save();
                        }

                        rule.save();

                    }


                    return "Success";

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error";
                }
            } finally {
                return "Error";
            }

        }

        protected void onPostExecute(String result) {
            if(result.equals("Success")) {

            }
            else {

            }

        }
    }

}
