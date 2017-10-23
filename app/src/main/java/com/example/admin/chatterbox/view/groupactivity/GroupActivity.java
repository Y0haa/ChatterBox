package com.example.admin.chatterbox.view.groupactivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.injection.groupactivity.DaggerGroupActivityComponent;
import com.example.admin.chatterbox.model.chat.Chat;
import com.example.admin.chatterbox.util.KeyContract;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupActivity extends AppCompatActivity implements ChatRecyclerViewAdapter.OnListInteractionListener, GroupActivityContract.View {
    public static final String TAG = "GroupActivity";
    private static final int RESULT_LOAD_IMG = 1;
    private static final int PICKFILE_REQUEST_CODE = 2;
    Effectstype effect;
    NiftyDialogBuilder dialogBuilder;
    @BindView(R.id.rvChat)
    RecyclerView rvChat;
    @BindView(R.id.etMsg)
    EditText etMsg;
    @BindView(R.id.btnSend)
    ImageButton btnSend;
    @Inject
    GroupActivityPresenter presenter;
    @BindView(R.id.btnSendFile)
    ImageButton btnSendFile;
    private int mColumnCount;
    //private List<Chat> chatList;
    private ChatRecyclerViewAdapter mAdapter;
    private String id;
    ProgressDialog pd;
    private int STORAGE_PERMISSION_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        KeyContract.setUpKey(this);

        ButterKnife.bind(this);

        SetupDagger();

        presenter.attacheView(this);

        mColumnCount = 1;
        id = getIntent().getStringExtra("ID");
        presenter.setGroupId(id);

        presenter.findDatabaseReference();
        //Group group = presenter.getGroup(getIntent().getStringExtra("ID"));
        //chatList = group.getPosts();


        mAdapter = new ChatRecyclerViewAdapter(id, presenter.getDatabaseReference(), this, this);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

        //----------permissions
        if(isReadStorageAllowed()){

            //Existing the method with return
            return;
        }

        //If the app has not the permission then asking for the permission
        requestStoragePermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mColumnCount <= 1) {
            LinearLayoutManager llm = new LinearLayoutManager(this);
            //llm.setReverseLayout(true);
            llm.setStackFromEnd(true);
            rvChat.setLayoutManager(llm);
        } else {
            rvChat.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }

        rvChat.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void SetupDagger() {
        DaggerGroupActivityComponent.create().inject(this);
    }

    @OnClick(R.id.btnSend)
    public void onViewClicked() {
        String msg = etMsg.getText().toString();
        msg = msg.trim();
        if (msg.length() > 0) {
            if (msg.charAt(0) == '/') {
                presenter.checkCommand(msg);
            } else {
                presenter.sendMessage(msg, 0l);
            }
        }
    }

    @OnClick(R.id.btnSendFile)
    public void onSendFileCliked() {
        //Display User's dialog for uploads
        uploadDialog();

    }

    @Override
    public void OnListInteraction(Chat mItem) {

    }

    @Override
    public void onListUpdate() {
        rvChat.scrollToPosition(rvChat.getAdapter().getItemCount() - 1);
    }

    @Override
    public void showError(String s) {

    }

    @Override
    public void updateInputMsg() {
        etMsg.setText("");
        rvChat.scrollToPosition(rvChat.getAdapter().getItemCount() - 1);
    }

    @Override
    public void updateOnSendImage(String something) {
        pd.dismiss();
        dialogBuilder.dismiss();

      //  Toast.makeText(this, something, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendSystemMsg(String msg) {
        mAdapter.addSystemMsg(msg);
    }

    public void uploadDialog(){



        effect = Effectstype.RotateBottom;
        dialogBuilder=NiftyDialogBuilder.getInstance(this);

        dialogBuilder
                .withTitle(null)                                  //.withTitle(null)  no title
                .withMessage(null)                     //.withMessage(null)  no Msg
                .withDialogColor("#C1C8E4")                               //def  | withDialogColor(int resid)                               //def
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(400)                                          //def
                .withEffect(effect)                                         //def Effectstype.Slidetop
                .setCustomView(R.layout.custom_upload_dialog, this)         //.setCustomView(View or ResId,context)
                .show();
    }

    public void dialogClicking(View view) {
        switch (view.getId()) {
            case R.id.btnDialogUploadImage:

                // Allow use to select Image
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);


                break;
            case R.id.btnDialogUploadFile:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword","application/pdf"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);

                break;
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
                final Uri fileUri = data.getData();

                pd.show();

                // Get filename of Image
                Cursor returnCursor =
                        getContentResolver().query(fileUri, null, null, null, null);

                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();


            if(reqCode == 1)
                    presenter.uploadAFile(fileUri,
                        returnCursor.getString(nameIndex),
                            "UPLOADED_IMAGE");

            else if ( reqCode == 2)
                presenter.uploadAFile(fileUri,
                        returnCursor.getString(nameIndex),
                        "UPLOADED_DOCUMENT");

        } else {
            Toast.makeText(this, "You haven't picked a valid Image", Toast.LENGTH_LONG).show();
        }
    }

    //-----------------permissions
    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast

            }else{
                //Displaying another toast if permission is not granted

            }
        }
    }
}
