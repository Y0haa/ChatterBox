package com.example.admin.chatterbox.view.joingroup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.injection.joingroup.DaggerJoinGroupComponent;
import com.example.admin.chatterbox.view.groupactivity.GroupActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;

public class JoinGroup extends AppCompatActivity implements JoinGroupContract.View   {
    private static final String TAG = "JoinGroup";

//    @BindView(R.id.rvJoin)
//    RecyclerView rvJoin;
//    @BindView(R.id.etJoin)
//    EditText etJoin;
//    @BindView(R.id.btnJoin)
//    ImageButton btnJoin;

    RecyclerView rvPersonList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;

    Button btnJoinGroup;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private FirebaseDatabase database;
    private DatabaseReference myRefUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        btnJoinGroup = (Button) findViewById(R.id.btnJoin);



        database = FirebaseDatabase.getInstance();
        myRefUsers = database.getReference("Groups");

        btnJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Map<String,Object> map = new HashMap<String, Object>();
//                map.put(myRefUsers.getKey().toString(),"");
//                root.updateChildren(map);

               Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
                intent.putExtra("Group",((myRefUsers).getDatabase().toString()));

            }


        });
        groupFiller();
        ButterKnife.bind(this);
        setupDaggerComponent();
        //presenter.attacheView(this);
    }

    private void setupDaggerComponent() {
        DaggerJoinGroupComponent.create().inject(this);
    }

    @Override
    public void showError(String s) {
        Log.d(TAG, "showError: " + s);
    }

    public void groupFiller(){




    }

    @Override
    public void updateView(String groupName) {

    }
}
