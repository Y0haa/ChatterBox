package com.example.admin.chatterbox.view.mainactivity;

import com.example.admin.chatterbox.BasePresenter;
import com.example.admin.chatterbox.BaseView;

/**
 * Created by Admin on 10/10/2017.
 */

public interface MainActivityContract {

    interface View extends BaseView{
        void updateView(String s);
    }

    interface Presenter extends BasePresenter<View>{
        void validationInput(String inputString);
    }
}
