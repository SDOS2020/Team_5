package org.emberon.winscan.splash.presenter;

import org.emberon.winscan.base.BaseView;
import org.emberon.winscan.base.UseCase;
import org.emberon.winscan.base.UseCaseHandler;
import org.emberon.winscan.data.local.LocalRepository;
import org.emberon.winscan.domain.entity.User;
import org.emberon.winscan.domain.usecase.FetchUser;
import org.emberon.winscan.domain.usecase.InitUser;
import org.emberon.winscan.splash.SplashContract;

import javax.inject.Inject;

public class SplashPresenter implements SplashContract.SplashPresenter {

    private final UseCaseHandler useCaseHandler;
    private final FetchUser fetchUserUseCase;
    private final InitUser initUserUseCase;
    private final LocalRepository localRepository;
    private SplashContract.SplashView view;

    @Inject
    public SplashPresenter(UseCaseHandler useCaseHandler, FetchUser fetchUserUseCase,
                           InitUser initUserUseCase, LocalRepository localRepository) {
        this.useCaseHandler = useCaseHandler;
        this.fetchUserUseCase = fetchUserUseCase;
        this.initUserUseCase = initUserUseCase;
        this.localRepository = localRepository;
    }

    @Override
    public void attachView(BaseView baseView) {
        view = (SplashContract.SplashView) baseView;
    }

    @Override
    public void fetchUser() {
        useCaseHandler.execute(fetchUserUseCase, new FetchUser.RequestValues(),
                new UseCase.UseCaseCallback<FetchUser.ResponseValue>() {
                    @Override
                    public void onSuccess(FetchUser.ResponseValue response) {
                        final User user = response.getUser();
                        if (user == null) {
                            initUser();
                        } else {
                            localRepository.saveUser(user);
                            view.fetchUserSuccess();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        view.fetchUserFailure(message);
                    }
                });
    }

    private void initUser() {
        useCaseHandler.execute(initUserUseCase, new InitUser.RequestValues(),
                new UseCase.UseCaseCallback<InitUser.ResponseValue>() {
                    @Override
                    public void onSuccess(InitUser.ResponseValue response) {
                        fetchUser();
                    }

                    @Override
                    public void onError(String message) {
                        view.fetchUserFailure(message);
                    }
                });
    }
}
