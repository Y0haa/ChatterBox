package com.example.admin.chatterbox.injection.mainactivity;

import com.example.admin.chatterbox.view.mainactivity.MainActivity;

import dagger.Component;

/**
 * Created by Admin on 10/23/2017.
 */

@Component(modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);


}

