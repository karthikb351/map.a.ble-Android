package com.karthikb351.mapable.models;

/**
 * Created by sreeram on 4/13/15.
 */
public enum Building {
    TechnologyTower("TT"),
    SMV("SMV"),
    SilverJublieeTower("SJT");

    private String mBuildingName;

    private Building(String building){
        mBuildingName=building;
    }

    public String getValue(){
        return mBuildingName;
    }
}
