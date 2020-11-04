package org.emberon.winscan.injection.component;


import org.emberon.winscan.WinCashApp;
import org.emberon.winscan.base.UseCaseHandler;
import org.emberon.winscan.data.local.LocalRepository;
import org.emberon.winscan.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(WinCashApp winCashApp);

    LocalRepository getLocalRepository();

    UseCaseHandler getUseCaseHandler();
}