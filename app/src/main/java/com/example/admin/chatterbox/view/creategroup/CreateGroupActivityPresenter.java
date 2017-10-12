package com.example.admin.chatterbox.view.creategroup;



/**
 * Created by Admin on 10/11/2017.
 */

public class CreateGroupActivityPresenter implements CreateGroupActivityContract.Presenter{

    CreateGroupActivityContract.View view;

    @Override
    public void attacheView(CreateGroupActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void validateGroupNameAndPassword(String groupName, String groupPassword) {

        String outputString =groupName + " " + groupPassword;
        view.updateView(outputString);
    }
}
