package com.example.admin.chatterbox.injection.joingroup;


import com.example.admin.chatterbox.view.joingroup.JoinGroup;

import dagger.Component;

/**
 * Created by Admin on 10/15/2017.
 */



@Component(modules = JoinGroupModule.class)
public interface JoinGroupComponent {
    void inject(JoinGroup joinGroup);

}