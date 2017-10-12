package com.example.admin.chatterbox.injection.creategroupactivity;

import com.example.admin.chatterbox.view.creategroup.CreateGroupActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Admin on 10/12/2017.
 */


@Module
public class CreateGroupActivityModule {

    @Provides
    CreateGroupActivityPresenter getCreateGroupActivityPresenter(){
        return new CreateGroupActivityPresenter();
    }



}