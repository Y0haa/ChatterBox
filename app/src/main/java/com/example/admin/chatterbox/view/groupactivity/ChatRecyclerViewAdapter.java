package com.example.admin.chatterbox.view.groupactivity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.model.chat.Chat;

import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private final List<Chat> mValues;
    private final OnListInteractionListener mListener;

    public ChatRecyclerViewAdapter(List<Chat> items, OnListInteractionListener listener) {
        mValues = items;
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
        holder.mItem = mValues.get(position);
        holder.tvAuthor.setText(mValues.get(position).getOwner().getUsername());
        holder.tvMsg.setText(mValues.get(position).getPost());

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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvAuthor;
        public final TextView tvMsg;
        public Chat mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
            tvMsg = (TextView) view.findViewById(R.id.tvMessage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvMsg.getText() + "'";
        }
    }

    interface OnListInteractionListener{
        void OnListInteraction(Chat mItem);
    }
}
