package com.example.admin.chatterbox.view.creategroupactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.injection.creategroupactivity.DaggerCreateGroupActivityComponent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateGroupActivity extends AppCompatActivity implements CreateGroupActivityContract.View {

    private static final String TAG = "CreateGroupActivity";
    private String uid;
    private FirebaseDatabase database;
    private DatabaseReference myRefUsers;
    @Inject
    CreateGroupActivityPresenter presenter;
    @BindView(R.id.tvOutputString)
    TextView tvOutputString;
    @BindView(R.id.etInputGroupName)
    EditText etInputGroupName;
    @BindView(R.id.btnCreateGroup)
    Button btnCreateGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);


        //-------------
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();

           // tvOutputString.setText(uid.toString());
            Log.d(TAG, "onCreate: "+uid);
         //   Toast.makeText(this, ""+user.getUid(), Toast.LENGTH_SHORT).show();
        }
        database = FirebaseDatabase.getInstance();
        myRefUsers = database.getReference("Groups");




        //---------


        ButterKnife.bind(this);
        setupDaggerComponent();
        presenter.attacheView(this);
    }

    private void setupDaggerComponent() {
        DaggerCreateGroupActivityComponent.create().inject(this);
    }

    @Override
    public void showError(String s) {
        Log.d(TAG, "showError: " + s);
    }

    public void createGroupClicks(View view) {
        Log.d(TAG, "createGroupClicks: "+ uid.toString());
        presenter.validateGroupName(uid, etInputGroupName.getText().toString());


    }

    @Override
    public void updateView(String createdGroupStatus) {
        tvOutputString.setText(createdGroupStatus);
    }
}
