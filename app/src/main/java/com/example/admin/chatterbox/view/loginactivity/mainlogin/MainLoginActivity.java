package com.example.admin.chatterbox.view.loginactivity.mainlogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.injection.loginactivity.DaggerLoginActivityComponent;
import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.example.admin.chatterbox.view.loginactivity.MainLoginContract;
import com.example.admin.chatterbox.view.loginactivity.MainLoginPresenter;
import com.example.admin.chatterbox.view.loginactivity.registeruser.MainRegisterActivity;
import com.example.admin.chatterbox.view.loginactivity.signinuser.MainSignInActivity;
import com.example.admin.chatterbox.view.mainactivity.MainActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import javax.inject.Inject;


public class MainLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, MainLoginContract.View{

    private FirebaseAuth mAuth;

    // Constants
    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";
    private static final int RC_SIGN_IN = 9002;

    @Inject
    MainLoginPresenter mActionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        setupDagger();
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupDagger() {
        DaggerLoginActivityComponent.create().inject(this);
        mActionsListener.attacheView(this);
    }

    public void registerNewUser(View view) {
        Intent intent = new Intent(this, MainRegisterActivity.class);
        startActivity(intent);
    }

    public void signInExistingUser(View view) {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        //TODO REVIEW ISSUE WITH AUTHENTICATION AFTER MINUTES
        if (currentUser != null){
            Log.d("TAG", "onStart: " + currentUser.getDisplayName());
            //CurrentStoredUser.generateUserBaseOnAuthObject(mAuth);
            //saveDisplayName(CurrentStoredUser.getInstance().getUser().getEmail());
            //callNextActivity();
            mActionsListener.getLastUserOnDB(mAuth.getCurrentUser().getUid());
        } else{
            Log.d("TAG", "onStart: " + "No user");
            Intent intent = new Intent(this, MainSignInActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        reproduceVideo();


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Log.d("TAG", "onStart: " + currentUser.getDisplayName());
        } else{
            Log.d("TAG", "onStart: " + "No user");
        }
    }

    private void saveDisplayName(String user) {
        String displayName = user;//mUsernameView.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, 0);
        prefs.edit().putString(DISPLAY_NAME_KEY, displayName).apply();
    }

    private void callNextActivity() {
        Intent intent = new Intent(MainLoginActivity.this, MainActivity.class);
        startActivity(intent);
    }


    public void signOutAuth(View view) {

        if (mAuth != null){
            mAuth.signOut();

            Toast.makeText(this, "User logged out", Toast.LENGTH_SHORT).show();

            try{
                LoginManager.getInstance().logOut();
            }catch(Exception e){
            }

            try{
                GoogleApiClient mGoogleApiClient;

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this, this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {@Override
                public void onResult(@NonNull Status status) {}});

            }catch(Exception e){
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void reproduceVideo() {
        VideoView mVideoView;

        mVideoView = (VideoView) findViewById(R.id.bgVideoView);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.landscape);

        mVideoView.setVideoURI(uri);
        mVideoView.start();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
    }


    @Override
    public void showDialog(String title, String msg) {

    }

    @Override
    public void userSuccessful(String user) {
    }

    @Override
    public void userSuccessful(Object value) {
        CurrentStoredUser.generateUserBaseOnUserObject((HashMap<String, String>) value);
        saveDisplayName(CurrentStoredUser.getInstance().getUser().getEmail());
        callNextActivity();
    }
}
