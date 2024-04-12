package com.example.myeducationapp.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;
import com.example.myeducationapp.TokenizerAndParser.CourseParser;
import com.example.myeducationapp.TokenizerAndParser.CourseToken;
import com.example.myeducationapp.ui.course.CourseJoinActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author u7532738 Jinhan Tan
 * SearchActivity class
 * this is the activity where user can search course
 * user need to type in content in the edit text and click the search button
 * user can receive result list of the course, it will display there
 * user can click the course to see its details
 * user can sort the course by time and cno, and decide whether it is asc order or desc order
 */
public class SearchActivity extends AppCompatActivity {

    private ImageButton searchBtn;
    private EditText searchText;
    private RadioGroup sortBy;
    private ListView courseListView;
    private ArrayList<Course> coursesList;
    private ArrayList<Course> resultCourseLists;
    private ImageButton ascBtn;
    private ImageButton descBtn;
    private int order = 1;
    SimpleAdapter listAdapter;
    private List<Map<String, Object>> courseResultMapList = new ArrayList<>();

    /**
     * constructor
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_course);
        searchBtn = findViewById(R.id.searchBtn);
        searchText = findViewById(R.id.searchText);
        courseListView = findViewById(R.id.courseList);
        sortBy = findViewById(R.id.radioGroup);
        ascBtn=findViewById(R.id.ascOrder);
        descBtn=findViewById(R.id.descOrder);
        init();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * init the page
     */
    private void init() {
        coursesList = new ArrayList<>();
        searchText.setHint("eg. CNO=;CNMAE=;LEC=;SUB=;");
        ascBtn.setEnabled(false);
        sortBy.check(R.id.radioButton1);
        initResultList();
        // get all courses before search
        for (Course course : resultCourseLists) System.out.println(course.toString());
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchContent = searchText.getText().toString();
                searchText.setText("");
                if (searchContent.equals("")) {
                    initResultList();
                    showCourse(resultCourseLists);
                } else {
                    getResultList(searchContent);
                }
            }
        });
        sortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sortList();
            }
        });
        ascBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order=1;
                ascBtn.setEnabled(false);
                descBtn.setEnabled(true);
                sortList();
            }
        });
        descBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order=-1;
                ascBtn.setEnabled(true);
                descBtn.setEnabled(false);
                sortList();
            }
        });
        listAdapter = new SimpleAdapter(
                this,
                courseResultMapList,
                R.layout.course_item,
                new String[]{"c_img", "c_no", "c_name", "c_subject", "c_date", "c_lecturer"},
                new int[]{R.id.c_img, R.id.c_no, R.id.c_name, R.id.c_sub, R.id.c_date, R.id.c_lecturer}
        );
        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CourseJoinActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Course", resultCourseLists.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        courseListView.setAdapter(listAdapter);
        sortList();
        showCourse(resultCourseLists);
    }

    /**
     * this is a function getting all the courseList
     */
    private void initResultList() {
        resultCourseLists = new ArrayList<>();
        coursesList = new ArrayList<>();
        for (Course course : Global.courseList) {
            resultCourseLists.add(course);
            coursesList.add(course);
        }
    }

    /**
     * this is the function search courses according to the content user input
     * @param content
     */
    private void getResultList(String content) {
        CourseParser parser = null;
        try {
            //use parser
            parser = new CourseParser(content);
            List<CourseToken> validList = parser.getTokenList();
            // no course found or invalid
            if (validList.size() == 0) {
                Toast.makeText(getApplicationContext(), R.string.no_course_found, Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {

        }
        if (parser != null) {
            // update course according to the parser
            resultCourseLists = new ArrayList<>();
            for (Course course : coursesList) {
                if (parser.isMatched(course)) {
                    resultCourseLists.add(course);
                }
            }
            sortList();
            showCourse(resultCourseLists);
            if(resultCourseLists.size()!=0) {
                Toast toast = Toast.makeText(getApplicationContext(), "Search successfully! " + resultCourseLists.size() + " result(s)", Toast.LENGTH_LONG);
                toast.show();
            }else {
                Toast.makeText(getApplicationContext(), R.string.no_course_found, Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_course_found, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * this is a function that can sort courses according to given ways
     */
    private void sortList() {
        RadioButton radioButton = findViewById(sortBy.getCheckedRadioButtonId());
        String sortBy = radioButton.getText().toString();
        if (sortBy.equals("cno")) {
            for (Course course : resultCourseLists) {
                course.setSortOrder(1, order);
            }
        }
        if (sortBy.equals("date")) {
            for (Course course : resultCourseLists) {
                course.setSortOrder(2, order);
            }
        }
        Collections.sort(resultCourseLists);
        showCourse(resultCourseLists);
    }

    /**
     * this is the function that can show the course according to
     * the given arraylist
     */
    private void showCourse(List<Course> courseList) {
        courseResultMapList.clear();
        for (int i = 0; i < courseList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            Course course = courseList.get(i);
            try {
                //int img_id = Global.drawables.get(i%Global.drawables.size());
                int img_id = Integer.parseInt(course.getImgUrl());
                map.put("c_img", img_id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("c_no", course.getCno());
            map.put("c_name", course.getCname());
            map.put("c_subject", course.getSubject());
            map.put("c_date", course.getReleaseDate());
            map.put("c_lecturer", course.getLecturer());
            courseResultMapList.add(map);
        }
        listAdapter.notifyDataSetChanged();

    }

    /**
     * go back button clicked
     * @param item
     * @return
     */
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
