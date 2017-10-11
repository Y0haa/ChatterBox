package com.example.admin.chatterbox.view.mainactivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Admin on 10/10/2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {

    MainActivityContract.View view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    @Override
    public void attacheView(MainActivityContract.View view) {
        this.view = view;

    }

    @Override
    public void detachView() {

        this.view = null;
    }

    @Override
    public void validationInput(String inputString) {

        String outputString = "Test" + inputString;

        view.updateView(outputString);

    }
}
