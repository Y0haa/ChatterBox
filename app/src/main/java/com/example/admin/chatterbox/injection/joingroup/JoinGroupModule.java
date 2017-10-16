package com.example.admin.chatterbox.injection.joingroup;


import com.example.admin.chatterbox.view.joingroup.JoinGroup;
import com.example.admin.chatterbox.view.joingroup.JoinGroupPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Admin on 10/15/2017.
 */


@Module
public class JoinGroupModule {

    @Provides
    JoinGroupPresenter getJoinGroupPresenter(){
        return new JoinGroupPresenter();
    }

}
