package com.example.admin.chatterbox.util;

import android.util.Log;

import com.example.admin.chatterbox.model.chat.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

/**
 * Created by Admin on 10/13/2017.
 */

public class CurrentStoredUser {


    private User user = new User();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private static final CurrentStoredUser ourInstance = new CurrentStoredUser();

    public static CurrentStoredUser getInstance() {
        return ourInstance;
    }

    private CurrentStoredUser() {
    }

    public static void generateUserBaseOnAuthObject(FirebaseAuth mAuth) {

        try {

            //TODO VALIDATE IF USER EXIST OTHERWISER CREATE THE FIRST ONE


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
            user.setPhoneNumber("9999999999");
            user.setId(mAuth.getCurrentUser().getUid());

        }catch(Exception e){
            Log.d("TAG", "generateUserBaseOnAuthObject: " + "User not generated. Auth is empty");
        }
    }

    public static void generateUserBaseOnUserObject(HashMap<String, String> userDB) {

        try {

            //TODO VALIDATE IF USER EXIST OTHERWISER CREATE THE FIRST ONE

            User user = CurrentStoredUser.getInstance().getUser();
            user.setName(userDB.get("name"));
            user.setEmail(userDB.get("email"));
            user.setUsername(userDB.get("username"));
            user.setPhoneNumber(userDB.get("phoneNumber"));
            user.setId(userDB.get("id"));
            user.setUserImage(userDB.get("userImage"));

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
