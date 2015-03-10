package com.karthikb351.mapable.service;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;

import com.karthikb351.mapable.bus.BusProvider;
import com.karthikb351.mapable.bus.events.BeaconFoundInRange;
import com.squareup.otto.Bus;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import timber.log.Timber;

/**
 * Created by karthikbalakrishnan on 10/03/15.
 */
public abstract class BeaconService implements BeaconConsumer {

    private BeaconManager beaconManager;
    private Context ctx;
    private Bus mBus = BusProvider.getInstance();

    public BeaconService(Context ctx) {
        this.ctx = ctx;
        this.beaconManager = BeaconManager.getInstanceForApplication(ctx);
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
                if (beacons.size() > 0) {
                    mBus.post(new BeaconFoundInRange(beacons.iterator().next()));

                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }


}
