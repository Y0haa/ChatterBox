package com.example.admin.chatterbox.view.groupactivity;


import com.example.admin.chatterbox.BasePresenter;
import com.example.admin.chatterbox.BaseView;

/**
 * Created by admin on 10/11/2017.
 */

public class GroupActivityContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {

        void sendMessage();
    }

}
