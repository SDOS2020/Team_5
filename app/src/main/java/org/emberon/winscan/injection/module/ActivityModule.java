package org.emberon.winscan.injection.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.emberon.winscan.injection.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity providesActivity() {
        return activity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return activity;
    }

    @Provides
    Intent providesIntent() {
        return activity.getIntent();
    }

}