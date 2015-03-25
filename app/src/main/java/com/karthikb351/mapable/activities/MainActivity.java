package com.karthikb351.mapable.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    ListView mBeaconListView;
    TextView mBluetoothDisabledText;
    private Bus mBus = BusProvider.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBeaconListView = (ListView) findViewById(R.id.beacons_list);
        mBluetoothDisabledText = (TextView) findViewById(R.id.bluetooth_disabled_text);
        mAdapter = new BeaconListAdapter();
        mBeaconListView.setAdapter(mAdapter);
        checkBluetoothStatus();
        registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBus.register(this);
        startBeaconService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopBeaconService();
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

    public class BeaconListAdapter extends ArrayAdapter<Beacon> {

        public BeaconListAdapter() {
            super(getApplicationContext(), R.layout.beacon_item);
        }

        @Override
        public Beacon getItem(int position) {
            return mBeaconList.get(position);
        }

        @Override
        public int getCount() {
            return mBeaconList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = null;
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            item = inflater.inflate(R.layout.beacon_item, null);

            TextView beaconId = (TextView) item.findViewById(R.id.beacon_id);
            TextView beaconDistance = (TextView) item.findViewById(R.id.beacon_distance);

            Beacon beacon = getItem(position);

            if (beacon != null) {
                beaconId.setText(beacon.getId1() + "");
                beaconDistance.setText(beacon.getDistance() + "");
            }

            return item;
        }
    }

    @Subscribe
    public void onBeaconFoundInRange(BeaconsFoundInRange b) {
        mBeaconList = b.getBeacons();
        mAdapter.notifyDataSetChanged();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
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

    private void startBeaconRanging(){
        mBus.post(new BeaconServiceState(true));
    }

    private void startBeaconService(){
        startService(new Intent(this,BeaconService.class));
    }

    private void stopBeaconService(){
        stopService(new Intent(this,BeaconService.class));
    }

    private void stopBeaconRanging(){
        mBus.post(new BeaconServiceState(false));
    }

}
