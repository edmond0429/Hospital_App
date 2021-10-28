package com.example.hospital_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatRVAdapter extends RecyclerView.Adapter {

    private ArrayList<ChatsModel> mChatsModelArrayList;
    private Context mContext;

    public ChatRVAdapter(ArrayList<ChatsModel> chatsModelArrayList, Context context) {
        this.mChatsModelArrayList = chatsModelArrayList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        // layout type along with view holder.
        switch (viewType){
            case 0:
                // case 0 will inflating user message layout.
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg_rv_item,parent,false);
                return new UserViewHolder(view);
                // case 1 will inflating bot message layout.
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg_rv_item,parent,false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // this method is use to set data to layout file.
        ChatsModel chatsModel = mChatsModelArrayList.get(position);
        switch (chatsModel.getSender()){
            case "user":        //set user textview
                ((UserViewHolder)holder).userTV.setText(chatsModel.getMessage());
                break;
            case "bot":         //set bot textview
                ((BotViewHolder)holder).botMsgTV.setText(chatsModel.getMessage());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        // this function is create to set position.
        switch (mChatsModelArrayList.get(position).getSender()){
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        // return the size of array list
        return mChatsModelArrayList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        TextView userTV;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userTV=itemView.findViewById(R.id.idTvUser);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder{
        TextView botMsgTV;
        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botMsgTV = itemView.findViewById(R.id.idTvChatbot);
        }
    }
}
