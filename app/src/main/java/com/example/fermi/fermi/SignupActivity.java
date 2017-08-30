package com.example.fermi.fermi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
public class SignupActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private static final int RC_EMAIL_SIGNUP = 55;
    Button facebookButton;
    TextView emailSignup, loginText2, loginText1;
    DatabaseReference mDatabase;
    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        facebookButton = (Button) findViewById(R.id.fbSignup);
        emailSignup = (TextView) findViewById(R.id.emailSignup);
        loginText2 = (TextView) findViewById(R.id.loginText2);
        loginText1 = (TextView) findViewById(R.id.loginText1);


        auth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (auth.getCurrentUser() != null) {
            // already signed in
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            SignupActivity.this.finish();
        } else {
           /* emailSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(SignupActivity.this, GetSubjectsActivity.class), RC_EMAIL_SIGNUP);
                }
            });*/
            facebookButton.setOnClickListener(new View.OnClickListener() {
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
            emailSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(
                                            Arrays.asList(
                                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                                            ))
                                    .setTheme(R.style.FirebaseTheme)
                                    .build(),
                            RC_SIGN_IN);
                }
            });
            loginText1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    SignupActivity.this.finish();
                }
            });
            loginText2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    SignupActivity.this.finish();
                }
            });
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN || requestCode == RC_EMAIL_SIGNUP) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {

                User user = new User();
                user.setName(auth.getCurrentUser().getDisplayName());
                mDatabase.child("users").child(auth.getCurrentUser().getUid()).setValue(user);


                startActivity(new Intent(SignupActivity.this, GetProfileActivity.class));
                SignupActivity.this.finish();
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