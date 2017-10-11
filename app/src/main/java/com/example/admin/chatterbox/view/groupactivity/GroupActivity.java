package com.example.admin.chatterbox.view.groupactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.injection.groupactivity.DaggerGroupActivityComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupActivity extends AppCompatActivity {

    @BindView(R.id.rvChat)
    RecyclerView rvChat;
    @BindView(R.id.etMsg)
    EditText etMsg;
    @BindView(R.id.btnSend)
    ImageButton btnSend;

    @Inject
    GroupActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        SetupDagger();



    }

    private void SetupDagger() {
        DaggerGroupActivityComponent.create().inject(this);
    }

    @OnClick(R.id.btnSend)
    public void onViewClicked() {

    }
}
