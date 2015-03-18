package com.karthikb351.mapable.bus.events;

import org.altbeacon.beacon.Beacon;

import java.util.List;

/**
 * Created by karthikbalakrishnan on 10/03/15.
 */
public class BeaconsFoundInRange {

    List<Beacon> beacons;

    public BeaconsFoundInRange(List<Beacon> beacons) {
        this.beacons = beacons;
    }

    public List<Beacon> getBeacons() {
        return beacons;
    }

    public void setBeacons(List<Beacon> beacons) {
        this.beacons = beacons;
    }
}
