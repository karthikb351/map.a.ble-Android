package com.karthikb351.mapable.activities;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karthikb351.mapable.R;
import com.karthikb351.mapable.adapters.EventListAdapter;
import com.karthikb351.mapable.models.Event;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class EventsActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Location mDeviceLocation;
    private GoogleApiClient mGoogleApiClient;
    EventListAdapter mAdapter;
    RecyclerView mEventListView;
    List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        events = new ArrayList<Event>();
        mEventListView = (RecyclerView) findViewById(R.id.events_list);
        mAdapter = new EventListAdapter(events);
        mEventListView.setAdapter(mAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mEventListView.setLayoutManager(llm);

    }

    //All Location related methods
    @Override
    public void onLocationChanged(Location location) {
        mDeviceLocation = location;
        Timber.d("Latitude", location.toString());
        Timber.d("Latitude", location.toString());
        //Refresh nearby events.
        //Write a async task to get nearby events.
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private class getEventsInBackground extends AsyncTask<Location, Integer, List<Event>> {

        @Override
        protected List<Event> doInBackground(Location... params) {
            Location present_location = params[0];
            //Server call to get events.
            return null;
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);
            mAdapter.setEvent(events);
            mAdapter.notifyDataSetChanged();
        }
    }
}