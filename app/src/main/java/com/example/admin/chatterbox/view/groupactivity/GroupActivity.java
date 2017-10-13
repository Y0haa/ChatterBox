package com.example.admin.chatterbox.view.groupactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.injection.groupactivity.DaggerGroupActivityComponent;
import com.example.admin.chatterbox.model.chat.Chat;
import com.example.admin.chatterbox.model.chat.User;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupActivity extends AppCompatActivity implements ChatRecyclerViewAdapter.OnListInteractionListener, GroupActivityContract.View {

    @BindView(R.id.rvChat)
    RecyclerView rvChat;
    @BindView(R.id.etMsg)
    EditText etMsg;
    @BindView(R.id.btnSend)
    ImageButton btnSend;

    @Inject
    GroupActivityPresenter presenter;
    private int mColumnCount;
    //private List<Chat> chatList;
    private ChatRecyclerViewAdapter mAdapter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        SetupDagger();

        presenter.attacheView(this);

        mColumnCount = 1;
        id = getIntent().getStringExtra("ID");

        presenter.findDatabaseReference();
        //Group group = presenter.getGroup(getIntent().getStringExtra("ID"));
        //chatList = group.getPosts();


        mAdapter = new ChatRecyclerViewAdapter(id, presenter.getDatabaseReference(), this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mColumnCount <= 1) {
            rvChat.setLayoutManager(new LinearLayoutManager(this));
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
        if (msg.length() > 0) {
            presenter.sendMessage(id, msg, new User(), 0l);
        }
    }

    @Override
    public void OnListInteraction(Chat mItem) {

    }

    @Override
    public void showError(String s) {

    }

    @Override
    public void updateInputMsg() {
        etMsg.setText("");
    }
}
