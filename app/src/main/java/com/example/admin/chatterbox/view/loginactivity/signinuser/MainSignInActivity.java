package com.example.admin.chatterbox.view.loginactivity.signinuser;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.injection.loginactivity.DaggerLoginActivityComponent;
import com.example.admin.chatterbox.view.loginactivity.MainLoginContract;
import com.example.admin.chatterbox.view.loginactivity.MainLoginPresenter;
import com.example.admin.chatterbox.view.mainactivity.MainActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainSignInActivity extends AppCompatActivity implements View.OnClickListener, MainLoginContract.View, GoogleApiClient.OnConnectionFailedListener {

    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";
    private static final int RC_SIGN_IN = 9002;

    private FirebaseAuth mAuth;
    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;

    @BindView(R.id.login_email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.login_password)
    EditText mPasswordView;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.sign_in_button)
    SignInButton signInButton;
    @BindView(R.id.sign_out_button)
    Button signOutButton;
    @BindView(R.id.disconnect_button)
    Button disconnectButton;
    @BindView(R.id.sign_out_and_disconnect)
    LinearLayout signoutAnddisconnectButton;


    @Inject
    MainLoginPresenter mActionsListener;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sign_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ButterKnife.bind(this);


        mAuth = FirebaseAuth.getInstance();
        setupDagger();

        standardLogin();
        facebookLogin();
        googleLogin();
    }

    private void setupDagger() {
        DaggerLoginActivityComponent.create().inject(this);
        mActionsListener.attacheView(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                Log.d("TAG google success", "onActivityResult: ");
                GoogleSignInAccount account = result.getSignInAccount();
                mActionsListener.firebaseAuthWithGoogle(account);

            } else {
                Log.d("TAG google firebase", "onActivityResult: ");
            }
        }

    }

    private void standardLogin() {
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }

    private void facebookLogin() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TAG facebook", "onSuccess: ");
                mActionsListener.handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("TAG facebook", "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG facebook", "onError: ");
            }
        });
    }

    private void googleLogin() {

        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        disconnectButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void signInExistingUser(View v) {
        attemptLogin();
    }

    private void attemptLogin() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.equals("") || password.equals("")) return;
        Toast.makeText(this, "Login in progress...", Toast.LENGTH_SHORT).show();

        mActionsListener.signinFirebaseUser(email, password);

    }

    @Override
    public void showDialog(String title, String msg) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void userSuccessful(String user) {
        saveDisplayName(user);
        callNextActivity();
    }

    @Override
    public void userSuccessful(Object value) {

    }

    private void saveDisplayName(String user) {
        String displayName = user;
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, 0);
        prefs.edit().putString(DISPLAY_NAME_KEY, displayName).apply();
    }

    private void callNextActivity() {
        Intent intent = new Intent(MainSignInActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.disconnect_button) {
            revokeAccess();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();
        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            signInButton.setVisibility(View.GONE);
            disconnectButton.setVisibility(View.VISIBLE);
        } else {

            signInButton.setVisibility(View.VISIBLE);
            signoutAnddisconnectButton.setVisibility(View.GONE);
        }
    }

    public void forgotPassword(View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_reset_password, null);
        final EditText editText = alertLayout.findViewById(R.id.edit_text_email_id);

        new AlertDialog.Builder(this)
                .setView(alertLayout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mActionsListener.forgotPassword(editText.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

/*    public void sentEmailToResetPassword(View view) {
        switch (view.getId()) {
            case R.id.button_reset_password_id:

                *//*editTextEmailId = findViewById(R.id.edit_text_email_id);*//*
               // mActionsListener.forgotPassword(editTextEmailId.getText().toString());
                break;
        }
    }*/

}
