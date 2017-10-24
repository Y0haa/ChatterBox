package com.example.admin.chatterbox.view.mainactivity;

import android.util.Log;

import com.example.admin.chatterbox.model.chat.Group;
import com.example.admin.chatterbox.model.chat.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Admin on 10/10/2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
/*
    MainActivityContract.View view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
*/
    private static final String TAG = "MainActivityPresenterTag";
    private FirebaseDatabase database;
    private DatabaseReference myRefUsers;
    String uid;
    MainActivityContract.View view;

    @Override
    public void attacheView(MainActivityContract.View view) {
        this.view = view;

    }

    @Override
    public void detachView() {

        this.view = null;
    }

    @Override
    public void createChatGroup(String groupName) {

        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                uid = currentUser.getUid();
            }
            database = FirebaseDatabase.getInstance();
            myRefUsers = database.getReference("Groups");
            //-------------------------------
            Group group = new Group();
            User user = new User();
            user.setUsername(uid.toString());

            group.setTitle(groupName);
            group.setAdmin(user);

            String id = myRefUsers.child("Groups").push().getKey();


            Log.d(TAG, "onSuccess: " + id);

            myRefUsers
                    // .child(uid)
                    // .child("groupCreated")
                    .child(id)
                    .setValue(group)

            ;
            view.updateOnCreateChatGroup(id);
        }
        catch (Exception e){
            view.updateOnCreateChatGroup("noToken");
        }

        //-------------------------------

        //String outputString =groupName ;
     //   view.updateView(id);




    }
}
