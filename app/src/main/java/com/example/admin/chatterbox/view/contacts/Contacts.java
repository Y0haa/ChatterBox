package com.example.admin.chatterbox.view.contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.injection.Contacts.DaggerContactsComponent;
import com.example.admin.chatterbox.model.chat.User;
import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.example.admin.chatterbox.view.groupactivity.GroupActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;

public class Contacts extends AppCompatActivity implements ContactsContract.View, ContactsRecyclerViewAdapter.OnListInteractionListener {


    RecyclerView rvContactsList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;
    SearchView etSearchContactsView;

//    Button btnSearchGroup;
    private DatabaseReference root;
    private String TAG = "Contacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ButterKnife.bind(this);
        setupDaggerComponent();


        root = FirebaseDatabase.getInstance().getReference("users");

        rvContactsList = (RecyclerView) findViewById(R.id.rvContacts);
        Log.d(TAG, "onCreate: " + root.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();

        ContactsRecyclerViewAdapter contactsRecyclerViewAdapter = new ContactsRecyclerViewAdapter(root, this);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        rvContactsList.setLayoutManager(llm);
        rvContactsList.setAdapter(contactsRecyclerViewAdapter);


    }

    private void setupDaggerComponent() {
        DaggerContactsComponent.create().inject(this);

    }


    @Override
    public void showError(String s) {

        Log.d(TAG, "showError: " + s);
    }

    @Override
    public void updateView(String contactName) {


    }

    @Override
    public void onListInteractionListener(User mItem) {

        String id = mItem.getId();

        Intent intent = new Intent(this, GroupActivity.class);
        Log.d(TAG, "onListInteractionListener: " + id);


        String myId = CurrentStoredUser.getInstance().getUser().getId();//you
       String theirId =  mItem.getId();//the other one

        String theirEmail = mItem.getEmail();
        String myEmail = CurrentStoredUser.getInstance().getUser().getEmail();//you

        String CombindId;

        if(myEmail.compareTo(theirEmail) > 0 ){
            CombindId = myId+theirId;
        }
        else {
            CombindId = theirId+myId;
        }

        intent.putExtra("ID", CombindId);

        startActivity(intent);

    }
}
