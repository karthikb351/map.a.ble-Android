package com.karthikb351.mapable;

import android.app.Application;
import android.content.Intent;
import android.content.ServiceConnection;

import com.karthikb351.mapable.bus.BusProvider;
import com.karthikb351.mapable.bus.events.BeaconFoundInRange;
import com.karthikb351.mapable.service.BeaconService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import timber.log.Timber;

/**
 * Created by karthikbalakrishnan on 10/03/15.
 */
public class Mapable extends Application {
    private Bus mBus = BusProvider.getInstance();
    private BeaconService mService;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

        Timber.tag("MapableApplication");
        Timber.d("Application Created");

        mService = new BeaconService(Mapable.this) {

            @Override
            public void unbindService(ServiceConnection serviceConnection) {
                Mapable.this.unbindService(serviceConnection);
            }
            @Override
            public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
                return Mapable.this.bindService(intent, serviceConnection, i);
            }
        };

        mBus.register(this); //listen for "global" events
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.HollowTree {
        @Override public void i(String message, Object... args) {
            // TODO e.g., Crashlytics.log(String.format(message, args));
        }

        @Override public void i(Throwable t, String message, Object... args) {
            i(message, args); // Just add to the log.
        }

        @Override public void e(String message, Object... args) {
            i("ERROR: " + message, args); // Just add to the log.
        }

        @Override public void e(Throwable t, String message, Object... args) {
            e(message, args);

            // TODO e.g., Crashlytics.logException(t);
        }
    }



    @Subscribe
    void onBeaconFoundInRange(BeaconFoundInRange b){
        Timber.d("The beacon I see is about "+b.getBeacon().getDistance()+" meters away.");
    }
}
