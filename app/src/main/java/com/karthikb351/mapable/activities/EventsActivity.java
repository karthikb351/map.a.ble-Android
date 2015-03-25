package com.karthikb351.mapable.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karthikb351.mapable.R;
import com.karthikb351.mapable.adapters.EventListAdapter;

/**
 * Created by sreeram on 4/13/15.
 */
public class EventsActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Location mDeviceLocation;
    private GoogleApiClient mGoogleApiClient;
    EventListAdapter mAdapter;
    RecyclerView mEventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        mEventListView = (RecyclerView)findViewById(R.id.events_list);
        mAdapter = new EventListAdapter();
        mEventListView.setAdapter(mAdapter);
    }



    //All Location related methods
    @Override
    public void onLocationChanged(Location location) {
        mDeviceLocation = location;
        //Send a broadcast event to get beacons in range.
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
