package com.example.admin.chatterbox.view.joingroup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.chatterbox.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Admin on 10/15/2017.
 */

public class JoinGroupRecyclerViewAdapter extends RecyclerView.Adapter<JoinGroupRecyclerViewAdapter.ViewHolder> {

    private OnListInteractionListener gListener;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private FirebaseDatabase database;
    public DatabaseReference myRefUsers;
    private String groupId;
    private ListView rvList;
    private ArrayAdapter <String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();





    public JoinGroupRecyclerViewAdapter (String id, DatabaseReference myRefUsers){

        groupId = id;
        database = FirebaseDatabase.getInstance();
        myRefUsers = database.getReference("Groups");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();

                Set<String> set = new HashSet<String>();
                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // list_of_groups.notifyDataSetChanged();

    }

    @Override
    public JoinGroupRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_join_group, parent, false);
        return new ViewHolder(view);

    }



    @Override
    public void onBindViewHolder(JoinGroupRecyclerViewAdapter.ViewHolder holder, int position) {

        database = FirebaseDatabase.getInstance();
        myRefUsers = database.getReference("Groups");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();

                Set<String> set = new HashSet<String>();
                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View jView;
        public final TextView tvJoin;

        public ViewHolder (View view){
            super(view);
             jView = view;
             tvJoin = (TextView) view.findViewById(R.id.tvGroup);
        }

        @Override
        public String toString() {
            return super.toString() + " " + tvJoin.getText() + "";
        }

    }

    interface OnListInteractionListener{
        void OnListInteractionListener();
    }
}
