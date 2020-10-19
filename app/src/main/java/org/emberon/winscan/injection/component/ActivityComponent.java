package org.emberon.winscan.injection.component;

import org.emberon.winscan.MainActivity;
import org.emberon.winscan.injection.PerActivity;
import org.emberon.winscan.injection.module.ActivityModule;
import org.emberon.winscan.splash.SplashActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {ActivityModule.class})

public interface ActivityComponent {

    void inject(SplashActivity splashActivity);

    void inject(MainActivity mainActivity);

}
