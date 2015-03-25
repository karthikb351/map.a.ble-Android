package com.karthikb351.mapable.activities;

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
import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.karthikb351.mapable.Mapable;
import com.karthikb351.mapable.R;
import com.karthikb351.mapable.bus.BusProvider;
import com.karthikb351.mapable.bus.events.BeaconsFoundInRange;

import com.google.android.gms.location.LocationServices;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
   GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Mapable app;
    List<Beacon> mBeaconList = new ArrayList<>();
    Location mDeviceLocation;
    BeaconListAdapter mAdapter;
    private GoogleApiClient mGoogleApiClient;
    ListView mBeaconListView;
    ListView mEventView;
    private Bus mBus = BusProvider.getInstance();


    @Override
    public void onLocationChanged(Location location) {
        mDeviceLocation = location;
        //Send a broadcast event to get beacons in range.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get Location

        app = (Mapable)getApplication();
        mBeaconListView = (ListView)findViewById(R.id.beacons_list);
        mAdapter = new BeaconListAdapter();
        mBeaconListView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        app.startBeaconService();
        mBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        app.stopBeaconService();
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

    public class BeaconListAdapter extends ArrayAdapter<Beacon>{

        public BeaconListAdapter(){
            super(getApplicationContext(),R.layout.beacon_item);
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
            item = inflater.inflate(R.layout.beacon_item,null);

            TextView beaconId = (TextView)item.findViewById(R.id.beacon_id);
            TextView beaconDistance = (TextView)item.findViewById(R.id.beacon_distance);

            Beacon beacon = getItem(position);

            if(beacon != null){
                beaconId.setText(beacon.getId1()+"");
                beaconDistance.setText(beacon.getDistance()+"");
            }

            return item;
        }
    }

    @Subscribe
    public void onBeaconFoundInRange(BeaconsFoundInRange b){
        mBeaconList = b.getBeacons();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        return locationRequest;
    }

    @Override
    public void onConnected(Bundle bundle) {
        // creates a location request
        LocationRequest locationRequest = createLocationRequest();
        startLocationUpdates(locationRequest);
    }

    protected void startLocationUpdates(LocationRequest locationRequest) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,locationRequest, this);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
}

