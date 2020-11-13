package org.emberon.winscan.dashboard;

import android.content.Intent;

import org.emberon.winscan.base.BasePresenter;
import org.emberon.winscan.base.BaseView;
import org.emberon.winscan.domain.entity.Transaction;

import java.util.List;

public interface DashboardContract {

    interface DashboardView extends BaseView<DashboardContract.DashboardPresenter> {

    }

    interface DashboardPresenter extends BasePresenter {
        List<Transaction> getTransactionList();
    }
}
