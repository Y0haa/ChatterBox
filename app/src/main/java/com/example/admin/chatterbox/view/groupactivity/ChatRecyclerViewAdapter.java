package com.example.admin.chatterbox.view.groupactivity;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.model.chat.Chat;
import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewTag";
    private final List<DataSnapshot> mValues;
    private final OnListInteractionListener mListener;
    private final DatabaseReference databaseReference;
    private final String groupId;
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG, "onChildAdded: added");
            mValues.add(dataSnapshot);
            notifyDataSetChanged();
            mListener.onListUpdate();
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

    public ChatRecyclerViewAdapter(String id, DatabaseReference databaseReference, OnListInteractionListener listener) {
        groupId = id;
        this.databaseReference = databaseReference.child("Groups").child(id).child("messages");
        Log.d(TAG, "ChatRecyclerViewAdapter: " +databaseReference.toString());
        this.databaseReference.addChildEventListener(childEventListener);
        mValues = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = getItem(position);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.tvMsg.getLayoutParams();

        if(holder.mItem != null) {
            if(holder.mItem.getOwner() != null) {
                Log.d(TAG, "onBindViewHolder: " + CurrentStoredUser.getInstance().getUser().getName());
                if(holder.mItem.getOwner().compareTo(CurrentStoredUser.getInstance().getUser().getName()) == 0) {
                    holder.tvMsg.setBackgroundResource(R.drawable.me_speechbubble);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        holder.tvMsg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    }
                }else{
                    holder.tvMsg.setBackgroundResource(R.drawable.you_speechbubble);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        holder.tvMsg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        lp.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    }
                }
                holder.tvAuthor.setText(holder.mItem.getOwner());
            }
            holder.tvMsg.setLayoutParams(lp);
            holder.tvMsg.setText(holder.mItem.getPost());
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.OnListInteraction(holder.mItem);
                }
            }
        });
    }

    private Chat getItem(int position) {
        DataSnapshot snapShot = mValues.get(position);
        Log.d(TAG, "getItem: " + snapShot.toString());
        return snapShot.getValue(Chat.class);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvAuthor;
        public final TextView tvMsg;
        private final ImageView ivImage;
        public Chat mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
            tvMsg = (TextView) view.findViewById(R.id.tvMessage);
            ivImage = view.findViewById(R.id.ivAuthorImg);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvMsg.getText() + "'";
        }
    }

    interface OnListInteractionListener{
        void OnListInteraction(Chat mItem);
        void onListUpdate();
    }
}
