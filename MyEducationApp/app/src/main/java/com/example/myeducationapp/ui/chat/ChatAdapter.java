package com.example.myeducationapp.ui.chat;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myeducationapp.DAO.Message;
import com.example.myeducationapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author u7532738 Jinhan Tan
 * ChatAdapter class
 * this is an adapter for p2p chat, where the message user send display on the right,
 * and the message user received display on the left,
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<Message> myMessageList;
    private Context context;

    /**
     * constructor
     * @param list
     * @param context
     */
    public ChatAdapter(List<Message> list, Context context) {
        this.myMessageList=list;
        this.context=context;
    }

    /**
     *
     * @param position position to query
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (myMessageList.get(position).isFrom()) {
            return 1;
        }
        return 2;
    }

    /**
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1){
            return new FromViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.from_message,parent,false));
        }
        return new ToViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.to_message,parent,false));
    }


    /**
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message=myMessageList.get(position);
        holder.tv_date.setText(message.getDateTime());
        holder.tv_message.setText(message.getMessage());
    }

    /**
     *
     * @return item number
     */
    @Override
    public int getItemCount() {
        return myMessageList.size();
    }

    /**
     * update data and notify the adapter
     */
    public void update(){
        notifyDataSetChanged();
    }

    /**
     *  MyViewHolder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_date;
        TextView tv_message;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     *  FromViewHolder
     */
    public class FromViewHolder extends MyViewHolder {
        /**
         *
         * @param itemView
         */
        public FromViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.date_text);
            tv_message = itemView.findViewById(R.id.message_text);
        }
    }

    /**
     * ToViewHolder
     */
    public class ToViewHolder extends MyViewHolder {

        /**
         *
         * @param itemView
         */
        public ToViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.date_text2);
            tv_message = itemView.findViewById(R.id.message_text2);
        }
    }
}

