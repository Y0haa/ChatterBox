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
import com.example.admin.chatterbox.glide.GlideApp;
import com.example.admin.chatterbox.model.chat.Chat;
import com.example.admin.chatterbox.util.Commands;
import com.example.admin.chatterbox.util.CurrentStoredUser;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
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
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
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
        RelativeLayout.LayoutParams lprlDisplayImageContent = (RelativeLayout.LayoutParams) holder.rlDisplayImageContent.getLayoutParams();
        //lprlDisplayImageContent.height=300;

        String message = "";
        if (holder.mItem != null) {
            if (holder.mItem.getOwner() != null) {
                Log.d(TAG, "onBindViewHolder: " + CurrentStoredUser.getInstance().getUser().getName());
                if (holder.mItem.getOwner().compareTo(CurrentStoredUser.getInstance().getUser().getName()) == 0) {
                    holder.tvMsg.setBackgroundResource(R.drawable.me_speechbubble);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        holder.tvMsg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        holder.ivImage.setVisibility(ImageView.GONE);
                        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lpGif.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lprlDisplayImageContent.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lp.removeRule(RelativeLayout.RIGHT_OF);
                        lpGif.removeRule(RelativeLayout.RIGHT_OF);
                        lprlDisplayImageContent.removeRule(RelativeLayout.RIGHT_OF);
                        holder.cvImg.setVisibility(CardView.GONE);
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
                        lprlDisplayImageContent.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lprlDisplayImageContent.addRule(RelativeLayout.RIGHT_OF, R.id.cvAuthorImg);
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
                holder.ivGif.setVisibility(View.GONE);
                holder.tvFileId.setVisibility(View.INVISIBLE);
            }
            else {
                holder.ivGif.setVisibility(View.VISIBLE);
                holder.tvMsg.setVisibility(View.INVISIBLE);
                String cmd = holder.mItem.getPost().substring(1);
                holder.tvFileId.setVisibility(ImageView.INVISIBLE);
                holder.tvFileId.setText("");
                holder.ivGif.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                doCommand(holder, cmd);
              //  holder.tvFileId.setVisibility(ImageView.INVISIBLE);
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
        final String args = cmd.substring(cmd.indexOf(' ') + 1);
        if (cmd.contains(" ")) {
            cmd = cmd.substring(0, cmd.indexOf(' '));
        }
        switch (Commands.valueOf(cmd.toUpperCase())) {
            case YOKO:
                Glide.with(context).asGif().load(R.drawable.yoko).into(holder.ivGif);
                break;

            case GIPHY:
                Log.d(TAG, "args giphy: " + args);
                final String giphyFilename = args.substring(args.lastIndexOf("/") + 1);
                Glide.with(context).asGif().load(args).into(holder.ivGif);
                holder.ivGif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPopUp(holder.ivGif, args, giphyFilename);
                    }
                });

                holder.ivGif.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //------------prompt user to download
                        promptDownload(context, args, giphyFilename);
                        //------------
                        return false;
                    }
                });

                break;
            case UPLOADIMG :
                imageURL =args;
                final String filenameImg;
                final String validUrlImg = args.substring(args.indexOf(' ') + 1);
                if (args.contains(" ")) {
                    filenameImg = args.substring(0, args.indexOf(' '));
                    if (URLUtil.isValidUrl(validUrlImg)) {
                        holder.tvFileId.setVisibility(ImageView.VISIBLE);
                        holder.ivGif.setVisibility(View.VISIBLE);
                        holder.tvMsg.setVisibility(View.INVISIBLE);
                        //holder.ivGif.getLayoutParams().height = 300;
                        holder.ivGif.getLayoutParams().height = 300;
                        holder.tvFileId.setText(filenameImg);
                        Glide.with(context).load(validUrlImg).into(holder.ivGif);
                        holder.ivGif.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                viewPopUp(holder.ivGif, validUrlImg,filenameImg);
                                //Log.d(TAG, "onClick: "+view.getTransitionName().toString());
                            }
                        });
                        holder.ivGif.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                //------------prompt user to download
                                promptDownload(context, validUrlImg, filenameImg);
                                //------------
                                return false;
                            }
                        });

                }
                    //----
                /*
                    imageURL = holder.mItem.getPost().substring(11, Math.min(holder.mItem.getPost().length(), holder.mItem.getPost().length()));
                if (URLUtil.isValidUrl(imageURL)) {
                    holder.tvFileId.setVisibility(ImageView.VISIBLE);
                    holder.ivGif.setVisibility(View.VISIBLE);
                    holder.tvMsg.setVisibility(View.INVISIBLE);
                    //holder.ivGif.getLayoutParams().height = 300;
                    holder.ivGif.getLayoutParams().height = 300;
                    Glide.with(context).load(imageURL).into(holder.ivGif);
                    holder.ivGif.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            viewPopUp(holder.ivGif);
                            //Log.d(TAG, "onClick: "+view.getTransitionName().toString());
                        }
                    });
*/
                }
                break;

            case UPLOADDOC:
                imageURL =args;
                final String filename;
                final String validUrl = args.substring(args.indexOf(' ') + 1);
                if (args.contains(" ")) {
                    filename = args.substring(0, args.indexOf(' '));

                    if (URLUtil.isValidUrl(validUrl)) {
                        holder.tvFileId.setVisibility(ImageView.VISIBLE);
                        holder.ivGif.setVisibility(View.VISIBLE);
                        holder.tvMsg.setVisibility(View.INVISIBLE);
                        //holder.ivGif.getLayoutParams().height = 300;
                        Drawable myDrawable = context.getResources().getDrawable(R.drawable.document_icon);
                        holder.ivGif.setImageDrawable(myDrawable);

                        Log.d(TAG, "file complete url: " + imageURL);
                        Log.d(TAG, "FILE url: " + validUrl);
                        Log.d(TAG, "file name: " + filename);

                        holder.tvFileId.setText(filename);
                        //--------------------
                        holder.ivGif.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                //------------prompt user to download
                                promptDownload(context, validUrl, filename);
                                //------------

                            }
                        });
                    }
                }

                break;

        }
    }

    private void promptDownload(final Context context, final String validUrl, final String filename) {

        effect = Effectstype.RotateBottom;
        dialogBuilder=NiftyDialogBuilder.getInstance(context);
        dialogBuilder
                .withTitle("Download Attention")                                  //.withTitle(null)  no title
                .withTitleColor("#ffffff")                                  //def
               // .withDividerColor("#11000000")                              //def
                .withMessage("Would you like to download " + filename + " ?")                     //.withMessage(null)  no Msg
                .withMessageColor("#33ebf4")                              //def  | withMessageColor(int resid)
                .withDialogColor("#770a7f")                               //def  | withDialogColor(int resid)                               //def
                //    .withIcon(getResources().getDrawable(R.drawable.icon))
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(effect)                                         //def Effectstype.Slidetop
                .withButton1Text("OK")                                      //def gone
                .withButton2Text("Cancel")                                  //def gone
                //  .setCustomView(R.layout.custom_view,v.getContext())         //.setCustomView(View or ResId,context)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(v.getContext(), "i'm btn1", Toast.LENGTH_SHORT).show();
                        //  Log.d(TAG, "onClick: " + validUrl);
                        new DownloadTask(context, validUrl, filename);
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        // Toast.makeText(v.getContext(), "i'm btn2", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void viewPopUp(ImageView ivGif, final String url, final String filename) {
        Drawable myDrawable = ivGif.getDrawable();
        //    ImageView imageView = (ImageView) view;
        // Log.d(TAG, "onClick: "+imageView.getContentDescription());
        // Log.d(TAG, "onClick: "+imageView.getId());
        // Log.d(TAG, "onClick: "+ivGif.getContentDescription());
        // Log.d(TAG, "onClick: "+ivGif.getId());
        // imageView.getTag();
        //ImageView ivDisplayFullImage = (ImageView) view.findViewById(R.id.ivDisplayFullImage);
        //GlideApp.with(context).load(imageURL).into(ivDisplayFullImage);
        effect = Effectstype.Fliph;
        dialogBuilder= NiftyDialogBuilder.getInstance(context);


        View view1 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_group_photo_dialog, parent, false);


        ImageView ivDisplayFullImage = view1.findViewById(R.id.ivDisplayFullImage);
        ivDisplayFullImage.getLayoutParams().height = 800;
        Glide.with(ivDisplayFullImage.getContext()).load(url).into(ivDisplayFullImage);

      //  final String giphyFilename = args.substring(args.lastIndexOf("/") + 1);
        ivDisplayFullImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //------------prompt user to download
                promptDownload(context, url, filename);
                //------------
                return false;
            }
        });
        /*
        ivDisplayFullImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //------------prompt user to download
                promptDownload(context, validUrlImg, filenameImg);
                //------------
                return false;
            }
        });
*/
        //Glide.with(context).load( imageURL).into(ivDisplayFullImage);
       // ivDisplayFullImage.setImageDrawable(myDrawable);
        //ivDisplayFullImage.getLayoutParams().height = 800;


        dialogBuilder
                .withTitle(null)                                  //.withTitle(null)  no title
                .withMessage(null)                     //.withMessage(null)  no Msg
                .withDialogColor("#770a7f")                               //def  | withDialogColor(int resid)                               //def
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(effect)                                         //def Effectstype.Slidetop
                .setCustomView(view1, context)         //.setCustomView(View or ResId,context)
                .show();
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
        private final TextView tvFileId;
        public Chat mItem;
        private final CardView cvImg;
        private final RelativeLayout rlDisplayImageContent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            cvImg = view.findViewById(R.id.cvAuthorImg);
            //tvAuthor = view.findViewById(R.id.tvAuthor);
            tvMsg = view.findViewById(R.id.tvMessage);
            ivImage = view.findViewById(R.id.ivAuthorImg);
            ivGif = view.findViewById(R.id.ivGif);
            tvFileId = view.findViewById(R.id.tvFileId);
            rlDisplayImageContent = view.findViewById(R.id.rlDisplayImageContent);

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




