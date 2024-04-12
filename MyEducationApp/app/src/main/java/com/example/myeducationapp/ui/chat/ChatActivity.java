package com.example.myeducationapp.ui.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myeducationapp.DAO.Message;
import com.example.myeducationapp.DAO.UserDAO.MyUser;
import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * @author u7532738 Jinhan Tan
 * ChatActivity class
 * this is the chat page where users can send peer to peer message
 */
public class ChatActivity extends AppCompatActivity {
    private Button sendMsgBtn;
    private EditText messageInput;
    private RecyclerView messageRecyclerView;
    private User toUser;
    ChatAdapter chatAdapter;
    ArrayList<Message>messageArrayList=new ArrayList<>();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference messageRef=database.getReference("message");

    /**
     * on create
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toUser = (User) getIntent().getSerializableExtra("user");
        setTitle(toUser.getName());
        sendMsgBtn=findViewById(R.id.sendMsgBtn);
        messageInput=findViewById(R.id.messageInput);
        messageRecyclerView=findViewById(R.id.MessageRecyclerView);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        init();
    }

    /**
     * init view
     */
    private void init(){
        messageInput.setHint("input message");
        chatAdapter=new ChatAdapter(messageArrayList,getApplicationContext());
        messageRecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        messageRecyclerView.setLayoutManager(layoutManager);
        messageRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                LinearLayoutManager.VERTICAL));
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        // set listener to firebase reference
        messageRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message msg=snapshot.getValue(Message.class);
                if((msg.getFromUserId().equals(Global.currentUser.getId())&&msg.getToUserId().equals(toUser.getId())
                )||(msg.getToUserId().equals(Global.currentUser.getId())&&msg.getFromUserId().equals(toUser.getId()))
                ) {
                    messageArrayList.add(msg);
                    System.out.println(msg);
                    messageRecyclerView.invalidate();
                    chatAdapter.notifyItemInserted(messageArrayList.size()-1);
                    messageRecyclerView.scrollToPosition(messageArrayList.size()-1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * send message, add to firebase
     */
    private void sendMessage(){
        String message=messageInput.getText().toString();
        if(message.equals(""))return;
        String fromUid= Global.currentUser.getId();
        String toUid=toUser.getId();
        Message msg=new Message(fromUid,toUid,message);
        messageInput.setText("");
        // add message to firebase
        messageRef.push().setValue(msg);
    }

    /**
     * back button
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}