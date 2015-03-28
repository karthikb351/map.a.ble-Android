package com.karthikb351.mapable.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.karthikb351.mapable.adapters.BeaconListAdapter;
import com.karthikb351.mapable.R;
import com.karthikb351.mapable.bus.BusProvider;
import com.karthikb351.mapable.bus.events.BeaconServiceState;
import com.karthikb351.mapable.bus.events.BeaconsFoundInRange;
import com.karthikb351.mapable.service.BeaconService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    List<Beacon> mBeaconList = new ArrayList<>();
    BeaconListAdapter mAdapter;
    RecyclerView mBeaconListView;
    TextView mBluetoothDisabledText;
    private Bus mBus = BusProvider.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBeaconListView = (RecyclerView) findViewById(R.id.beacons_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mBeaconListView.setLayoutManager(llm);

        mBluetoothDisabledText = (TextView) findViewById(R.id.bluetooth_disabled_text);
        mAdapter = new BeaconListAdapter(mBeaconList);
        mBeaconListView.setAdapter(mAdapter);
        checkBluetoothStatus();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mBus.register(this);
        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        startBeaconService();
    }

   @Override
    protected void onStop() {
        super.onStop();
        stopBeaconService();
        unregisterReceiver(mReceiver);
        mBus.unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }


    @Subscribe
    public void onBeaconFoundInRange(BeaconsFoundInRange b) {
        mBeaconList = b.getBeacons();
        //After  getting the beacons, populate them in the list.
        mAdapter.setBeacons(mBeaconList);
        mAdapter.notifyDataSetChanged();
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
                    mBeaconListView.setVisibility(View.VISIBLE);
                    mBluetoothDisabledText.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
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
    }

    private void handleBluetoothDisabled() {
        stopBeaconRanging();
        mBluetoothDisabledText.setVisibility(View.VISIBLE);
        mBluetoothDisabledText.setText("Bluetooth is currently disabled. PLease turn it on to start ranging");
        mBeaconListView.setVisibility(View.GONE);
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

}
