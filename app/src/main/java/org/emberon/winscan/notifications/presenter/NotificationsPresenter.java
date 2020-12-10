package org.emberon.winscan.notifications.presenter;

import android.app.Activity;
import android.content.Context;

import org.emberon.winscan.base.BaseView;
import org.emberon.winscan.base.UseCase;
import org.emberon.winscan.base.UseCaseHandler;
import org.emberon.winscan.data.local.LocalRepository;
import org.emberon.winscan.domain.entity.Rewards;
import org.emberon.winscan.domain.entity.User;
import org.emberon.winscan.domain.usecase.UpdateUser;
import org.emberon.winscan.injection.ActivityContext;
import org.emberon.winscan.notifications.NotificationsContract;
import org.emberon.winscan.util.DebugUtil;

import java.util.List;

import javax.inject.Inject;

public class NotificationsPresenter implements NotificationsContract.NotificationsPresenter  {
    private final static int UPI_PAYMENT = 123;
    private final @ActivityContext
    Context context;
    private final Activity activity;
    private final LocalRepository localRepository;
    private final UseCaseHandler useCaseHandler;
    private final UpdateUser updateUserUseCase;
    private NotificationsContract.NotificationsView view;

    @Inject
    public NotificationsPresenter(@ActivityContext Context context, Activity activity,
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
        view = (NotificationsContract.NotificationsView) baseView;
    }

    @Override
    public List<Rewards> getRewardsList() {
        return localRepository.getUser().getRewards();
    }

    public void updateRewards(String company, int amount, Rewards.rewardStatus rewardStatus) {
        DebugUtil.log("updateRewards");
        final User user = localRepository.getUser();
        final List<Rewards> rewards = user.getRewards();
        rewards.add(new Rewards(company, amount, rewardStatus));
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

    @Override
    public void updateRewards(int position, Rewards.rewardStatus claimed) {
        DebugUtil.log("updateRewards");
        final User user = localRepository.getUser();
        final List<Rewards> rewards = user.getRewards();
        rewards.get(position).setCurrentStatus(claimed);
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

    @Override
    public void notifyServer(Rewards rewards) {
        // TODO :: call server api
    }
}
