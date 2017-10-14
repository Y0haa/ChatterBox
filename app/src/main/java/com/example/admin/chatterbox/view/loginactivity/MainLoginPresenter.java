package com.example.admin.chatterbox.view.loginactivity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.example.admin.chatterbox.util.CurrentStoredUser.generateUserBaseOnAuthObject;

/**
 * Created by Admin on 10/12/2017.
 */

public class MainLoginPresenter implements MainLoginContract.UserActionsListener {

    private FirebaseAuth mAuth;
    private final MainLoginContract.View mRegisterActivityView;

    public MainLoginPresenter(MainLoginContract.View mRegisterActivityView) {
        this.mRegisterActivityView = mRegisterActivityView;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void createFirebaseUser(String emailUser, String passwordUser) {
        String email = emailUser;
        String password = passwordUser;

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) mRegisterActivityView,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Chat", "createUser onComplete: " + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.d("Chat", "user creation failed: ");
                    mRegisterActivityView.showDialog("Oops", "Registration attemp failed");
                } else {
                    generateUserBaseOnAuthObject(mAuth);
                    mRegisterActivityView.userSuccessful(CurrentStoredUser.getInstance().getUser().getEmail());
                }
            }
        });
    }

    @Override
    public void signinFirebaseUser(String emailUser, String passwordUser) {
        String email = emailUser;
        String password = passwordUser;

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d("Chat", " signInWithEmail onComplete: " + task.isComplete());

                if (!task.isSuccessful()) {
                    Log.d("Chat", "Problem signing in: " + task.getException());
                    mRegisterActivityView.showDialog("Oops", "Registration attemp failed");
                } else {
                    generateUserBaseOnAuthObject(mAuth);
                    mRegisterActivityView.userSuccessful(CurrentStoredUser.getInstance().getUser().getEmail());
                }
            }
        });
    }

    @Override
    public void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mRegisterActivityView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG firebasefb", "signInWithCredential:success");
                            generateUserBaseOnAuthObject(mAuth);
                            mRegisterActivityView.userSuccessful(CurrentStoredUser.getInstance().getUser().getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG firebasefb", "signInWithCredential:failure", task.getException());
                            mRegisterActivityView.showDialog("Oops", "Facebook authentication failed.");
                        }
                    }
                });
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) mRegisterActivityView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            generateUserBaseOnAuthObject(mAuth);
                            mRegisterActivityView.userSuccessful(CurrentStoredUser.getInstance().getUser().getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            mRegisterActivityView.showDialog("Oops", "Facebook authentication failed.");
                        }
                    }
                });
    }


    /*private void generateUserBaseOnAuthObject(FirebaseAuth mAuth) {

        User user = CurrentStoredUser.getInstance().getUser();
        String userName=null;

        if (mAuth.getCurrentUser().getDisplayName() == null ){
            userName = mAuth.getCurrentUser().getEmail().split("@")[0].toString();
        }else{
            userName = mAuth.getCurrentUser().getDisplayName();
        }

        user.setName(userName);
        user.setEmail(mAuth.getCurrentUser().getEmail());
        user.setUsername(userName);
        user.setPhoneNumber("not setting yet");
        user.setId(mAuth.getCurrentUser().getUid());
    }*/
}
