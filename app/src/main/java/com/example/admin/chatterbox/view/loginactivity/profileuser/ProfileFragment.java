package com.example.admin.chatterbox.view.loginactivity.profileuser;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.glide.GlideApp;
import com.example.admin.chatterbox.injection.loginactivity.DaggerLoginActivityComponent;
import com.example.admin.chatterbox.model.chat.User;
import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.example.admin.chatterbox.util.Utility;
import com.example.admin.chatterbox.util.customfonts.MyEditText;
import com.example.admin.chatterbox.view.loginactivity.MainLoginContract;
import com.example.admin.chatterbox.view.loginactivity.MainLoginPresenter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Admin on 10/17/2017.
 */

public class ProfileFragment extends DialogFragment implements MainLoginContract.View {
    @BindView(R.id.image_profile)
    CircleImageView imageProfile;
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
    @BindView(R.id.current_password_edit)
    MyEditText currentPasswordEdit;
    private View view;

    @Inject
    MainLoginPresenter mActionsListener;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;


    private void setViewValues() {
        try {
            mActionsListener.verifyOrCreateIfUserExistOnDB(CurrentStoredUser.getInstance().getUser().getId());
            User user = CurrentStoredUser.getInstance().getUser();

            nameEdit.setText(user.getName());
            userName.setText(user.getUsername());
            //passwordEdit.setText("*****");
            emailEdit.setText(user.getEmail());
            phoneNumberEdit.setText(user.getPhoneNumber());
            GlideApp.with(getContext()).load(user.getUserImage()).into(imageProfile);
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_activity, container, true);
        unbinder = ButterKnife.bind(this, view);

        setupDagger();
        setViewValues();

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
        selectImage();

        //imageProfile.setImageResource(R.mipmap.ic_launcher);
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

                if (passwordEdit.getText().length() > 0) {
                    if (passwordEdit.getText().length() < 6) {
                        Toast.makeText(getContext(), "Password lenght must be at least 6", Toast.LENGTH_SHORT).show();
                    }else{
                        if(currentPasswordEdit.getText().length()<0){
                            Toast.makeText(getContext(), "Must type current password", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            CurrentStoredUser.getInstance().setPassword(currentPasswordEdit.getText().toString());
                        }
                    }
                }
                mActionsListener.updateFirebaseUser(createUserObject(), passwordEdit.getText().toString());
                Toast.makeText(getContext(), "Record updated", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
        }
    }

    private User createUserObject() {

        User user = new User();
        try {
            user.setId(CurrentStoredUser.getInstance().getUser().getId());
            user.setName(nameEdit.getText().toString());
            user.setUsername(userName.getText().toString());
            user.setEmail(emailEdit.getText().toString());
            user.setPhoneNumber(phoneNumberEdit.getText().toString());
            user.setUserImage(CurrentStoredUser.getInstance().getUser().getUserImage());
        } catch (Exception e) {

        }
        return user;
    }

    @Override
    public void showDialog(String title, String msg) {

    }

    @Override
    public void userSuccessful(String user) {
        GlideApp.with(getContext()).load(CurrentStoredUser.getInstance().getUser().getUserImage()).into(imageProfile);

    }

    @Override
    public void userSuccessful(Object value) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //imageProfile.setImageBitmap(thumbnail);
        mActionsListener.savePictureProfile(data.getData(), CurrentStoredUser.getInstance().getUser().getId());
        //      ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //imageProfile.setImageBitmap(bm);
        mActionsListener.savePictureProfile(data.getData(), CurrentStoredUser.getInstance().getUser().getId());


//        ivImage.setImageBitmap(bm);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }


}
