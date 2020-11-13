package org.emberon.winscan.splash;

import org.emberon.winscan.base.BasePresenter;
import org.emberon.winscan.base.BaseView;

public interface SplashContract {

    interface SplashView extends BaseView<SplashPresenter> {

        void fetchUserFailure(String message);

        void fetchUserSuccess();
    }

    interface SplashPresenter extends BasePresenter {

        void fetchUser();
    }
}
