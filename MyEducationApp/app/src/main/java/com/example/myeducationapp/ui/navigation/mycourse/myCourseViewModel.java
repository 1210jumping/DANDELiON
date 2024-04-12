package com.example.myeducationapp.ui.navigation.mycourse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.Global;

import java.util.ArrayList;
import java.util.List;
/**
 * @author u7516976 Zixuan Wang
 * CourseHomeActivity class
 */
// Loading the course content
public class myCourseViewModel extends ViewModel {
    private final MutableLiveData<List<String>> mTexts;

    public myCourseViewModel() {
        mTexts = new MutableLiveData<>();
    }
    public LiveData<List<String>> getTexts() {
        List<String>courseName=new ArrayList<>();
        for(Course course: Global.myCourseList){
            courseName.add(course.getCno()+" "+course.getCname()+"\n"+course.getContent());
        }
        mTexts.setValue(courseName);
        return mTexts;
    }
}