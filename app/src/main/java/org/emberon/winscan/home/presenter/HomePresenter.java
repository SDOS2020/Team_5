package org.emberon.winscan.home.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.shreyaspatil.easyupipayment.EasyUpiPayment;
import com.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import com.shreyaspatil.easyupipayment.model.TransactionDetails;

import org.emberon.winscan.base.BaseView;
import org.emberon.winscan.base.UseCase;
import org.emberon.winscan.base.UseCaseHandler;
import org.emberon.winscan.data.local.LocalRepository;
import org.emberon.winscan.domain.entity.Transaction;
import org.emberon.winscan.domain.entity.User;
import org.emberon.winscan.domain.usecase.UpdateUser;
import org.emberon.winscan.home.HomeContract;
import org.emberon.winscan.injection.ActivityContext;
import org.emberon.winscan.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class HomePresenter implements HomeContract.HomePresenter, PaymentStatusListener {

    private final static int UPI_PAYMENT = 123;
    private final @ActivityContext
    Context context;
    private final Activity activity;
    private final LocalRepository localRepository;
    private final UseCaseHandler useCaseHandler;
    private final UpdateUser updateUserUseCase;
    private HomeContract.HomeView view;
    private EasyUpiPayment easyUpiPayment;

    @Inject
    public HomePresenter(@ActivityContext Context context, Activity activity,
                         LocalRepository localRepository, UseCaseHandler useCaseHandler,
                         UpdateUser updateUserUseCase) {
        this.context = context;
        this.activity = activity;
        this.localRepository = localRepository;
        this.useCaseHandler = useCaseHandler;
        this.updateUserUseCase = updateUserUseCase;
    }

    @Override
    public void attachView(BaseView baseView) {
        view = (HomeContract.HomeView) baseView;
    }

    @Override
    public void initiatePayment(String payeeName, String upiId, String amount, String remarks) {
        if (TextUtils.isEmpty(payeeName)) {
            view.showToast("Name is invalid");
        } else if (TextUtils.isEmpty(upiId)) {
            view.showToast("UPI ID is invalid");
        } else if (TextUtils.isEmpty(amount)) {
            view.showToast("Amount is invalid");
            Toast.makeText(context, " Amount is invalid", Toast.LENGTH_SHORT).show();
        } else {
            if (!amount.contains("."))
                amount = amount + ".00";
            String tid = "TID" + System.currentTimeMillis();
            payUsingUpi(payeeName, upiId, tid, tid, amount, remarks);
        }
    }

    private void payUsingUpi(String name, String upiId, String transactionId,
                             String transactionRefId,
                             String amount, String note) {
        try {
            // Build instance
            EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(activity)
                    .setPayeeVpa(upiId)
                    .setPayeeName(name)
                    .setTransactionId(transactionId)
                    .setTransactionRefId(transactionRefId)
                    .setDescription(note)
                    .setAmount(amount);
            easyUpiPayment = builder.build();
            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this);

            // Start payment / transaction
            easyUpiPayment.startPayment();
        } catch (Exception exception) {
            exception.printStackTrace();
            view.showToast("Error: " + exception.getMessage());
        }
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
        view.showToast("Transaction cancelled.");
    }

    private void onTransactionSuccess() {
        // Payment Success
        view.showToast("Transaction success.");
    }

    private void onTransactionSubmitted() {
        // Payment Pending
    }

    private void onTransactionFailed() {
        // Payment Failed
        view.showToast("Transaction failed.");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user
                    // simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (Utils.isConnectionAvailable(context)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String[] response = str.split("&");
            for (String s : response) {
                String[] equalStr = s.split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                // Code to handle successful transaction here.
                // TODO:: call updateTransactions() with params;
                view.showToast("Transaction successful.");
                Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                view.showToast("Transaction cancelled.");
            } else {
                view.showToast("Transaction failed. Please try again.");
            }
        } else {
            view.showToast("Internet connection is not available. Please check and try again");
        }
    }

    private void updateTransactions(String id, String payerName, String payeeName,
                                    String payerUpiId,
                                    String payeeUpiId, long amount, Date transactionDate) {
        final User user = localRepository.getUser();
        final List<Transaction> transactions = user.getTransactions();
        transactions.add(new Transaction(id, payerName, payeeName, payerUpiId, payeeUpiId, amount
                , transactionDate));
        localRepository.saveUser(user);
        useCaseHandler.execute(updateUserUseCase, new UpdateUser.RequestValues(user),
                new UseCase.UseCaseCallback<UpdateUser.ResponseValue>() {
                    @Override
                    public void onSuccess(UpdateUser.ResponseValue response) {

                    }

                    @Override
                    public void onError(String message) {

                    }
                });
    }

}