package com.example.admin.chatterbox.view.contacts;

/**
 * Created by Admin on 10/23/2017.
 */

public class ContactsPresenter implements ContactsContract.Presenter {

    ContactsContract.View view;

    @Override
    public void attacheView(ContactsContract.View view) {
        this.view= view;
    }

    @Override
    public void detachView() {
        this.view= null;
    }

    @Override
    public void validateContactName(String uid, String contactName) {

    }
}
