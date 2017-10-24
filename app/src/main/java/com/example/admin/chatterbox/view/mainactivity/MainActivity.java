package com.example.admin.chatterbox.view.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.injection.mainactivity.DaggerMainActivityComponent;
import com.example.admin.chatterbox.view.contacts.Contacts;
import com.example.admin.chatterbox.view.groupactivity.GroupActivity;
import com.example.admin.chatterbox.view.joingroup.JoinGroup;
import com.example.admin.chatterbox.view.loginactivity.profileuser.ProfileFragment;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    private static final String TAG = "MainActivityTag";
    Button btnCreate, btnJoin;

    // @BindView(R.id.etCreateGroup)
    // EditText etCreateGroup;
    private Effectstype effect;
    @Inject
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.setContentView(R.layout.activity_main);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ButterKnife.bind(this);
        setupDaggerComponent();
        presenter.attacheView(this);
    }

    private void setupDaggerComponent() {
        DaggerMainActivityComponent.create().inject(this);
    }

    public void GroupButton(View view) {

        switch (view.getId()) {

            case R.id.btnCreate:
             //   Toast.makeText(this, "createggroup click", Toast.LENGTH_SHORT).show();
                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
                effect = Effectstype.Fadein;

                dialogBuilder
                        .withTitle("Create a group")                                  //.withTitle(null)  no title
                        .withTitleColor("#ffffff")                                  //def
                       // .withDividerColor("#33ebf4")                              //def
                        .withMessage(null)                     //.withMessage(null)  no Msg
                        .withMessageColor("#33ebf4")                              //def  | withMessageColor(int resid)
                        .withDialogColor("#770a7f")                               //def  | withDialogColor(int resid)                               //def
                        //  .withIcon(getResources().getDrawable(R.drawable.icon))
                        .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                        .withDuration(700)                                          //def
                        .withEffect(effect)                                         //def Effectstype.Slidetop
                        .withButton1Text("OK")                                      //def gone
                        .withButton2Text("Cancel")                                  //def gone
                        .setCustomView(R.layout.custom_create_group, view.getContext())         //.setCustomView(View or ResId,context)
                        .setButton1Click(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // Log.d(TAG, "createGroupClicks: " + uid.toString());
                                //   presenter.validateGroupName(etCreateGroup.getText().toString());

                                //    Toast.makeText(v.getContext(), etCreateGroup.getText(), Toast.LENGTH_SHORT).show();

                              //  Button button = (Button) dialog.findViewById(R.id.dialog_ok);
                                EditText etCreateGroup = (EditText) dialogBuilder.findViewById(R.id.etCreateGroup);
                                if(etCreateGroup.getText().toString().equals("")){
                                    Toast.makeText(MainActivity.this, "Type a group name", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    presenter.createChatGroup(etCreateGroup.getText().toString());
                                    dialogBuilder.dismiss();
                                }
                                  //      Toast.makeText(MainActivity.this, etCreateGroup.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                               // Toast.makeText(v.getContext(), "i'm btn2", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
//----------------------

                //    Intent createIntent = new Intent(this, CreateGroupActivity.class);
                //   startActivity(createIntent);

                break;
            case R.id.btnJoin:

                Intent joinIntent = new Intent(this, JoinGroup.class);
                startActivity(joinIntent);

                break;
            case R.id.btnContacts:
                Intent contactIntent = new Intent(this, Contacts.class);
                startActivity(contactIntent);


        }
    }

    public void ProfileButton(View view) {
        FragmentManager fm = getSupportFragmentManager();
        ProfileFragment alertdFragment = new ProfileFragment();
        alertdFragment.show(fm, "Profile");
    }

    @Override
    public void showError(String s) {

    }

    @Override
    public void updateOnCreateChatGroup(String groupToken ) {
        if(groupToken.equals("noToken")){
            Toast.makeText(this, "Unable to create group", Toast.LENGTH_SHORT).show();
        }
        else {

            Intent groupActivityIntent = new Intent(this, GroupActivity.class);
            groupActivityIntent.putExtra("ID", groupToken);
            startActivity(groupActivityIntent);
        }


       // Toast.makeText(this, groupStatus, Toast.LENGTH_SHORT).show();
    }

    public void createGroupClick(View view) {


    }
}
