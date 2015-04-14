package com.karthikb351.mapable.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karthikb351.mapable.R;
import com.karthikb351.mapable.models.Building;
import com.karthikb351.mapable.models.DistanceBucket;
import com.karthikb351.mapable.models.Event;

import org.altbeacon.beacon.Beacon;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by sreeram on 4/13/15.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ListItemViewHolder>{

    List<Event> events;

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setEvent(List<Event> events){ this.events = events;}
    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.event_item,
                viewGroup,
                false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        Event event = events.get(position);
        holder.setItems(event);

    }


    public final static class ListItemViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView eventTitle;
        public TextView eventLocation;
        public TextView eventTime;
        public Event event;

        public ListItemViewHolder(View itemView){
            super(itemView);
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventLocation = (TextView) itemView.findViewById(R.id.event_location);
            eventTime = (TextView) itemView.findViewById(R.id.event_timing);
        }
        public void setItems(Event event){
            String title = event.name;
            String location = event.building.getValue();
            String timing = event.date_string;
            eventTitle.setText(title);
            eventLocation.setText(location);
            eventTime.setText(timing);
            this.event=event;
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("title", event.name);

        }
    }
}
