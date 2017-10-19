package com.example.admin.chatterbox.view.groupactivity;

import com.example.admin.chatterbox.model.chat.Chat;
import com.example.admin.chatterbox.model.chat.Group;
import com.example.admin.chatterbox.util.Commands;
import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by admin on 10/11/2017.
 */

public class GroupActivityPresenter implements GroupActivityContract.Presenter {

    GroupActivityContract.View view;
    DatabaseReference databaseReference;
    String owner = CurrentStoredUser.getInstance().getUser().getName();
    String ownerId = CurrentStoredUser.getInstance().getUser().getId();
    private String groupId;

    @Override
    public void attacheView(GroupActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void sendMessage(String msg, Long time) {
        Chat chat = new Chat(msg, owner, ownerId, time);
        databaseReference.child("Groups").child(groupId).child("messages").push().setValue(chat);
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

    @Override
    public void checkCommand(String msg) {

            String cmd = msg.substring(1);
            String args = cmd.substring(cmd.indexOf(' ') + 1);
            if (cmd.contains(" ")) {
                cmd = cmd.substring(0, cmd.indexOf(' '));
            }
            switch (Commands.valueOf(cmd.toUpperCase())) {
                case HELP:
                    String commandList = "System \n List of commands:\n";
                    for (Commands c :
                            Commands.values()) {
                        commandList += "/" + c.name() + "\n";
                    }
                    view.sendSystemMsg(commandList);
                    break;

                case GIPHY:

                    break;

                case YOKO:
                    sendMessage("/yoko",0l);
                    break;

            }

    }

    @Override
    public void setGroupId(String id) {
        groupId = id;
    }
}
