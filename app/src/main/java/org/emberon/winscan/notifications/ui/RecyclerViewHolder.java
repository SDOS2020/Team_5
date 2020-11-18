package org.emberon.winscan.notifications.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.emberon.winscan.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView cashBackAmount;
    RewardListAdapter.RewardsListListener rewardsListListener;

    public RecyclerViewHolder(@NonNull View itemView, RewardListAdapter.RewardsListListener rewardsListListener) {
        super(itemView);
        cashBackAmount = itemView.findViewById(R.id.cashBackAmount);
        this.rewardsListListener = rewardsListListener;
        itemView.setOnClickListener(this);
    }

    public TextView getCashBackAmount(){
        return cashBackAmount;
    }

    @Override
    public void onClick(View v) {
        rewardsListListener.onRewardClick(getAdapterPosition());
    }
}
