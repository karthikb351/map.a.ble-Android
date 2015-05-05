package com.karthikb351.mapable.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karthikb351.mapable.R;
import com.karthikb351.mapable.adapters.RulesListAdapter;
import com.karthikb351.mapable.bus.BusProvider;
import com.karthikb351.mapable.bus.events.BeaconsSatisfiedRules;
import com.karthikb351.mapable.models.RuleModel;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 5/5/15.
 */
public class DetectedRulesFragment extends Fragment {

    List<RuleModel> mRuleModelsList = new ArrayList<>();
    RulesListAdapter mAdapter;
    RecyclerView mRulesListView;
    private Bus mBus = BusProvider.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_detected_rules, null);

        mRulesListView = (RecyclerView) root.findViewById(R.id.lv_rules);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRulesListView.setLayoutManager(llm);

        mAdapter = new RulesListAdapter(mRuleModelsList);
        mRulesListView.setAdapter(mAdapter);

        return root;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    @Subscribe
    public void onRulesFoundInRange(BeaconsSatisfiedRules satisfiedRules) {
        mRuleModelsList = satisfiedRules.getRuleList();
        //After  getting the beacons, populate them in the list.
        mAdapter.setRules(mRuleModelsList);
        mAdapter.notifyDataSetChanged();
    }
}
