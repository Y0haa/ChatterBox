package com.example.admin.chatterbox.view.joingroup;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.model.chat.Group;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/15/2017.
 */

public class JoinGroupRecyclerViewAdapter extends RecyclerView.Adapter<JoinGroupRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "JoinGroupTag";
    private final OnListInteractionListener listener;
    private OnListInteractionListener gListener;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    public DatabaseReference myRefUsers;
    private String groupId;
    private List<DataSnapshot> mValues;

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG, "onChildAdded: " + dataSnapshot.getValue().toString());

            try{
                Group tmp = dataSnapshot.getValue(Group.class);
                if(tmp.getTitle() != null && tmp.getTitle().compareTo("") != 0) {
                    mValues.add(dataSnapshot);
                    notifyDataSetChanged();
                }
            }
            catch (Exception e){

            }
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

    public JoinGroupRecyclerViewAdapter(DatabaseReference root, OnListInteractionListener listener){
        mValues = new ArrayList<>();
        this.root = root;
        this.listener = listener;
        this.root.addChildEventListener(childEventListener);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_join_group, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final JoinGroupRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.mItem = getItem(position);
        holder.tvGroupTitle.setText(holder.mItem.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onListInteractionListener(holder.mItem);
                }
            }
        });

    }

    private Group getItem(int position) {
        DataSnapshot snapShot = mValues.get(position);
        String id = snapShot.getKey();
        Group tmp = snapShot.getValue(Group.class);
        tmp.setId(id);
        return tmp;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View jView;
        private final TextView tvGroupTitle;
        public Group mItem;
        //public final TextView tvJoin;

        public ViewHolder (View view){
            super(view);
             jView = view;
             //tvJoin = (TextView) view.findViewById(R.id.tvGroup);
            tvGroupTitle = view.findViewById(R.id.tvGroupTitle);
        }

        @Override
        public String toString() {
            return super.toString() + " " + "";
        }

    }

    interface OnListInteractionListener{
        void onListInteractionListener(Group mItem);
    }
}
