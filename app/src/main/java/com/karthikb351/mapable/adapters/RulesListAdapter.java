package com.karthikb351.mapable.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karthikb351.mapable.R;
import com.karthikb351.mapable.models.ActionModel;
import com.karthikb351.mapable.models.DistanceBucket;
import com.karthikb351.mapable.models.RuleModel;

import org.altbeacon.beacon.Beacon;

import java.text.DecimalFormat;
import java.util.List;


public class RulesListAdapter extends RecyclerView.Adapter<RulesListAdapter.ListItemViewHolder>{

    private List<RuleModel> mRuleModelList;

    public RulesListAdapter(List<RuleModel> ruleModels) {
        this.mRuleModelList = ruleModels;
    }

    public List<RuleModel> getRules() {
        return mRuleModelList;
    }

    public void setRules(List<RuleModel> ruleModels) {
        this.mRuleModelList = ruleModels;
    }

    @Override
    public int getItemCount() {
        return mRuleModelList.size();
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.row_action,
                viewGroup,
                false);

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        ActionModel action = mRuleModelList.get(position).getAction();

        holder.payload.setText(action.getPayload());
        holder.type.setText(""+action.getType());
        holder.description.setText(action.getDescription());
    }

    public final static class ListItemViewHolder extends  RecyclerView.ViewHolder {

        TextView description;
        TextView type;
        TextView payload;

        public ListItemViewHolder(View itemView){
            super(itemView);
            description = (TextView)itemView.findViewById(R.id.tv_action_description);
            type = (TextView)itemView.findViewById(R.id.tv_action_type);
            payload = (TextView)itemView.findViewById(R.id.tv_action_payload);
        }

    }
}
