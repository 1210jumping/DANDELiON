package com.example.myeducationapp.ui.course;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.myeducationapp.DAO.CommentDAO.Comment;
import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.DAO.UserDAO.MyUser;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author u7532738 Jinhan Tan
 * CourseJoinActivity class
 * This is the activity that when user click the course in the courselist,
 * it will show the detail of the course, the comment list of the coursse,
 * and user and join in the course at this page
 */
public class CourseJoinActivity extends AppCompatActivity {

    Button joinCourseBtn;
    TextView courseNameText;
    TextView courseNoText;
    TextView courseDateText;
    TextView courseLecturerText;
    TextView courseSubjectText;
    ImageView iv_img;
    ListView commentListView;
    Course currentCourse;
    MyUser currentUser;
    ArrayList<Comment> commentsList = new ArrayList<>();

    private List<Map<String, Object>> commentResultMapList = new ArrayList<>();
    SimpleAdapter listAdapter = null;

    /**
     * default onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        joinCourseBtn = findViewById(R.id.join_course_button);
        courseNameText = findViewById(R.id.course_title);
        courseNoText = findViewById(R.id.course_id);
        courseDateText = findViewById(R.id.course_date);
        courseLecturerText = findViewById(R.id.teacher_name);
        courseSubjectText = findViewById(R.id.course_category);
        commentListView = findViewById(R.id.commentListView);
        iv_img = findViewById(R.id.iv_course_img);
        currentCourse = (Course) getIntent().getSerializableExtra("Course");
        currentUser = Global.currentUser;
        setTitle("Course: " + currentCourse.getCname());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    /**
     * init the view
     */
    private void initView() {
        courseNoText.setText(currentCourse.getCno());
        courseSubjectText.setText(currentCourse.getSubject());
        courseLecturerText.setText(currentCourse.getLecturer());
        courseDateText.setText(currentCourse.getReleaseDate());
        courseNameText.setText(currentCourse.getCname());
        iv_img.setImageResource(Integer.parseInt(currentCourse.getImgUrl()));

        if (Global.myCourseList.contains(currentCourse)) {
            joinCourseBtn.setEnabled(false);
        } else {
            joinCourseBtn.setEnabled(true);
        }

        joinCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.myCourseList.add(currentCourse);
                Global.enroll(currentCourse);
                joinCourseBtn.setEnabled(false);
            }
        });
        if (Global.courseComment.get(currentCourse.getId()) != null)
            Global.courseComment.get(currentCourse.getId()).toList(commentsList);
        for (int i = 0; i < commentsList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            Comment comment = commentsList.get(i);
            map.put("comment_name", Global.getUserById(comment.getUserId()).getName());
            map.put("comment_text", comment.getContent());
            map.put("comment_time", comment.getReleaseDate());
            commentResultMapList.add(map);
        }

        listAdapter = new SimpleAdapter(
                this,
                commentResultMapList,
                R.layout.comment_list,
                new String[]{"comment_name", "comment_text", "comment_time"},
                new int[]{R.id.comment_name, R.id.comment_text, R.id.comment_time}
        );
        commentListView.setAdapter(listAdapter);
    }

    /**
     * go back button
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

    /**
     * notify the adapter that data has changed
     */
    public void update() {
        if (listAdapter != null)
            listAdapter.notifyDataSetChanged();
    }
}