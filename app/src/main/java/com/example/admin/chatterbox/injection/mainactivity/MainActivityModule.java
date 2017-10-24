package com.example.admin.chatterbox.injection.mainactivity;

import com.example.admin.chatterbox.view.mainactivity.MainActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Admin on 10/23/2017.
 */


@Module
public class MainActivityModule {

    @Provides
    MainActivityPresenter getMainActivityPresenter(){
        return new MainActivityPresenter();
    }



}