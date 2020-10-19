package org.emberon.winscan.injection.module;

import android.app.Application;
import android.content.Context;

import org.emberon.winscan.base.UseCaseHandler;
import org.emberon.winscan.base.UseCaseThreadPoolScheduler;
import org.emberon.winscan.injection.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    UseCaseHandler provideUseCaseHandler() {
        return new UseCaseHandler(new UseCaseThreadPoolScheduler());
    }
}