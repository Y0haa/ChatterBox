package com.example.admin.chatterbox.view.joingroup;


import com.example.admin.chatterbox.view.joingroup.JoinGroupContract;

/**
 * Created by Admin on 10/15/2017.
 */




public class JoinGroupPresenter implements  JoinGroupContract.Presenter {

    JoinGroupContract.View view;

    @Override
    public void attacheView(JoinGroupContract.View view) { this.view = view;

    }

    @Override
    public void detachView() {
        this.view = null;

    }

    @Override
    public void validateGroupName(String uid, String groupName) {

    }
}
