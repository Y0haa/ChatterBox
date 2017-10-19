package com.example.admin.chatterbox.view.loginactivity;

import com.example.admin.chatterbox.model.chat.User;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by Admin on 10/12/2017.
 */

public interface MainLoginContract {

    interface View{
        void showDialog(String title, String msg);
        void userSuccessful(String user);
    }

    interface UserActionsListener{
        void createFirebaseUser(String emailUser, String passwordUser);
        void handleFacebookAccessToken(AccessToken token);
        void firebaseAuthWithGoogle(GoogleSignInAccount acct);

        void signinFirebaseUser(String emailUser, String passwordUser);
        void updateFirebaseUser(User user, String password);
    }

}
