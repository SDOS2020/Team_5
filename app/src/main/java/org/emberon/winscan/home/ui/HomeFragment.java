package org.emberon.winscan.home.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.emberon.winscan.base.BaseActivity;
import org.emberon.winscan.base.BaseFragment;
import org.emberon.winscan.databinding.FragmentHomeBinding;
import org.emberon.winscan.home.HomeContract;
import org.emberon.winscan.home.presenter.HomePresenter;
import org.emberon.winscan.util.Utils;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import javax.inject.Inject;

import com.google.zxing.Result;
import me.sudar.zxingorient.ZxingOrient;
import me.sudar.zxingorient.ZxingOrientResult;

public class HomeFragment extends BaseFragment implements HomeContract.HomeView {

    @Inject
    HomePresenter presenter;
    private FragmentHomeBinding binding;

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
        new ZxingOrient((Activity) binding.getRoot().getContext()).initiateScan();
//        ScannerActivity.startActivityForQrCode(this, REQUEST_CODE_SCANNER)
        showToast("START");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ZxingOrientResult scanResult = ZxingOrient.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            binding.payeeUpiId.getEditText().setText(scanResult.getContents());
            showToast(scanResult.getContents());
        }
    }

    @Override
    public void showToast(String message) {
        Utils.showToast(message);
    }
}
