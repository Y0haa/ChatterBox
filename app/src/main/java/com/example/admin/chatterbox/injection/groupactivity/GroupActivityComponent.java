package com.example.admin.chatterbox.injection.groupactivity;

import com.example.admin.chatterbox.view.groupactivity.GroupActivity;

import dagger.Component;

/**
 * Created by admin on 10/11/2017.
 */


@Component(modules = GroupActivityModule.class)
public interface GroupActivityComponent {
    void inject(GroupActivity groupActivity);

}
