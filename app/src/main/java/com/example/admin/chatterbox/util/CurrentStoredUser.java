package com.example.admin.chatterbox.util;

import android.util.Log;

import com.example.admin.chatterbox.model.chat.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Admin on 10/13/2017.
 */

public class CurrentStoredUser {

    private static DatabaseReference mDatabaseReference;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user = new User();

    private static final CurrentStoredUser ourInstance = new CurrentStoredUser();

    public static CurrentStoredUser getInstance() {
        return ourInstance;
    }

    private CurrentStoredUser() {
    }

    public static void generateUserBaseOnAuthObject(FirebaseAuth mAuth) {

        try {

            User user = CurrentStoredUser.getInstance().getUser();
            String userName = null;

            if (mAuth.getCurrentUser().getDisplayName() == null) {
                userName = mAuth.getCurrentUser().getEmail().split("@")[0].toString();
            } else {
                userName = mAuth.getCurrentUser().getDisplayName();
            }

            user.setName(userName);
            user.setEmail(mAuth.getCurrentUser().getEmail());
            user.setUsername(userName);
            user.setPhoneNumber("not setting yet");
            user.setId(mAuth.getCurrentUser().getUid());



/*
            mDatabaseReference = FirebaseDatabase.getInstance().getReference();
            mDatabaseReference.child("users").push().setValue(user);
*/


        }catch(Exception e){
            Log.d("TAG", "generateUserBaseOnAuthObject: " + "User not generated. Auth is empty");
        }
    }

    public static void updateUserBaseOnUserDB(User user) {

        try {

            CurrentStoredUser.getInstance().setUser(user);

        }catch(Exception e){
            Log.d("TAG", "generateUserBaseOnAuthObject: " + "User not generated. Auth is empty");
        }
    }
}
