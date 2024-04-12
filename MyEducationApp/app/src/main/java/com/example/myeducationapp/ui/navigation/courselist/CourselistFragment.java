package com.example.myeducationapp.ui.navigation.courselist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myeducationapp.databinding.FragmentTransformBinding;
import com.example.myeducationapp.ui.course.CourseJoinActivity;
import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author u7516976 Zixuan Wang and jinhan Tan
 * CourseHomeActivity class
 * this is an adapter for user to display all courses list
 */
public class CourselistFragment extends Fragment {
    private FragmentTransformBinding binding;
    private ArrayList<Course> courseLists=new ArrayList<>();

    ListView courseListView;
    TextView sortByTime;
    TextView sortByName;
    Context myContext;
    SimpleAdapter listAdapter;
    int order=1;

    private List<Map<String, Object>> courseResultMapList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTransformBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        myContext=this.getContext();
        courseListView=binding.courseListView;
        sortByTime = binding.sortByTime;
        sortByName = binding.sortByName;
        init();
        return root;
    }

    private void init(){
        courseLists.clear();
        courseLists.addAll(Global.courseList);
        initResultList();
        sortByTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Course course:courseLists){
                    course.setSortOrder(2,order);
                }
                Collections.sort(courseLists);
                showCourse(courseLists);
                listAdapter.notifyDataSetChanged();
            }
        });
        sortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Course course:courseLists){
                    course.setSortOrder(1,order);
                }
                Collections.sort(courseLists);
                showCourse(courseLists);
                listAdapter.notifyDataSetChanged();
            }
        });
        listAdapter = new SimpleAdapter(
                myContext,
                courseResultMapList,
                R.layout.course_item,
                new String[]{"c_img", "c_no", "c_name", "c_subject", "c_date", "c_lecturer"},
                new int[]{R.id.c_img, R.id.c_no, R.id.c_name, R.id.c_sub, R.id.c_date, R.id.c_lecturer}
        );
        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(myContext, CourseJoinActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Course", courseLists.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        courseListView.setAdapter(listAdapter);
        showCourse(courseLists);
    }
// Get the course information
    private void showCourse(List<Course> courseList) {
        courseResultMapList.clear();
        for (int i = 0; i < courseList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            Course course = courseList.get(i);
            try {
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

    private void initResultList() {
        courseLists = new ArrayList<>();
        for (Course course : Global.courseList) {
            courseLists.add(course);
        }
    }
// Destroy the view
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}