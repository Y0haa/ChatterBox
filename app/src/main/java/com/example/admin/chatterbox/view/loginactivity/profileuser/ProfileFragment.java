package com.example.admin.chatterbox.view.loginactivity.profileuser;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.injection.loginactivity.DaggerLoginActivityComponent;
import com.example.admin.chatterbox.model.chat.User;
import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.example.admin.chatterbox.util.customfonts.MyEditText;
import com.example.admin.chatterbox.view.loginactivity.MainLoginContract;
import com.example.admin.chatterbox.view.loginactivity.MainLoginPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Admin on 10/17/2017.
 */

public class ProfileFragment extends DialogFragment implements MainLoginContract.View {
    @BindView(R.id.image_profile)
    ImageView imageProfile;
    @BindView(R.id.name_edit)
    MyEditText nameEdit;
    @BindView(R.id.user_name)
    MyEditText userName;
    @BindView(R.id.password_edit)
    MyEditText passwordEdit;
    @BindView(R.id.email_edit)
    MyEditText emailEdit;
    @BindView(R.id.phone_number_edit)
    MyEditText phoneNumberEdit;
    Unbinder unbinder;
    @BindView(R.id.button_fragment_cancel)
    Button buttonFragmentCancel;
    @BindView(R.id.button_fragment_ok)
    Button buttonFragmentOk;
    private View view;

    @Inject
    MainLoginPresenter mActionsListener;


    private void setViewValues() {
        User user = CurrentStoredUser.getInstance().getUser();

        nameEdit.setText(user.getName());
        userName.setText(user.getUsername());
        passwordEdit.setText("*****");
        emailEdit.setText(user.getEmail());
        phoneNumberEdit.setText(user.getPhoneNumber());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_activity, container, true);
        unbinder = ButterKnife.bind(this, view);

        setViewValues();
        setupDagger();

        return view;
    }

    private void setupDagger() {
        DaggerLoginActivityComponent.create().inject(this);
        mActionsListener.attacheView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.image_profile)
    public void onViewClicked() {
        Toast.makeText(getContext(), "Something", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.button_fragment_cancel, R.id.button_fragment_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_fragment_cancel:
                Toast.makeText(getContext(), "cancel", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.button_fragment_ok:
                //Toast.makeText(getContext(), emailEdit.getText().toString(), Toast.LENGTH_SHORT).show();
                mActionsListener.updateFirebaseUser(createUserObject(), passwordEdit.getText().toString());
                break;
        }
    }

    private User createUserObject() {
        User user = new User();

        user.setId(CurrentStoredUser.getInstance().getUser().getId());
        user.setName(nameEdit.getText().toString());
        user.setUsername(userName.getText().toString());
        user.setEmail(emailEdit.getText().toString());
        user.setPhoneNumber(phoneNumberEdit.getText().toString());


        return user;
    }

    @Override
    public void showDialog(String title, String msg) {

    }

    @Override
    public void userSuccessful(String user) {

    }
}
