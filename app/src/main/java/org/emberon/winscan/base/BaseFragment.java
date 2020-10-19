package org.emberon.winscan.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.jetbrains.annotations.NotNull;

public class BaseFragment extends Fragment {

    private BaseActivityCallback callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showProgressDialog(String message) {
        callback.showProgressDialog(message);
    }

    protected void hideProgressDialog() {
        callback.hideProgressDialog();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            callback = (BaseActivityCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BaseActivityCallback methods");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getSimpleName());
        FirebaseAnalytics.getInstance(getContext())
                .logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    public void replaceFragmentUsingFragmentManager(Fragment fragment, boolean addToBackStack,
                                                    int containerId) {
        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = getFragmentManager().popBackStackImmediate(backStateName,
                0);

        if (!fragmentPopped && getFragmentManager().findFragmentByTag(backStateName) ==
                null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(containerId, fragment, backStateName);
            if (addToBackStack) {
                transaction.addToBackStack(backStateName);
            }
            transaction.commit();
        }
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = getChildFragmentManager().popBackStackImmediate(backStateName,
                0);

        if (!fragmentPopped && getChildFragmentManager().findFragmentByTag(backStateName) ==
                null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(containerId, fragment, backStateName);
            if (addToBackStack) {
                transaction.addToBackStack(backStateName);
            }
            transaction.commit();
        }
    }
}
