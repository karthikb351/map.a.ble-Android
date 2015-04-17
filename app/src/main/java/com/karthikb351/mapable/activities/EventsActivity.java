package com.karthikb351.mapable.activities;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karthikb351.mapable.R;
import com.karthikb351.mapable.adapters.EventListAdapter;
import com.karthikb351.mapable.api.EventAPI;
import com.karthikb351.mapable.models.Event;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import timber.log.Timber;


public class EventsActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, SwipeRefreshLayout.OnRefreshListener {

    Location mDeviceLocation;
    private GoogleApiClient mGoogleApiClient;
    EventListAdapter mAdapter;
    RecyclerView mEventListView;
    List<Event> events;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private EventAPI apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        initializeUI();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.github.com")
                .build();

        apiService = restAdapter.create(EventAPI.class);
    }

    private void initializeUI(){
        events = new ArrayList<Event>();
        mEventListView = (RecyclerView) findViewById(R.id.events_list);
        mAdapter = new EventListAdapter(events);
        mEventListView.setAdapter(mAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mEventListView.setLayoutManager(llm);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.eventSwipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
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
        Toast.makeText(getApplicationContext(), "Could Not Get Location", Toast.LENGTH_SHORT).show();
        Log.d("Connection Result", connectionResult.toString());
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
        /*
        Async Task to get events in the nearby location of the user.
         */
        @Override
        protected List<Event> doInBackground(Location... params) {
            Location present_location = params[0];
            //Server call to get events.
            List<Event> events = new ArrayList<Event>();
            return events;
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);

            mAdapter.setEvent(events);
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        new getEventsInBackground().execute(mDeviceLocation);
    }
}