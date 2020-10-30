package org.emberon.winscan.home;

import android.content.Intent;

import org.emberon.winscan.base.BasePresenter;
import org.emberon.winscan.base.BaseView;

public interface HomeContract {

    interface HomeView extends BaseView<HomePresenter> {

        void showToast(String message);
    }

    interface HomePresenter extends BasePresenter {

        void initiatePayment(String payeeName, String upiId, String amount, String remarks);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
