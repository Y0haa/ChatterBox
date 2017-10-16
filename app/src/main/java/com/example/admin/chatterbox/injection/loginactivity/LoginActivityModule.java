package com.example.admin.chatterbox.injection.loginactivity;


import com.example.admin.chatterbox.view.loginactivity.MainLoginPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by admin on 10/11/2017.
 */

@Module
public class LoginActivityModule {

    @Provides
    MainLoginPresenter getLoginActivityPresenter(){
        return new MainLoginPresenter();
    }
}
