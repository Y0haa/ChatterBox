package com.example.admin.chatterbox;

/**
 * Created by Admin on 10/10/2017.
 */

public interface BasePresenter <V extends BaseView> {

    void attacheView(V view);
    void detachView();
}
