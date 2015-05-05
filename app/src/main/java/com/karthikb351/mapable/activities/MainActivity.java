package com.karthikb351.mapable.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.karthikb351.mapable.adapters.BeaconListAdapter;
import com.karthikb351.mapable.R;
import com.karthikb351.mapable.adapters.MainActivityPagerAdapter;
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

    TextView mBluetoothDisabledText;
    private Bus mBus = BusProvider.getInstance();
    ViewPager mViewPager;
    MainActivityPagerAdapter mainActivityPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
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
                    mainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
                    mViewPager.setAdapter(mainActivityPagerAdapter);
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

}
