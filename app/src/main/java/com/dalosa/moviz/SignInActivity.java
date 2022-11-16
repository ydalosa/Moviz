package com.dalosa.moviz;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {


    private View baseView;

    private void initUi() {
        baseView = findViewById(R.id.mainLayout);
    }

    private void singUpActivity() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );


        Intent signIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)

                .setLogo(R.drawable.ic_baseline_movie_24)
                .setTheme(R.style.LoginTheme)
                .setTosAndPrivacyPolicyUrls("https://google.fr", "https://yahoo.fr")
                .setIsSmartLockEnabled(true)
                .build();

        signLauncher.launch(signIntent);
    }

    private final ActivityResultLauncher<Intent> signLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignResult(result);
                }
            }
    );

    private void onSignResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            //connecté
            Utils.showSnackBar(baseView, getString(R.string.Connected));
        } else {
            //Pas connecté
            if (response == null) {
                Utils.showSnackBar(baseView, getString(R.string.error_canceled));
            } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                Utils.showSnackBar(baseView, "No internet");
            } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
                    Utils.showSnackBar(baseView, getString(R.string.sign_result_unknow_error));

                }
            }
        }
        public void startSignUpActivity (View view){

            Log.i("TAG", "startSignUpActivity: ");
            singUpActivity();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initUi();
    }

    @Override
    protected void onStart() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
        }
    }
}