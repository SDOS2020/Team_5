package org.emberon.winscan.home.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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

        if (ContextCompat.checkSelfPermission(binding.getRoot().getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(getActivity(), QrCodeActivity.class);
            startActivityForResult(i, REQUEST_CODE_QR_SCAN);
        }
        else
            showToast("Please allow access to camera!");
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
                    binding.payeeName.getEditText().getText().clear();
                    binding.payeeUpiId.getEditText().getText().clear();
                }

            } else {
                if (data == null)
                    return;
                //Getting the passed result
                String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Uri resultUri = Uri.parse(result);
                String payeeUpiIdQR = resultUri.getQueryParameter("pa");
                String payeeNameQR = resultUri.getQueryParameter("pn");
                if ((TextUtils.isEmpty(payeeNameQR) || (TextUtils.isEmpty(payeeUpiIdQR)))) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Scan Error");
                    alertDialog.setMessage("Invalid QR Code!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> dialog.dismiss());
                    alertDialog.show();
                    binding.payeeName.getEditText().getText().clear();
                    binding.payeeUpiId.getEditText().getText().clear();
                }
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Scan result");
                    alertDialog.setMessage("Name: " + payeeNameQR + "\n" + "UPI ID: " + payeeUpiIdQR);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> dialog.dismiss());
                    alertDialog.show();
                    binding.payeeName.getEditText().setText(payeeNameQR);
                    binding.payeeUpiId.getEditText().setText(payeeUpiIdQR);
                }
            }
        }
    }

    @Override
    public void showToast(String message) {
        Utils.showToast(message);
    }
}
