package com.example.admin.chatterbox.view.groupactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupActivity extends AppCompatActivity implements ChatRecyclerViewAdapter.OnListInteractionListener, GroupActivityContract.View {
    public static final String TAG = "GroupActivity";
    private static final int RESULT_LOAD_IMG = 1;
    Effectstype effect;
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
    NiftyDialogBuilder dialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
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
                .withDuration(700)                                          //def
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
                Toast.makeText(this, "File TEST ", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
              //  btnSend.setImageBitmap(selectedImage);

                // Progress Dialog start
                pd.show();

                // Get filename of Image
                Cursor returnCursor =
                        getContentResolver().query(imageUri, null, null, null, null);

                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();

                presenter.uploadImage(imageUri,
                        returnCursor.getString(nameIndex));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
               // Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "You haven't picked a valid Image", Toast.LENGTH_LONG).show();
        }
    }
}
