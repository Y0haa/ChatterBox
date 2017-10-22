package com.example.admin.chatterbox.view.groupactivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.example.admin.chatterbox.download.DownloadTask;
import com.example.admin.chatterbox.model.chat.Chat;
import com.example.admin.chatterbox.util.Commands;
import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewTag";
    private final List<DataSnapshot> mValues;
    private final List<Chat> chats;
    private final OnListInteractionListener mListener;
    private final DatabaseReference databaseReference;
    private final String groupId;
    Effectstype effect;
    NiftyDialogBuilder dialogBuilder;
    String imageURL;
    ViewGroup parent;


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
        Log.d(TAG, "ChatRecyclerViewAdapter: " + databaseReference.toString());
        this.databaseReference.addChildEventListener(childEventListener);
        mValues = new ArrayList<>();
        chats = new ArrayList<>();
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //holder.mItem = getItem(position);
        holder.mItem = chats.get(position);


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
                        holder.ivImage.setVisibility(ImageView.VISIBLE);
                        holder.cvImg.setVisibility(CardView.VISIBLE);
                        lp.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lp.addRule(RelativeLayout.RIGHT_OF, R.id.cvAuthorImg);
                        lpGif.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lpGif.addRule(RelativeLayout.RIGHT_OF, R.id.cvAuthorImg);
                        message = holder.mItem.getOwner() + "\n";
                    }
                }

                //holder.tvAuthor.setText(holder.mItem.getOwner());
            }

            if (holder.mItem.getPost().charAt(0) != '/') {
                holder.tvMsg.setVisibility(View.VISIBLE);
                holder.tvMsg.setLayoutParams(lp);
                message += holder.mItem.getPost();
                holder.tvMsg.setText(message);
                holder.ivGif.setVisibility(View.INVISIBLE);
            }
            else {
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

    private void doCommand(final ViewHolder holder, String cmd) {
        Log.d(TAG, "doCommand: " +cmd);
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
                holder.ivGif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPopUp(holder.ivGif);
                    }
                });
                break;
            case UPLOADIMG :
                imageURL = holder.mItem.getPost().substring(11, Math.min(holder.mItem.getPost().length(), holder.mItem.getPost().length()));
                if (URLUtil.isValidUrl(imageURL)) {
                    holder.ivGif.setVisibility(View.VISIBLE);
                    holder.tvMsg.setVisibility(View.INVISIBLE);
                    //holder.ivGif.getLayoutParams().height = 300;
                    Glide.with(context).load(imageURL).into(holder.ivGif);
                    holder.ivGif.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            viewPopUp(holder.ivGif);
                            //Log.d(TAG, "onClick: "+view.getTransitionName().toString());
                        }
                    });

                }
                break;

            case UPLOADDOC:
                imageURL =args;
                final String filename;
                final String validUrl = args.substring(args.indexOf(' ') + 1);
                if (args.contains(" ")) {
                    filename = args.substring(0, args.indexOf(' '));

                    if (URLUtil.isValidUrl(validUrl)) {
                        holder.ivGif.setVisibility(View.VISIBLE);
                        holder.tvMsg.setVisibility(View.INVISIBLE);
                        //holder.ivGif.getLayoutParams().height = 300;
                        Drawable myDrawable = context.getResources().getDrawable(R.drawable.yoko);
                        holder.ivGif.setImageDrawable(myDrawable);

                        Log.d(TAG, "file complete url: " + imageURL);
                        Log.d(TAG, "FILE url: " + validUrl);

                        Log.d(TAG, "file name: " + filename);
                        // Glide.with(context).load(imageURL).into(holder.ivGif);
                        //holder.ivGif

                        //--------------------
                        holder.ivGif.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //  Log.d(TAG, "onClick: " + validUrl);
                                    new DownloadTask(validUrl, filename);
                                //context.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                            }
                        });
                    }
                }

                break;

        }
    }

    private void viewPopUp(ImageView ivGif) {
        Drawable myDrawable = ivGif.getDrawable();
        //    ImageView imageView = (ImageView) view;
        // Log.d(TAG, "onClick: "+imageView.getContentDescription());
        // Log.d(TAG, "onClick: "+imageView.getId());
        // Log.d(TAG, "onClick: "+ivGif.getContentDescription());
        // Log.d(TAG, "onClick: "+ivGif.getId());
        // imageView.getTag();
        //ImageView ivDisplayFullImage = (ImageView) view.findViewById(R.id.ivDisplayFullImage);
        //Glide.with(context).load(imageURL).into(ivDisplayFullImage);
        effect = Effectstype.Fliph;
        dialogBuilder= NiftyDialogBuilder.getInstance(context);

        //---------------------

        //                  Log.d(TAG, "onClick: "+ imageView.getContentDescription().toString());
        //---------------
        View view1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_group_photo_dialog, parent, false);


        ImageView ivDisplayFullImage = view1.findViewById(R.id.ivDisplayFullImage);

        //Glide.with(context).load( imageURL).into(ivDisplayFullImage);
        ivDisplayFullImage.setImageDrawable(myDrawable);
        ivDisplayFullImage.getLayoutParams().height = 800;
//
        //  ivDisplayFullImage.setImageBitmap() imageView;
        //  Glide.with(context).load( imageURL).into(ivDisplayFullImage);


        dialogBuilder
                .withTitle(null)                                  //.withTitle(null)  no title
                .withMessage(null)                     //.withMessage(null)  no Msg
                .withDialogColor("#C1C8E4")                               //def  | withDialogColor(int resid)                               //def
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(effect)                                         //def Effectstype.Slidetop
                .setCustomView(view1, context)         //.setCustomView(View or ResId,context)

                .show();
        //----------
/*
                    dialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface d) {
                            ImageView ivDisplayFullImage = dialogBuilder.findViewById(R.id.ivDisplayFullImage);
                           // Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                           //         R.drawable.whygoprodialogimage);
                           // float imageWidthInPX = (float)image.getWidth();

 //                           LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
   //                                 Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
     //                       image.setLayoutParams(layoutParams);

                            Glide.with(context).load(imageURL).into(ivDisplayFullImage);


                        }
                    });
            */
        //---------
        // Glide.with(context).load(imageURL).into(ivDisplayFullImage);
        //Toast.makeText(context, "power", Toast.LENGTH_SHORT).show();

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




