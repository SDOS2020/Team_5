package org.emberon.winscan.injection.component;

import org.emberon.winscan.dashboard.ui.DashboardFragment;
import org.emberon.winscan.home.ui.HomeFragment;
import org.emberon.winscan.injection.PerActivity;
import org.emberon.winscan.injection.module.ActivityModule;
import org.emberon.winscan.main.MainActivity;
import org.emberon.winscan.notifications.ui.NotificationsFragment;
import org.emberon.winscan.splash.ui.SplashActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {ActivityModule.class})

public interface ActivityComponent {

    void inject(SplashActivity splashActivity);

    void inject(MainActivity mainActivity);

    void inject(HomeFragment homeFragment);

    void inject(DashboardFragment dashboardFragment);

    void inject(NotificationsFragment notificationsFragment);
}
