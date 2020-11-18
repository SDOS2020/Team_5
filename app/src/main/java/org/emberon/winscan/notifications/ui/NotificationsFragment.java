package org.emberon.winscan.notifications.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thefinestartist.finestwebview.FinestWebView;

import org.emberon.winscan.base.BaseActivity;
import org.emberon.winscan.databinding.FragmentNotificationsBinding;
import org.emberon.winscan.notifications.NotificationsContract;
import org.emberon.winscan.notifications.presenter.NotificationsPresenter;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;


public class NotificationsFragment extends Fragment implements NotificationsContract.NotificationsView, RewardListAdapter.RewardsListListener {
    @Inject
    NotificationsPresenter presenter;
    @Inject
    RewardListAdapter adapter;
    private @NonNull FragmentNotificationsBinding binding;
    List<String> rewardList;
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
        rewardList = Arrays.asList("ZOMATO", "UBER", "OLA");
        adapter.setRewardsList(rewardList);
        adapter.setRewardsListListener(this::onRewardClick);
        return binding.getRoot();
    }

    @Override
    public void onRewardClick(int position) {
        Toast.makeText(binding.getRoot().getContext(), rewardList.get(position), Toast.LENGTH_SHORT).show();
        new FinestWebView.Builder(binding.getRoot().getContext()).show("https://www.zomato.com/");
    }
}
