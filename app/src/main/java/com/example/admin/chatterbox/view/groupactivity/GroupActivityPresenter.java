package com.example.admin.chatterbox.view.groupactivity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.admin.chatterbox.model.chat.Chat;
import com.example.admin.chatterbox.model.chat.Group;
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
    public void sendMessage(String id, String msg, String owner, String ownerId, Long time) {
        Chat chat = new Chat(msg,owner,ownerId, time);
        databaseReference.child("Groups").child(id).child("messages").push().setValue(chat);
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
    public void uploadImage(Uri imageUri, String ownerId, String filename) {
        if(imageUri != null) {

            //Storing in unique location
            StorageReference childRef = storageRef.child(ownerId+"/"+filename);

            //uploading the image
            UploadTask uploadTask = childRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
