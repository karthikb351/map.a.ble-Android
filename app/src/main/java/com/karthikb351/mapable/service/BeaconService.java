package com.karthikb351.mapable.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;

import com.karthikb351.mapable.bus.BusProvider;
import com.karthikb351.mapable.bus.events.BeaconServiceState;
import com.karthikb351.mapable.bus.events.BeaconsFoundInRange;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

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
public class BeaconService extends Service{

    public BeaconManager beaconManager;
    private Bus mBus = BusProvider.getInstance();
    Handler handler;

    public BeaconService(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBus.register(this);
        Timber.tag("BeaconService");
        Timber.d("Service created");
        this.beaconManager = BeaconManager.getInstanceForApplication(BeaconService.this.getApplicationContext());
        this.beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")); // iBeacon standard
        startBeaconConsumer();
        handler = new Handler(Looper.getMainLooper()); // To send messages to the main thread
        getApplicationContext().registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                        == BluetoothAdapter.STATE_OFF){
                    beaconManager.unbind(mConsumer);
                }
                else if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                        == BluetoothAdapter.STATE_ON){
                    beaconManager.bind(mConsumer);
                }
                // Bluetooth is disconnected, do handling here
            }

        }

    };

    public void startBeaconConsumer() {
        beaconManager.bind(mConsumer);
    }

    public void stopBeaconConsumer() {
        beaconManager.unbind(mConsumer);
    }

    @Subscribe
    public void serviceStateChanged(BeaconServiceState state){
        if(state.isActive()) {
            startBeaconConsumer();
        }
        else {
            stopBeaconConsumer();
        }
    }

    BeaconConsumer mConsumer = new BeaconConsumer() {
        @Override
        public Context getApplicationContext() {
            return BeaconService.this.getApplicationContext();
        }

        @Override
        public void onBeaconServiceConnect() {
            Timber.d("BeaconService Connected");
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

        @Override
        public void unbindService(ServiceConnection serviceConnection) {
            Timber.d("unbinding cons");
            BeaconService.this.unbindService(serviceConnection);
        }

        @Override
        public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
            Timber.d("binding cons");
            return BeaconService.this.bindService(intent,serviceConnection,i);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(mConsumer);
        mBus.unregister(this);
        Timber.d("Service destroyed");
    }
}
