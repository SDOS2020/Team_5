package org.emberon.winscan.notifications.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.emberon.winscan.R;
import org.emberon.winscan.domain.entity.Rewards;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class RewardListAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    private List<Rewards> rewardsList = new ArrayList<>();
    private RewardsListListener rewardsListListener;

    @Inject
    public RewardListAdapter() {

    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.framelayout_rewards;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(view, rewardsListListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.getCashBackAmount().setText(String.valueOf(rewardsList.get(position).getValue()));
        holder.getCashBackCompany().setText(String.valueOf(rewardsList.get(position).getCompany()));
        Rewards.rewardStatus status = rewardsList.get(position).getCurrentStatus();
        holder.getCashBackStatus().setText(String.valueOf(status));
        if (status == Rewards.rewardStatus.UNCLAIMED)
            holder.getCashBackStatus().setTextColor(Color.RED);
        else
            holder.getCashBackStatus().setTextColor(Color.GRAY);

    }

    @Override
    public int getItemCount() { return rewardsList.size(); }

    public void setRewardsList(List<Rewards> rewardsList) {
        this.rewardsList = rewardsList;
    }

    public void setRewardsListListener (RewardsListListener rewardsListListener) {
        this.rewardsListListener = rewardsListListener;
    }

    public interface RewardsListListener {
        void onRewardClick(int position);
    }
}
