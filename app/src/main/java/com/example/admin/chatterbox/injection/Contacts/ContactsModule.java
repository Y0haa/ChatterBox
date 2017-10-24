package com.example.admin.chatterbox.injection.Contacts;

import com.example.admin.chatterbox.view.contacts.ContactsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Admin on 10/23/2017.
 */

@Module
public class ContactsModule {

    @Provides
    ContactsPresenter getContactsPresenter(){
        return new ContactsPresenter();
    }
}
