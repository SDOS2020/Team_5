package org.emberon.winscan.splash.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import org.emberon.winscan.R;
import org.emberon.winscan.base.BaseActivity;
import org.emberon.winscan.main.MainActivity;
import org.emberon.winscan.splash.SplashContract;
import org.emberon.winscan.splash.presenter.SplashPresenter;
import org.emberon.winscan.util.Utils;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements SplashContract.SplashView {

    private final int RC_SIGN_IN = 101;
    @Inject
    SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build());

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setAlwaysShowSignInMethodScreen(true)
                            .build(),
                    RC_SIGN_IN);
        } else {
            signInSuccess();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                signInSuccess();
            } else {
                Toast.makeText(this, "Error Logging in", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void signInSuccess() {
        presenter.fetchUser();
    }

    @Override
    public void fetchUserSuccess() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void fetchUserFailure(String message) {
        Utils.showToast(message);
        finish();
    }
}