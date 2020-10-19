package org.emberon.winscan.base;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.emberon.winscan.WinCashApp;
import org.emberon.winscan.injection.component.ActivityComponent;
import org.emberon.winscan.injection.component.DaggerActivityComponent;
import org.emberon.winscan.injection.module.ActivityModule;


@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements BaseActivityCallback {

    public ProgressDialog progressDialog;

    private ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(WinCashApp.get(this).component())
                    .build();
        }
        return activityComponent;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void cancelProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    public void addFragment(Fragment fragment, int containerId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(containerId, fragment);
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, int containerId,
                                String tag) {
        invalidateOptionsMenu();
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(tag, 0);
        if (!fragmentPopped && getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.replace(containerId, fragment, tag);
            if (addToBackStack) {
                transaction.addToBackStack(tag);
            }
            transaction.commit();
        }
    }

    public void clearFragmentBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            int backStackId = getSupportFragmentManager().getBackStackEntryAt(i).getId();
            fm.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

}
