package com.example.myeducationapp.ui.navigation.mycourse;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.DAO.CourseDAO.CourseDao;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.databinding.FragmentHomeBinding;
import com.example.myeducationapp.ui.course.MycourseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/**
 * @author u7516976 Zixuan Wang
 * CourseHomeActivity class
 * this is a fragment for user to display the courses their selected
 */
// Implementation of a HomeFragment class
public class myCourseFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ListView myCourseListView;
    TextView sortByTime;
    TextView sortByName;
    Context myContext;
    int order=1;
    private ArrayList<Course> myCourseLists=new ArrayList<>();

    private List<Map<String,Object>> courseResultMapList = new ArrayList<>();

    private RecyclerView recyclerView;
    MycourseAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myCourseViewModel homeViewModel =
                new ViewModelProvider(this).get(myCourseViewModel.class);
        myContext=this.getContext();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sortByTime = binding.sortByTime;
        sortByName = binding.sortByName;
        myCourseListView=binding.MyCourseListView;
        recyclerView=binding.myCourserecyclerview;

        init();
        return root;
    }
//  Sets up event listeners
    void init() {
        myCourseLists=new ArrayList<>();
        new CourseDao().getUserCourse();
        int i=0;
        for(Course course:Global.myCourseList){
            myCourseLists.add(course);
        }
        adapter = new MycourseAdapter(myCourseLists,myContext);
        // Sorting by time
        sortByTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Course course:myCourseLists){
                    course.setSortOrder(2,order);
                }
                Collections.sort(myCourseLists);
                adapter.notifyDataSetChanged();
            }
        });
        // Sorting by name
        sortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Course course:myCourseLists){
                    course.setSortOrder(1,order);
                }
                Collections.sort(myCourseLists);
                adapter.notifyDataSetChanged();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(myContext);

        recyclerView.addItemDecoration(new DividerItemDecoration(myContext,
                LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}