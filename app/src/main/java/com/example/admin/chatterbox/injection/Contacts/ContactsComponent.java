package com.example.admin.chatterbox.injection.Contacts;

import com.example.admin.chatterbox.view.contacts.Contacts;

import dagger.Component;

/**
 * Created by Admin on 10/23/2017.
 */

@Component(modules = ContactsModule.class)
public interface ContactsComponent {
    void inject (Contacts contacts);

}
