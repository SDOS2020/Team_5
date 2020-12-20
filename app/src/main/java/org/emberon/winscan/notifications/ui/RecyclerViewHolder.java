package org.emberon.winscan.notifications.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.emberon.winscan.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView cashBackAmount;

    private final TextView cashBackStatus;
    private final ImageView cashBackCompanyLogo;
    RewardListAdapter.RewardsListListener rewardsListListener;

    public RecyclerViewHolder(@NonNull View itemView, RewardListAdapter.RewardsListListener rewardsListListener) {
        super(itemView);
        cashBackAmount = itemView.findViewById(R.id.cashBackAmount);
        cashBackStatus = itemView.findViewById(R.id.cashBackStatus);
        cashBackCompanyLogo = itemView.findViewById(R.id.cashBackCompanyLogo);
        this.rewardsListListener = rewardsListListener;
        itemView.setOnClickListener(this);
    }

    public TextView getCashBackAmount(){
        return cashBackAmount;
    }

    public TextView getCashBackStatus(){
        return cashBackStatus;
    }
    public ImageView getCashBackCompanyLogo(){return cashBackCompanyLogo;}
    @Override
    public void onClick(View v) {
        rewardsListListener.onRewardClick(getAdapterPosition());
    }
}
