package com.example.myeducationapp.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author u7532738 Jinhan Tan
 * ContactListAdapter class
 * this is an adapter for user to display their contact list
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder>{
    private List<String> myMessageList;
    private List<User> myContactList;
    private Context context;

    /**
     * constructor
     * @param msgList
     * @param userList
     * @param context
     */
    public ContactListAdapter(List<String> msgList, List<User>userList, Context context) {
        this.myMessageList=msgList;
        this.myContactList=userList;
        this.context=context;
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
    public ContactListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ContactListAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list,parent,false));
    }


    /**
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ContactListAdapter.MyViewHolder holder, int position) {
        User user=myContactList.get(position);
        Random r = new Random();
        int i = r.nextInt(Global.drawables.size());
        holder.imageView.setImageResource(Global.drawables.get(i % Global.drawables.size()));
        holder.tv_name.setText(user.getName());
        holder.tv_message.setText(myMessageList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    /**
     * get item number
     * @return
     */
    @Override
    public int getItemCount() {
        return myMessageList.size();
    }

    /**
     * notify the adapter that data has changed
     */
    public void update(){
        notifyDataSetChanged();
    }

    /**
     * myViewHolder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tv_name;
        TextView tv_message;

        /**
         * constructor
         * @param itemView
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.userHead);
            tv_name=itemView.findViewById(R.id.userNameText);
            tv_message=itemView.findViewById(R.id.msgText);
        }
    }

}
