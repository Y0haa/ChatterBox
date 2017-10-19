package com.example.admin.chatterbox.view.groupactivity;


import android.net.Uri;

import com.example.admin.chatterbox.BasePresenter;
import com.example.admin.chatterbox.BaseView;
import com.example.admin.chatterbox.model.chat.Group;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by admin on 10/11/2017.
 */

public interface GroupActivityContract {

    interface View extends BaseView {

        void updateInputMsg();

        void updateOnSendImage(String something);
    }

    interface Presenter extends BasePresenter<View> {

        void sendMessage(String id, String msg, String owner, String ownerId, Long time);
        DatabaseReference getDatabaseReference();
        Group getGroup(String id);

        void findDatabaseReference();


        void uploadImage(Uri imageUri, String ownerId, String filename);
    }

}
