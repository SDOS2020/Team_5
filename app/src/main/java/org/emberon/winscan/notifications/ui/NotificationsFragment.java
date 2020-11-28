package org.emberon.winscan.notifications.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thefinestartist.finestwebview.FinestWebView;
import com.thefinestartist.finestwebview.listeners.WebViewListener;

import org.emberon.winscan.base.BaseActivity;
import org.emberon.winscan.base.UseCase;
import org.emberon.winscan.databinding.FragmentNotificationsBinding;
import org.emberon.winscan.domain.entity.Rewards;
import org.emberon.winscan.domain.entity.User;
import org.emberon.winscan.domain.usecase.UpdateUser;
import org.emberon.winscan.notifications.NotificationsContract;
import org.emberon.winscan.notifications.presenter.NotificationsPresenter;
import org.emberon.winscan.util.DebugUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class NotificationsFragment extends Fragment implements NotificationsContract.NotificationsView, RewardListAdapter.RewardsListListener {

    private List<Rewards> rewardsList = new ArrayList<>();

    @Inject
    NotificationsPresenter presenter;
    @Inject
    RewardListAdapter adapter;
    private @NonNull FragmentNotificationsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        presenter.attachView(this);
        binding.rewardsView.setHasFixedSize(true);
        binding.rewardsView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.rewardsView.setAdapter(adapter);
        rewardsList = presenter.getRewardsList();
        adapter.setRewardsList(rewardsList);
        adapter.setRewardsListListener(this::onRewardClick);
        return binding.getRoot();
    }

    @Override
    public void onRewardClick(int position) {
        if (rewardsList.get(position).getCurrentStatus() == Rewards.rewardStatus.UNCLAIMED) {
            presenter.updateRewardStatus(rewardsList.get(position));
            Toast.makeText(binding.getRoot().getContext(), rewardsList.get(position).getCompany(), Toast.LENGTH_SHORT).show();
            new FinestWebView.Builder(binding.getRoot().getContext()).addWebViewListener(new WebViewListener() {
                @Override
                public void onPageStarted(String url) {
                    System.out.println("REWARD" + url);
                    super.onPageStarted(url);
                }
            }).show("https://www.zomato.com/");
        }
    }
}
