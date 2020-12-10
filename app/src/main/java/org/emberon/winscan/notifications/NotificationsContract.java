package org.emberon.winscan.notifications;

import org.emberon.winscan.base.BasePresenter;
import org.emberon.winscan.base.BaseView;
import org.emberon.winscan.domain.entity.Rewards;
import org.emberon.winscan.domain.entity.Transaction;

import java.util.List;


public interface NotificationsContract {
    interface NotificationsView extends BaseView<NotificationsContract.NotificationsPresenter> {

    }

    interface NotificationsPresenter extends BasePresenter {
        List<Rewards> getRewardsList();
        public void updateRewards(String company, int amount, Rewards.rewardStatus rewardStatus);

        void updateRewards(int position, Rewards.rewardStatus claimed);

        void notifyServer(Rewards rewards);
    }
}
