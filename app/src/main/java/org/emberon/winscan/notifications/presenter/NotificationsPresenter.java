package org.emberon.winscan.notifications.presenter;

import android.app.Activity;
import android.content.Context;

import org.emberon.winscan.base.BaseView;
import org.emberon.winscan.base.UseCaseHandler;
import org.emberon.winscan.data.local.LocalRepository;
import org.emberon.winscan.domain.entity.Rewards;
import org.emberon.winscan.domain.usecase.UpdateUser;
import org.emberon.winscan.injection.ActivityContext;
import org.emberon.winscan.notifications.NotificationsContract;

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
}
