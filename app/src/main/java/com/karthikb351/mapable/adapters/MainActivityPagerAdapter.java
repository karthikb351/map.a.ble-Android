package com.karthikb351.mapable.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.karthikb351.mapable.fragments.DetectedBeaconsFragment;
import com.karthikb351.mapable.fragments.DetectedRulesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 5/4/15.
 */
public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    Context mContext;

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new DetectedRulesFragment();
                break;
            case 1:
                fragment = new DetectedBeaconsFragment();
                break;
        }

        return fragment;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";

        switch (position) {
            case 0:
                title = "Actions";
                break;
            case 1:
                title = "Beacons";
                break;
        }

        return title;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
