package com.karthikb351.mapable.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import com.karthikb351.mapable.bus.BusProvider;
import com.karthikb351.mapable.bus.events.BeaconsFoundInRange;
import com.squareup.otto.Bus;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

import timber.log.Timber;

/**
 * Created by karthikbalakrishnan on 10/03/15.
 */
public abstract class BeaconService implements BeaconConsumer {

    public BeaconManager beaconManager;
    private Context ctx;
    private Bus mBus = BusProvider.getInstance();
    Handler handler;

    public BeaconService(Context ctx) {

        Timber.tag("BeaconService");
        this.ctx = ctx;
        this.beaconManager = BeaconManager.getInstanceForApplication(ctx);
        this.beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")); // iBeacon standard

        handler = new Handler(Looper.getMainLooper()); // To send messages to the main thread
    }

    @Override
    public Context getApplicationContext() {
        return this.ctx;
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mBus.post(new BeaconsFoundInRange(new ArrayList<Beacon>(beacons)));
                        }
                    });
                }
            }
        });

        try {
            Timber.d("startRangingBeaconsInRegion");
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }


}
