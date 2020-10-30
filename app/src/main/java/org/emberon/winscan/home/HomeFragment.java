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
import com.shreyaspatil.easyupipayment.EasyUpiPayment;
import com.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import com.shreyaspatil.easyupipayment.model.TransactionDetails;

import org.emberon.winscan.MainActivity;
import org.emberon.winscan.R;

public class HomeFragment extends Fragment implements PaymentStatusListener {

    private HomeViewModel homeViewModel;
    private EasyUpiPayment easyUpiPayment;
    private Button buttonPay;
    private TextView textView;
    private TextInputLayout name;
    private TextInputLayout upiId;
    private TextInputLayout amount;
    private TextInputLayout note;
    String nameText;
    String upiIdText;
    String amountText;
    String noteText;
    Activity context;
    public final int UPI_PAYMENT = 123;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textView = root.findViewById(R.id.text_home);
        buttonPay = root.findViewById(R.id.button_pay);
        name = root.findViewById(R.id.name);
        upiId = root.findViewById(R.id.upi_id);
        amount = root.findViewById(R.id.amount);
        note = root.findViewById(R.id.note);
        context = getActivity();
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        clearFields();
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText = name.getEditText().getText().toString();
                upiIdText = upiId.getEditText().getText().toString();
                amountText = amount.getEditText().getText().toString();
                noteText = note.getEditText().getText().toString();
                if (TextUtils.isEmpty(nameText)) {
                    Toast.makeText(context, " Name is invalid", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(upiIdText)) {
                    Toast.makeText(context, " UPI ID is invalid", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(amount.getEditText().getText().toString())) {
                    Toast.makeText(context, " Amount is invalid", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(noteText)) {
                    Toast.makeText(context, " Note is invalid", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!amountText.contains("."))
                        amountText = amountText + ".00";
                    textView.setText(upiIdText.concat(amountText));
                    String tid = "TID" + System.currentTimeMillis();
                    payUsingUpi(nameText, upiIdText, tid, tid, amountText, noteText);
//                    payUsingUpi("vaibhav","asdaa", tid, tid, "900", "transfeere");
                }
            }
        });
        return root;
    }

    void payUsingUpi(String name, String upiId, String transactionId, String transactionRefId, String amount, String note) {

        System.out.println("Name: "+name+" UPI: "+upiId+" TID: "+transactionId+" TREFID: "+transactionRefId+" amount: "+amount+" NOTE: "+note);
        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(context)
                .setPayeeVpa(upiId)
                .setPayeeName(name)
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionRefId)
                .setDescription(note)
                .setAmount(amount);

        try {
            // Build instance
            easyUpiPayment = builder.build();

            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this);

            // Start payment / transaction
            easyUpiPayment.startPayment();
        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(context, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void clearFields() {
       name.getEditText().getText().clear();
       upiId.getEditText().getText().clear();
       amount.getEditText().getText().clear();
       note.getEditText().getText().clear();
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // Transaction Completed
        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }
    }

    @Override
    public void onTransactionCancelled() {
        // Payment Cancelled by User
        Toast.makeText(context, "Cancelled by user", Toast.LENGTH_SHORT).show();
    }

    private void onTransactionSuccess() {
        // Payment Success
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
    }

    private void onTransactionSubmitted() {
        // Payment Pending
        Toast.makeText(context, "Pending | Submitted", Toast.LENGTH_SHORT).show();
    }

    private void onTransactionFailed() {
        // Payment Failed
        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
    }
/*      void payUsingUpi(String name, String upiId, String transactionId, String transactionRefId, String amount, String note) {
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
    } */
}
