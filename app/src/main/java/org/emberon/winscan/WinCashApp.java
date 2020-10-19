package org.emberon.winscan;

import android.app.Application;
import android.content.Context;

import org.emberon.winscan.injection.component.ApplicationComponent;
import org.emberon.winscan.injection.component.DaggerApplicationComponent;
import org.emberon.winscan.injection.module.ApplicationModule;

public class WinCashApp extends Application {

    private static WinCashApp instance;

    ApplicationComponent applicationComponent;

    public static WinCashApp get(Context context) {
        return (WinCashApp) context.getApplicationContext();
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (WinCashApp.instance == null) {
            WinCashApp.instance = this;
        }

        component().inject(this);
    }

    public ApplicationComponent component() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}

