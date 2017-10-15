package com.example.admin.chatterbox.view.creategroupactivity;


import android.util.Log;

import com.example.admin.chatterbox.model.chat.Group;
import com.example.admin.chatterbox.model.chat.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Admin on 10/11/2017.
 */

public class CreateGroupActivityPresenter implements CreateGroupActivityContract.Presenter{
    private static final String TAG = "CreateGroupPresenterTag";
    private FirebaseDatabase database;
    private DatabaseReference myRefUsers;
    String uid;
    CreateGroupActivityContract.View view;

    @Override
    public void attacheView(CreateGroupActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void validateGroupName(String uid, final String groupName) {

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


        Log.d(TAG, "onSuccess: " +myRefUsers.child("Groups").push().getKey() );

        myRefUsers
                // .child(uid)
                // .child("groupCreated")
                .push()
                .setValue(group)

        ;
        Log.d(TAG, "onSuccess: " +myRefUsers.child("Groups").push().getKey() );
      //  Log.d(TAG, "validateGroupName: " + myRefUsers.child("Groups").getRef().push().getKey());
        //var name = snapshot.name();
       // var newRef = myDataRef.push(...);
        //var newID = newRef.name();
        /*
        Log.d(TAG, "onSuccess: " +myRefUsers.getKey() );
        Log.d(TAG, "onSuccess: " +myRefUsers.child("Groups").getRef().getKey() );
        Log.d(TAG, "onSuccess: " +myRefUsers.child("Groups").getParent().getKey() );
*/

        //-------------------------------

        //String outputString =groupName ;
        view.updateView(groupName);
    }
}
