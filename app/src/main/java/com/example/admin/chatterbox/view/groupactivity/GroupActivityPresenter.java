package com.example.admin.chatterbox.view.groupactivity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.admin.chatterbox.model.chat.Chat;
import com.example.admin.chatterbox.model.chat.Group;
import com.example.admin.chatterbox.util.Commands;
import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by admin on 10/11/2017.
 */

public class GroupActivityPresenter implements GroupActivityContract.Presenter {

    public static final String TAG="GroupActivityPresenterTAG";

    private static final Intent RESULT_LOAD_IMG =null;
    GroupActivityContract.View view;
    DatabaseReference databaseReference;
    String owner = CurrentStoredUser.getInstance().getUser().getName();
    String ownerId = CurrentStoredUser.getInstance().getUser().getId();
    private String groupId;
    FirebaseStorage storage = FirebaseStorage.getInstance(); // Use for Firebase storage
    StorageReference storageRef = storage.getReferenceFromUrl("gs://chatterbox-b78d6.appspot.com/");//Firebase storage location

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
        databaseReference =

                FirebaseDatabase.getInstance().getReference();
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

    @Override
    public void uploadImage(Uri imageUri, String filename) {
        if(imageUri != null) {

            //Storing in unique location
            final StorageReference childRef = storageRef.child(ownerId+"/"+filename);

            String referenceLocation = ownerId+"/"+filename;
            //uploading the image
            UploadTask uploadTask = childRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String taskSnapshopURL = downloadUrl.toString();

                    final long ONE_MEGABYTE = 1024 * 1024;
                    childRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Data for "images/island.jpg" is returns, use this as needed
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                    sendMessage(taskSnapshopURL,0l );
                    view.updateOnSendImage("File uploaded");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    view.updateOnSendImage("Failed to upload");
                }
            });
        }
        else {
            view.updateOnSendImage("No Image choosen");
        }

    }


}
