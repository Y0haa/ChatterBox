package com.example.admin.chatterbox.view.joingroup;

import com.example.admin.chatterbox.BasePresenter;
import com.example.admin.chatterbox.BaseView;


/**
 * Created by Admin on 10/15/2017.
 */

public interface JoinGroupContract {

    interface View extends BaseView {
        void updateView(String groupName);


    }

    interface Presenter extends BasePresenter<JoinGroupContract.View> {
        void validateGroupName(String uid, String groupName);

    }
}
