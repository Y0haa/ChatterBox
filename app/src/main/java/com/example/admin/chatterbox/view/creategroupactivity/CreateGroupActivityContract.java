package com.example.admin.chatterbox.view.creategroupactivity;

import com.example.admin.chatterbox.BasePresenter;
import com.example.admin.chatterbox.BaseView;


/**
 * Created by Admin on 10/11/2017.
 */

public interface CreateGroupActivityContract {

    interface View extends BaseView {
        void updateView(String groupNameAndPassword);


    }
    interface Presenter extends BasePresenter<CreateGroupActivityContract.View> {
        void validateGroupNameAndPassword(String groupName, String groupPassword);

    }
}
