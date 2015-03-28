package com.karthikb351.mapable.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karthikb351.mapable.R;
import com.karthikb351.mapable.models.DistanceBucket;

import org.altbeacon.beacon.Beacon;

import java.util.List;


public class BeaconListAdapter extends RecyclerView.Adapter<BeaconListAdapter.ListItemViewHolder>{

    private List<Beacon> beacons;

    public BeaconListAdapter(List<Beacon> beacons) {
        this.beacons = beacons;
    }

    public List<Beacon> getBeacons() {
        return beacons;
    }

    public void setBeacons(List<Beacon> beacons) {
        this.beacons = beacons;
    }

    @Override
    public int getItemCount() {
        return beacons.size();
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.beacon_item,
                viewGroup,
                false);

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        Beacon beacon = beacons.get(position);

        Double beacon_distance = new Double(beacon.getDistance());
        holder.distance.setText(beacon_distance.toString());
        holder.distanceBucket.setText(DistanceBucket.getDistanceBucketForDistance(
                beacon.getDistance()).toString());
        holder.macAddress.setText(beacon.getBluetoothAddress());
    }

    public final static class ListItemViewHolder extends  RecyclerView.ViewHolder {

        public TextView macAddress;
        public TextView distance;
        public TextView distanceBucket;

        public ListItemViewHolder(View itemView){
            super(itemView);
            macAddress = (TextView) itemView.findViewById(R.id.beacon_id);
            distance = (TextView) itemView.findViewById(R.id.beacon_distance);
            distanceBucket = (TextView) itemView.findViewById(R.id.bucket);
        }

    }
}
