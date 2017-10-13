package com.example.admin.chatterbox.view.groupactivity;

import com.example.admin.chatterbox.model.chat.Chat;
import com.example.admin.chatterbox.model.chat.Group;
import com.example.admin.chatterbox.model.chat.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by admin on 10/11/2017.
 */

public class GroupActivityPresenter implements GroupActivityContract.Presenter {

    GroupActivityContract.View view;
    DatabaseReference databaseReference;

    @Override
    public void attacheView(GroupActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void sendMessage(String id, String msg, User owner, Long time) {
        Chat chat = new Chat(msg,owner,time);
        databaseReference.child("groups").child("id").push().setValue(chat);
        view.updateInputMsg();
    }

    @Override
    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    @Override
    public Group getGroup(String id) {

        return null;
    }

    @Override
    public void findDatabaseReference() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
}
