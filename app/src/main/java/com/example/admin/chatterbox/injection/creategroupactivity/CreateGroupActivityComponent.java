package com.example.admin.chatterbox.injection.creategroupactivity;

import com.example.admin.chatterbox.view.creategroupactivity.CreateGroupActivity;

import dagger.Component;

/**
 * Created by Admin on 10/12/2017.
 */



@Component(modules = CreateGroupActivityModule.class)
public interface CreateGroupActivityComponent {
    void inject(CreateGroupActivity createGroupActivity);


}