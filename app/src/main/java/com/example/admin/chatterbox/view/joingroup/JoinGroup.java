package com.example.admin.chatterbox.view.joingroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.injection.joingroup.DaggerJoinGroupComponent;
import com.example.admin.chatterbox.model.chat.Group;
import com.example.admin.chatterbox.view.groupactivity.GroupActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;

public class JoinGroup extends AppCompatActivity implements JoinGroupContract.View, JoinGroupRecyclerViewAdapter.OnListInteractionListener {
    private static final String TAG = "JoinGroupTag";

//    @BindView(R.id.rvJoin)
//    RecyclerView rvJoin;
//    @BindView(R.id.etJoin)
//    EditText etJoin;
//    @BindView(R.id.btnJoin)
//    ImageButton btnJoin;

    RecyclerView rvGroupList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;

    Button btnSearchGroup;
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        ButterKnife.bind(this);
        setupDaggerComponent();

        root = FirebaseDatabase.getInstance().getReference("Groups");

        btnSearchGroup = (Button) findViewById(R.id.btnJoin);
        rvGroupList = (RecyclerView) findViewById(R.id.rvJoinGroup);
        Log.d(TAG, "onCreate: " + root.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        JoinGroupRecyclerViewAdapter joinGroupRecyclerViewAdapter = new JoinGroupRecyclerViewAdapter(root, this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvGroupList.setLayoutManager(llm);
        rvGroupList.setAdapter(joinGroupRecyclerViewAdapter);
    }

    private void setupDaggerComponent() {
        DaggerJoinGroupComponent.create().inject(this);
    }

    @Override
    public void showError(String s) {
        Log.d(TAG, "showError: " + s);
    }

    @Override
    public void updateView(String groupName) {

    }

    @Override
    public void onListInteractionListener(Group mItem) {
        String id = mItem.getId();
        Intent intent = new Intent(this, GroupActivity.class);
        Log.d(TAG, "onListInteractionListener: " + id);
        intent.putExtra("ID", id);
        startActivity(intent);
    }
}
