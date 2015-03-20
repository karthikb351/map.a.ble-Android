package com.karthikb351.mapable.service;

import android.content.Context;
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

    public BeaconService(Context ctx) {
        this.ctx = ctx;
        this.beaconManager = BeaconManager.getInstanceForApplication(ctx);
        this.beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")); // iBeacon standard
        Timber.tag("BeaconService");
    }

    @Override
    public Context getApplicationContext() {
        return this.ctx;
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Timber.d("Beacons found in range");
                Timber.d("Number of beacons:"+beacons.size());
                Timber.d("Beacon ID:"+(beacons.iterator().hasNext()?beacons.iterator().next().getBluetoothName()+beacons.iterator().next().getId1():"Empty List"));
                if (beacons.size() > 0) {
                    mBus.post(new BeaconsFoundInRange(new ArrayList<Beacon>(beacons)));

                }
            }
        });

        try {
            Timber.d("startRangingBeaconsInRegion");
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }


}
