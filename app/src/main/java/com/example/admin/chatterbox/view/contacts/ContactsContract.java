package com.example.admin.chatterbox.view.contacts;

import com.example.admin.chatterbox.BasePresenter;
import com.example.admin.chatterbox.BaseView;

/**
 * Created by Admin on 10/23/2017.
 */

public interface ContactsContract {
    interface View extends BaseView {
        void updateView(String contactName);


    }

    interface Presenter extends BasePresenter<View> {
        void validateContactName(String uid, String contactName);
    }
}
