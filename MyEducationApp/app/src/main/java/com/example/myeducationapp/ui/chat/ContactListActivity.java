package com.example.myeducationapp.ui.chat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;

import java.util.ArrayList;

/**
 * @author u7532738 Jinhan Tan
 * ContactListActivity class
 * this is the list that show the contact list of the user
 * user can only send message to those who enrolled in the same courses as they did
 */
public class ContactListActivity extends AppCompatActivity {
    ContactListAdapter adapter;
    ArrayList<String> messageArrayList=new ArrayList<>();
    ArrayList<User>contactList=new ArrayList<>();
    RecyclerView contactRecyclerListView;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        setTitle("Contact List");
        ActionBar actionBar = getSupportActionBar();
        contactRecyclerListView=findViewById(R.id.contactListRecyclerView);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        init();
    }

    /**
     * init view
     */
    private void init(){
        //get contactList
        for(User u: Global.userList){
            if(u.getId().equals(Global.currentUser.getId()))continue;
            for(Course course:Global.myCourseList){
                if(Global.enrollInfo.get(course.getId())!=null&&Global.enrollInfo.get(course.getId()).contains(u.getId())){
                    contactList.add(u);
                    messageArrayList.add("click to send messages");
                    break;
                }
            }
        }
        //set adapter
        adapter=new ContactListAdapter(messageArrayList,contactList,getApplicationContext());
        contactRecyclerListView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        contactRecyclerListView.setLayoutManager(layoutManager);
        contactRecyclerListView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                LinearLayoutManager.VERTICAL));
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