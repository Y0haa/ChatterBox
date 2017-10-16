package com.example.admin.chatterbox.injection.loginactivity;

import com.example.admin.chatterbox.view.loginactivity.registeruser.MainRegisterActivity;
import com.example.admin.chatterbox.view.loginactivity.signinuser.MainSignInActivity;

import dagger.Component;

/**
 * Created by admin on 10/11/2017.
 */


@Component(modules = LoginActivityModule.class)
public interface LoginActivityComponent {
    void inject(MainRegisterActivity activity);
    void inject(MainSignInActivity activity);

}
