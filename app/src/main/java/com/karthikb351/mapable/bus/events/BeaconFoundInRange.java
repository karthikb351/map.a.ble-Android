package com.karthikb351.mapable.bus.events;

import org.altbeacon.beacon.Beacon;

/**
 * Created by karthikbalakrishnan on 10/03/15.
 */
public class BeaconFoundInRange {

    Beacon beacon;

    public BeaconFoundInRange(Beacon beacon) {
        this.beacon = beacon;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }
}
