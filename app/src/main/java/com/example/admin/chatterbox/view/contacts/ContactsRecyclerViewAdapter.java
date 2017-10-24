package com.example.admin.chatterbox.view.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.model.chat.Group;
import com.example.admin.chatterbox.model.chat.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/23/2017.
 */

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder>{

    private final OnListInteractionListener listener;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private List<DataSnapshot> mValues;

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mValues.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    public ContactsRecyclerViewAdapter(DatabaseReference root, OnListInteractionListener listener){
        mValues = new ArrayList<>();
        this.root = root;
        this.listener = listener;
        this.root.addChildEventListener(childEventListener);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_contacts, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ContactsRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.mItem= getItem(position);
        holder.tvContactsTitle.setText(holder.mItem.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null ){
                    listener.onListInteractionListener(holder.mItem);
                }
            }
        });


    }

    private User getItem(int position) {
        DataSnapshot snapShot = mValues.get(position);
        String id = snapShot.getKey();
        User tmp = snapShot.getValue(User.class);
        tmp.setId(id);
        return tmp;
    }

    @Override
    public int getItemCount() { return mValues.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View cView;
        private final TextView tvContactsTitle;
        public User mItem;


        public ViewHolder (View view){
            super(view);
            cView = view;
            //tvJoin = (TextView) view.findViewById(R.id.tvGroup);
            tvContactsTitle = view.findViewById(R.id.tvContactsTitle);
        }

        @Override
        public String toString() {
            return super.toString() + " " + "";
        }

    }

    interface OnListInteractionListener {
        void onListInteractionListener(User mItem);
    }
}
