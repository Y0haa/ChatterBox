package com.example.admin.chatterbox.injection.groupactivity;

import com.example.admin.chatterbox.view.groupactivity.GroupActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by admin on 10/11/2017.
 */

@Module
public class GroupActivityModule {

    @Provides
    GroupActivityPresenter getGroupActivityPresenter(){
        return new GroupActivityPresenter();
    }

}
