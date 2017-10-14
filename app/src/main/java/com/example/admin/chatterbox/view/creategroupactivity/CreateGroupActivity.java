package com.example.admin.chatterbox.view.creategroupactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.injection.creategroupactivity.DaggerCreateGroupActivityComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateGroupActivity extends AppCompatActivity implements CreateGroupActivityContract.View {

    private static final String TAG = "CreateGroupActivity";
    @Inject
    CreateGroupActivityPresenter presenter;
    @BindView(R.id.tvOutputString)
    TextView tvOutputString;
    @BindView(R.id.etInputGroupName)
    EditText etInputGroupName;
    @BindView(R.id.etInputGroupPassword)
    EditText etInputGroupPassword;
    @BindView(R.id.btnCreateGroup)
    Button btnCreateGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

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
        Toast.makeText(this, "Clicking", Toast.LENGTH_SHORT).show();
        presenter.validateGroupNameAndPassword(etInputGroupName.getText().toString(), etInputGroupPassword.getText().toString());


    }

    @Override
    public void updateView(String str) {
        tvOutputString.setText(str);
    }
}
