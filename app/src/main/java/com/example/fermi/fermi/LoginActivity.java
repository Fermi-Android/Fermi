package com.example.fermi.fermi;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button fbButton, loginBtn;
    private TextView signupText1, signupText2, resetPassword;
    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && !user.getDisplayName().equals("") && user.getPhotoUrl() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else if(user != null && (user.getDisplayName().equals("") || user.getPhotoUrl() == null)) {
            startActivity(new Intent(LoginActivity.this, GetProfileActivity.class));
            finish();
        }

        emailInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        fbButton = (Button) findViewById(R.id.fbLogin);
        loginBtn = (Button) findViewById(R.id.loginbtn);
        signupText1 = (TextView) findViewById(R.id.signupText1);
        signupText2 = (TextView) findViewById(R.id.signupText2);
        resetPassword = (TextView) findViewById(R.id.resetPassword);

        loginBtn.setEnabled(false);
        final GradientDrawable drawable = (GradientDrawable)loginBtn.getBackground();
        drawable.setStroke(4, getResources().getColor(R.color.colorPrimaryLight));
        loginBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAllowNewEmailAccounts(false)
                                .setAvailableProviders(
                                        Arrays.asList(
                                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                                        ))
                                .build(),
                        999);
            }
        });


        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().equals("")) {
                    if(!emailInput.getText().toString().trim().equals("")) {
                        loginBtn.setEnabled(true);
                        loginBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        //drawable.setStroke(4, getResources().getColor(R.color.colorPrimaryDark));
                        loginBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(
                                        Arrays.asList(
                                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
                                        ))
                                .build(),
                        RC_SIGN_IN);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailInput.getText().toString().trim().equals("")) {
                    emailInput.setError("Cannot be empty");
                } else if (passwordInput.getText().toString().trim().equals("")) {
                    passwordInput.setError("Cannot be empty");
                } else {

                    auth.signInWithEmailAndPassword(emailInput.getText().toString().trim(), passwordInput.getText().toString().trim())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()) {
                                        if(passwordInput.getText().toString().trim().length() < 6) {
                                            passwordInput.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

        signupText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });
        signupText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e("Login","Login canceled by User");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.e("Login","No Internet Connection");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login","Unknown Error");
                    return;
                }
            }
            Log.e("Login","Unknown sign in response");
        }
    }

}