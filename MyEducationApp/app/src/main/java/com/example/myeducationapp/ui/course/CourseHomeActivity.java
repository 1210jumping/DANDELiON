package com.example.myeducationapp.ui.course;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.myeducationapp.DAO.CommentDAO.CommentDao;
import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.DAO.UserDAO.MyUser;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.ui.navigation.NavigationActivity;
import com.example.myeducationapp.R;

import java.util.Random;

/**
 * @author u7532738 Jinhan Tan && u6706220 Zhuxuan Yan
 * CourseHomeActivity class
 * This is the activity that when user click on the course he has enrolled in
 * It will show the course content and video here,
 * and allow user to send comment and drop the course
 */
public class CourseHomeActivity extends AppCompatActivity {
    Button sendCommentButton;
    TextView name;
    TextView desc;
    Button dropBtn;
    ImageView imgView;
    Course currentCourse;
    MyUser currentUser;

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_home);

        videoView=findViewById(R.id.videoView);
        sendCommentButton = findViewById(R.id.comment_button);
        name = findViewById(R.id.course_name);
        desc = findViewById(R.id.course_content);
        dropBtn = findViewById(R.id.exit_button);
        imgView = findViewById(R.id.course_image);
        currentCourse = (Course) getIntent().getSerializableExtra("Course");
        currentUser = Global.currentUser;
        setTitle(currentCourse.getCname());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView(){
        name.setText(currentCourse.getCname());
        desc.setText(currentCourse.getContent());
        Random r=new Random();
        int i=r.nextInt(Global.drawables.size());
        //holder.iv_img.setImageResource(news.getImgID());
        imgView.setImageResource(Integer.parseInt(currentCourse.getImgUrl()));
        //imgView.setImageResource(Global.drawables.get(i%Global.drawables.size()));
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
        dropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.drop(currentCourse);
                Intent intent=new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(intent);
                finish();
            }
        });
        videoView.setMediaController(new MediaController(this));
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    // input dialog box and ok button
    // author: u6706220 Zhuxuan Yan
    // Submit the comment
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Text my comment");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                new CommentDao().postNewComment(currentCourse.getId(),currentUser.getId(),text);
                // text in
            }
        });
        builder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


}



