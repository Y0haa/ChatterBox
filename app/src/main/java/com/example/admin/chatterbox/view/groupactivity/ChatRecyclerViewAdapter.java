package com.example.admin.chatterbox.view.groupactivity;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.glide.GlideApp;
import com.example.admin.chatterbox.model.chat.Chat;
import com.example.admin.chatterbox.util.Commands;
import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewTag";
    private final List<DataSnapshot> mValues;
    private final List<Chat> chats;
    private final OnListInteractionListener mListener;
    private final DatabaseReference databaseReference;
    private final DatabaseReference databaseReferenceUsers;
    private final String groupId;
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG, "onChildAdded: added");
            //mValues.add(dataSnapshot);
            chats.add(dataSnapshot.getValue(Chat.class));
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
    private Context context;

    public ChatRecyclerViewAdapter(String id, DatabaseReference databaseReference, OnListInteractionListener listener,
                                   Context context) {
        groupId = id;
        this.databaseReference = databaseReference.child("Groups").child(id).child("messages");
        this.databaseReferenceUsers = databaseReference.child("users");
        Log.d(TAG, "ChatRecyclerViewAdapter: " + databaseReference.toString());
        this.databaseReference.addChildEventListener(childEventListener);
        mValues = new ArrayList<>();
        chats = new ArrayList<>();
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //holder.mItem = getItem(position);
        holder.mItem = chats.get(position);
        HashMap<String, String> owner;

        databaseReferenceUsers.orderByChild("id").equalTo(holder.mItem.getOwnerId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onBindViewHolder: " + holder.mItem.getOwnerId());

                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {

                            if (ds.getValue() != null) {
                                //Log.d(TAG, "onDataChange: " + dataSnapshot.getValue().toString());
                                HashMap<String, String> owner = (HashMap<String, String>) ds.getValue();
                                //owner.toString();
                                Log.d(TAG, "onDataChange: image " + owner.get("userImage"));
                                if (owner.get("userImage") != null)
                                    GlideApp.with(context).load(owner.get("userImage")).into(holder.ivImage);
                                else
                                    GlideApp.with(context).load(R.drawable.shotgunsamurai).into(holder.ivImage);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.tvMsg.getLayoutParams();
        RelativeLayout.LayoutParams lpGif = (RelativeLayout.LayoutParams) holder.ivGif.getLayoutParams();
        String message = "";
        if (holder.mItem != null) {
            if (holder.mItem.getOwner() != null) {
                Log.d(TAG, "onBindViewHolder: " + CurrentStoredUser.getInstance().getUser().getName());
                if (holder.mItem.getOwner().compareTo(CurrentStoredUser.getInstance().getUser().getName()) == 0) {
                    holder.tvMsg.setBackgroundResource(R.drawable.me_speechbubble);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        holder.tvMsg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        holder.ivImage.setVisibility(ImageView.INVISIBLE);
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lpGif.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lp.removeRule(RelativeLayout.RIGHT_OF);
                        lpGif.removeRule(RelativeLayout.RIGHT_OF);
                        holder.cvImg.setVisibility(CardView.INVISIBLE);
                    }
                } else {
                    holder.tvMsg.setBackgroundResource(R.drawable.you_speechbubble);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        holder.tvMsg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        holder.cvImg.setVisibility(CardView.VISIBLE);
                        holder.ivImage.setVisibility(ImageView.VISIBLE);
                        lp.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lp.addRule(RelativeLayout.RIGHT_OF, R.id.cvAuthorImg);
                        lpGif.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lpGif.addRule(RelativeLayout.RIGHT_OF, R.id.cvAuthorImg);
                        message = holder.mItem.getOwner() + "\n";
                    }
                }

                //holder.tvAuthor.setText(holder.mItem.getOwner());
            }

            //PABLO IF YOU WANT TO USE THIS CODE ADD IT TO THE doCommand METHOD
            //WE CAN DISCUSS HEIGHT LIMITS LATER

            /*//Getting images that users posted
            if (URLUtil.isValidUrl(holder.mItem.getPost())) {
                holder.ivGif.setVisibility(View.VISIBLE);
                holder.tvMsg.setVisibility(View.INVISIBLE);
                holder.ivGif.getLayoutParams().height = 300;
                Glide.with(context).load(holder.mItem.getPost()).into(holder.ivGif);
            }*/


            if (holder.mItem.getPost().charAt(0) != '/') {
                holder.tvMsg.setVisibility(View.VISIBLE);
                holder.tvMsg.setLayoutParams(lp);
                message += holder.mItem.getPost();
                holder.tvMsg.setText(message);
                holder.ivGif.setVisibility(View.GONE);
            } else if (URLUtil.isValidUrl(holder.mItem.getPost())) {
                holder.ivGif.setVisibility(View.VISIBLE);
                holder.tvMsg.setVisibility(View.INVISIBLE);
                //holder.ivGif.getLayoutParams().height = 300;
                Glide.with(context).load(holder.mItem.getPost()).into(holder.ivGif);
            } else {
                holder.ivGif.setVisibility(View.VISIBLE);
                holder.tvMsg.setVisibility(View.INVISIBLE);
                String cmd = holder.mItem.getPost().substring(1);
                doCommand(holder, cmd);
            }
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

    private void doCommand(ViewHolder holder, String cmd) {
        String args = cmd.substring(cmd.indexOf(' ') + 1);
        if (cmd.contains(" ")) {
            cmd = cmd.substring(0, cmd.indexOf(' '));
        }
        switch (Commands.valueOf(cmd.toUpperCase())) {
            case YOKO:
                Glide.with(context).asGif().load(R.drawable.yoko).into(holder.ivGif);
                break;

            case GIPHY:
                Glide.with(context).asGif().load(args).into(holder.ivGif);
                break;
        }
    }

    private Chat getItem(int position) {
        DataSnapshot snapShot = mValues.get(position);
        Log.d(TAG, "getItem: " + snapShot.toString());
        return snapShot.getValue(Chat.class);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void addSystemMsg(String msg) {
        Chat chat = new Chat(msg, CurrentStoredUser.getInstance().getUser().getName(), "0", 0l);
        chats.add(chat);
        notifyDataSetChanged();
        mListener.onListUpdate();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView tvAuthor;
        public final TextView tvMsg;
        private final ImageView ivImage;
        private final ImageView ivGif;
        public Chat mItem;
        private final CardView cvImg;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            cvImg = view.findViewById(R.id.cvAuthorImg);
            //tvAuthor = view.findViewById(R.id.tvAuthor);
            tvMsg = view.findViewById(R.id.tvMessage);
            ivImage = view.findViewById(R.id.ivAuthorImg);
            ivGif = view.findViewById(R.id.ivGif);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvMsg.getText() + "'";
        }
    }

    interface OnListInteractionListener {
        void OnListInteraction(Chat mItem);

        void onListUpdate();
    }
}
