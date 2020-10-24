package org.emberon.winscan.home;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import org.emberon.winscan.MainActivity;
import org.emberon.winscan.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button buttonPay;
    private TextView textView;
    private TextInputLayout upiId;
    private TextInputLayout amount;
    String upiIdText;
    String amountText;
    Activity context;
    public final int UPI_PAYMENT = 123;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textView = root.findViewById(R.id.text_home);
        buttonPay = root.findViewById(R.id.button_pay);
        upiId = root.findViewById(R.id.upi_id);
        amount = root.findViewById(R.id.amount);
        context = getActivity();
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(upiId.getEditText().getText().toString())) {
                    Toast.makeText(context, " UPI ID is invalid", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(amount.getEditText().getText().toString())) {
                    Toast.makeText(context, " Amount is invalid", Toast.LENGTH_SHORT).show();
                }
                else {
                    upiIdText = upiId.getEditText().getText().toString();
                    amountText = amount.getEditText().getText().toString();
                    textView.setText(upiIdText.concat(amountText));
                    Log.e("main ", "STEP 1");
                    payUsingUpi("VAOBHAV", upiIdText,
                            "TIRAL", amountText);
                }
            }
        });
        return root;
    }

    void payUsingUpi(  String name,String upiId, String note, String amount) {

        Log.e("main ", "STEP 2");
        Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        Log.e("main ", "STEP 3");
        ComponentName x =  chooser.resolveActivity(context.getPackageManager());
        System.out.println(x);
        if(null != x) {
            Log.e("main ", "STEP INSIDE");
            context.startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Log.e("main ", "STEP OUTSIDE");
            Toast.makeText(context,"No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }
}
