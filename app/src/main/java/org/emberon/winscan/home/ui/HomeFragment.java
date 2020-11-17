package org.emberon.winscan.home.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.blikoon.qrcodescanner.QrCodeActivity;

import org.emberon.winscan.base.BaseActivity;
import org.emberon.winscan.base.BaseFragment;
import org.emberon.winscan.databinding.FragmentHomeBinding;
import org.emberon.winscan.home.HomeContract;
import org.emberon.winscan.home.presenter.HomePresenter;
import org.emberon.winscan.util.Utils;

import javax.inject.Inject;

public class HomeFragment extends BaseFragment implements HomeContract.HomeView {

    @Inject
    HomePresenter presenter;
    private FragmentHomeBinding binding;

    private static final int REQUEST_CODE_QR_SCAN = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        presenter.attachView(this);

        binding.buttonPay.setOnClickListener(this::onPayButtonClicked);
        binding.buttonQR.setOnClickListener(this::onQRButtonClicked);
        return binding.getRoot();
    }

    public void onPayButtonClicked(View view) {
        presenter.initiatePayment(
                binding.payeeName.getEditText().getText().toString(),
                binding.payeeUpiId.getEditText().getText().toString(),
                binding.payeeAmount.getEditText().getText().toString(),
                binding.payeeMerchantID.getEditText().getText().toString(),
                binding.payeeRemarks.getEditText().getText().toString());
    }

    public void onQRButtonClicked(View view) {
        Intent i = new Intent(getActivity(), QrCodeActivity.class);
        startActivityForResult(i, REQUEST_CODE_QR_SCAN);
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
                    alertDialog.setMessage("QR Code could not be scanned");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> dialog.dismiss());
                    alertDialog.show();
                }

            } else {
                if (data == null)
                    return;
                //Getting the passed result
                String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                showToast("Have scan result in your app activity :" + result);
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Scan result");
                alertDialog.setMessage(result);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();

            }
        }
    }

    @Override
    public void showToast(String message) {
        Utils.showToast(message);
    }
}
