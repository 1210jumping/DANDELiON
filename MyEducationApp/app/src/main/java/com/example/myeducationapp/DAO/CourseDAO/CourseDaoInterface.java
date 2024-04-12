package com.example.myeducationapp.DAO.CourseDAO;

import android.content.Context;

import java.util.List;

/**
 * @author u7532738 Jinhan Tan
 * CourseDaoInterface
 */
public interface CourseDaoInterface {

    void findAllCourses();

    Course findCourseById(Context context, String id);
}
