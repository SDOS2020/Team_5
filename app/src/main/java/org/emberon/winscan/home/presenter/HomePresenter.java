package org.emberon.winscan.home.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.shreyaspatil.easyupipayment.EasyUpiPayment;
import com.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import com.shreyaspatil.easyupipayment.model.PaymentApp;
import com.shreyaspatil.easyupipayment.model.TransactionDetails;

import org.emberon.winscan.base.BaseView;
import org.emberon.winscan.base.UseCase;
import org.emberon.winscan.base.UseCaseHandler;
import org.emberon.winscan.data.local.LocalRepository;
import org.emberon.winscan.domain.entity.Rewards;
import org.emberon.winscan.domain.entity.Transaction;
import org.emberon.winscan.domain.entity.User;
import org.emberon.winscan.domain.usecase.UpdateUser;
import org.emberon.winscan.home.HomeContract;
import org.emberon.winscan.injection.ActivityContext;
import org.emberon.winscan.util.DebugUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class HomePresenter implements HomeContract.HomePresenter, PaymentStatusListener {

    private final @ActivityContext
    Context context;
    private final Activity activity;
    private final LocalRepository localRepository;
    private final UseCaseHandler useCaseHandler;
    private final UpdateUser updateUserUseCase;
    private HomeContract.HomeView view;

    private String payeeName;
    private String payeeUpiId;
    private String transactionId;
    private String transactionRefId;
    private String payeeMerchantCode;
    private String amount;
    private String remarks;

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
    public void initiatePayment(String payeeName, String upiId, String amount, String payeeMerchantCode, String remarks) {
        if (TextUtils.isEmpty(payeeName)) {
            view.showToast("Name is invalid.");
        } else if (TextUtils.isEmpty(upiId)) {
            view.showToast("UPI ID is invalid.");
        } else if (TextUtils.isEmpty(amount)) {
            view.showToast("Amount is invalid.");
        } else if (TextUtils.isEmpty(payeeMerchantCode)) {
            view.showToast("Merchant ID is invalid.");
        } else if (TextUtils.isEmpty(remarks)) {
            view.showToast("Please enter valid remarks.");
        } else {
            if (!amount.contains("."))
                amount = amount + ".00";
            String tid = "TID" + System.currentTimeMillis();
            this.payeeName = payeeName;
            this.payeeUpiId = upiId;
            this.transactionId = tid;
            this.transactionRefId = tid;
            this.amount = amount;
            this.payeeMerchantCode = payeeMerchantCode;
            this.remarks = remarks;
            payViaUpi(payeeName, upiId, tid, tid, amount, payeeMerchantCode,remarks);
        }
    }

    private void payViaUpi(String name, String upiId, String transactionId,
                           String transactionRefId, String amount, String payeeMerchantCode,String remarks) {
        try {
            // Build instance
            EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(activity)
                    .with(PaymentApp.ALL)
                    .setPayeeVpa(upiId)
                    .setPayeeName(name)
                    .setTransactionId(transactionId)
                    .setTransactionRefId(transactionRefId)
                    .setPayeeMerchantCode(payeeMerchantCode)
                    .setDescription(remarks)
                    .setAmount(amount);

            EasyUpiPayment easyUpiPayment = builder.build();
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
    public void onTransactionCompleted(@NotNull TransactionDetails transactionDetails) {
        DebugUtil.log(transactionDetails);
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
        DebugUtil.log("onTransactionCancelled");
        updateTransactions(Transaction.transactionStatus.CANCELLED);
    }

    private void onTransactionSuccess() {
        // Payment Success
        updateTransactions(Transaction.transactionStatus.SUCCESSFUL);
        view.showToast("Transaction success.");
        DebugUtil.log("onTransactionSuccess");
    }

    private void onTransactionSubmitted() {
        // Payment Pending
        Date date = new Date();
        DebugUtil.log("onTransactionSubmitted");
        updateTransactions(Transaction.transactionStatus.PENDING);
    }

    private void onTransactionFailed() {
        // Payment Failed
        updateTransactions(Transaction.transactionStatus.FAILED);
        view.showToast("Transaction failed.");
        DebugUtil.log("Transaction failed.");
    }

    private void updateTransactions(Transaction.transactionStatus currentStatus) {
        DebugUtil.log("updateTransactions");
        final User user = localRepository.getUser();
        final List<Transaction> transactions = user.getTransactions();
        transactions.add(new Transaction(transactionId, "Payee Name Here", payeeName,
                "Payer Name Here", payeeUpiId, (long) (Double.parseDouble(amount) * 100),
                new Date(), currentStatus));
        updateUser(user);
    }

    private void updateUser(User user) {
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

    public void updateRewards(String company, int amount, Rewards.rewardStatus rewardStatus) {
        DebugUtil.log("updateRewards");
        final User user = localRepository.getUser();
        final List<Rewards> rewards = user.getRewards();
        rewards.add(new Rewards(company, amount, rewardStatus));
        updateUser(user);
    }
}