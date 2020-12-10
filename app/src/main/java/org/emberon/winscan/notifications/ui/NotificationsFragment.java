package org.emberon.winscan.notifications.ui;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.thefinestartist.finestwebview.FinestWebView;
import com.thefinestartist.finestwebview.listeners.WebViewListener;

import org.emberon.winscan.CashBackEngineService;
import org.emberon.winscan.base.BaseActivity;
import org.emberon.winscan.databinding.FragmentNotificationsBinding;
import org.emberon.winscan.domain.entity.Rewards;
import org.emberon.winscan.notifications.NotificationsContract;
import org.emberon.winscan.notifications.presenter.NotificationsPresenter;
import org.emberon.winscan.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;


public class NotificationsFragment extends Fragment implements NotificationsContract.NotificationsView, RewardListAdapter.RewardsListListener {

    private List<Rewards> rewardsList = new ArrayList<>();
    private int cashBackAmount = 0;
    private String rewardGeneratedCompany = new String();
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private HashMap<String, String> websites = new HashMap<String, String>() {{
        put("zomato", "https://www.zomato.com/");
        put("uber", "https://www.uber.com/");
    }};

    @Inject
    NotificationsPresenter presenter;
    @Inject
    RewardListAdapter adapter;
    private @NonNull
    FragmentNotificationsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        presenter.attachView(this);
        rewardsList = presenter.getRewardsList();
        binding.rewardsView.setHasFixedSize(true);
        binding.rewardsView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.rewardsView.setAdapter(adapter);
        adapter.setRewardsList(rewardsList);
        adapter.setRewardsListListener(this::onRewardClick);
        binding.buttonRewardQR.setOnClickListener(this::onRewardQRButtonClicked);
        if (rewardsList.isEmpty()) {
            binding.textViewNoReward.setVisibility(View.VISIBLE);
            binding.rewardsView.setVisibility(View.INVISIBLE);
        } else {
            binding.textViewNoReward.setVisibility(View.INVISIBLE);
            binding.rewardsView.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }

    public void createWebview(String url) {
        new FinestWebView.Builder(binding.getRoot().getContext()).addWebViewListener(new WebViewListener() {
            @Override
            public void onPageStarted(String url) {
                super.onPageStarted(url);
            }
        }).show(url);
    }

    @Override
    public void onRewardClick(int position) {
        if (rewardsList.get(position).getCurrentStatus() == Rewards.rewardStatus.UNCLAIMED) {
            presenter.updateRewards(position, Rewards.rewardStatus.CLAIMED);
            rewardsList = presenter.getRewardsList();
            adapter.setRewardsList(rewardsList);
            adapter.notifyDataSetChanged();
            createWebview(websites.get(rewardsList.get(position).getCompany()));
            presenter.notifyServer(rewardsList.get(position));
        }
    }

    public void onRewardQRButtonClicked(View view) {
        if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(getActivity(), QrCodeActivity.class);
            startActivityForResult(i, REQUEST_CODE_QR_SCAN);
        } else
            showToast("Please allow access to camera!");
    }

    public void generateReward(String rewardCompany) {
        rewardGeneratedCompany = rewardCompany;
        binding.textViewNoReward.setVisibility(View.INVISIBLE);
        binding.rewardsView.setVisibility(View.VISIBLE);
        //URI TO GENERATE REWARD myapp://reward?comp=zomato
        CashBackEngineService cashbackGenerator = new CashBackEngineService();
        cashBackAmount = cashbackGenerator.getCashBack();
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Reward!!!!");
        alertDialog.setMessage("Congratulations you have won Rs" + cashBackAmount + " cashback " +
                "from " + rewardCompany);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", this::addReward);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Claim", this::claimReward);
        alertDialog.show();
    }

    private void claimReward(DialogInterface dialogInterface, int i) {
        showToast("Reward Claimed");
        presenter.updateRewards(rewardGeneratedCompany, cashBackAmount,
                Rewards.rewardStatus.CLAIMED);
        rewardsList = presenter.getRewardsList();
        adapter.setRewardsList(rewardsList);
        adapter.notifyDataSetChanged();
        createWebview(websites.get(rewardGeneratedCompany));
        presenter.notifyServer(new Rewards(rewardGeneratedCompany, cashBackAmount, Rewards.rewardStatus.CLAIMED));
    }

    private void addReward(DialogInterface dialogInterface, int i) {
        showToast("Reward Added");
        presenter.updateRewards(rewardGeneratedCompany, cashBackAmount,
                Rewards.rewardStatus.UNCLAIMED);
        rewardsList = presenter.getRewardsList();
        adapter.setRewardsList(rewardsList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (resultCode != Activity.RESULT_OK) {
                if (data == null)
                    return;
                //Getting the passed result
                String result = data.getStringExtra("com.blikoon.qrcodescanner" +
                        ".error_decoding_image");
                if (result != null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Scan Error");
                    alertDialog.setMessage("QR Code could not be scanned!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> dialog.dismiss());
                    alertDialog.show();
                }

            } else {
                if (data == null)
                    return;
                //Getting the passed result
                String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Uri resultUri = Uri.parse(result);
                String rewardCompany = resultUri.getQueryParameter("comp");

                if (TextUtils.isEmpty(rewardCompany)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Scan Error");
                    alertDialog.setMessage("Invalid QR Code!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> dialog.dismiss());
                    alertDialog.show();
                } else {
                    generateReward(rewardCompany);
                }
            }
        }
    }

    public void showToast(String message) {
        Utils.showToast(message);
    }
}
