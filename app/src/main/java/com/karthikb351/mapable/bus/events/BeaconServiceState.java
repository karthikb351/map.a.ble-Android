package com.karthikb351.mapable.bus.events;

/**
 * Created by Nikhil on 3/25/15.
 */
public class BeaconServiceState {


    public BeaconServiceState(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    private boolean isActive;



}
