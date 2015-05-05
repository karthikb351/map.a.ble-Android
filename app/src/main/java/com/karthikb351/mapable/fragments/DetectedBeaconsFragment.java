package com.karthikb351.mapable.fragments;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karthikb351.mapable.R;
import com.karthikb351.mapable.adapters.BeaconListAdapter;
import com.karthikb351.mapable.bus.BusProvider;
import com.karthikb351.mapable.bus.events.BeaconServiceState;
import com.karthikb351.mapable.bus.events.BeaconsFoundInRange;
import com.karthikb351.mapable.service.BeaconService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 5/4/15.
 */
public class DetectedBeaconsFragment extends Fragment {

    List<Beacon> mBeaconList = new ArrayList<>();
    BeaconListAdapter mAdapter;
    RecyclerView mBeaconListView;
    private Bus mBus = BusProvider.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_detected_beacons, null);

        mBeaconListView = (RecyclerView) root.findViewById(R.id.beacons_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mBeaconListView.setLayoutManager(llm);

        mAdapter = new BeaconListAdapter(mBeaconList);
        mBeaconListView.setAdapter(mAdapter);

        return root;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    @Subscribe
    public void onBeaconFoundInRange(BeaconsFoundInRange b) {
        mBeaconList = b.getBeacons();
        //After  getting the beacons, populate them in the list.
        mAdapter.setBeacons(mBeaconList);
        mAdapter.notifyDataSetChanged();
    }

}
