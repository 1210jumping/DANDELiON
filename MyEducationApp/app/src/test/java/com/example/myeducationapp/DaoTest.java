package com.example.myeducationapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.myeducationapp.DAO.CommentDAO.Comment;
import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.DAO.UserDAO.MyUser;
import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.DAO.UserDAO.UserDao;


import org.junit.Test;

public class DaoTest {

    @Test
    public void UserTest(){
        User user = new User("1","user1","email1",1,"pwd","img1");
        assertEquals("1",user.getId());
        assertEquals("user1",user.getName());
        assertEquals("email1",user.getEmail());
        assertEquals("pwd",user.getPwd());
        assertEquals("img1",user.getImgURL());
        user.setSex(2);
        user.setName("user2");
        user.setCountry("CN");
        user.setImgURL("img2");
        user.setName("user2");
        assertEquals("user2",user.getName());
        assertEquals("img2",user.getImgURL());
        assertEquals("CN",user.getCountry());
        MyUser myUser=MyUser.getInstance(user);
        myUser.setCountry("CN");
        assertEquals("user2",myUser.getName());
        assertEquals("img2",myUser.getImgURL());
        assertEquals("CN",myUser.getCountry());
        myUser.logoutUser();
        user.setName("user3");
        myUser=MyUser.getInstance(user);
        assertEquals("user3",myUser.getName());
    }

    @Test
    public void CourseTest(){
        Course course=new Course("1","c","img","sub","lec","date");
        assertEquals("1",course.getId());
        assertEquals("c",course.getCname());
        assertEquals("img",course.getImgUrl());
        assertEquals("sub",course.getSubject());
        assertEquals("lec",course.getLecturer());
        assertEquals("date",course.getReleaseDate());
        assertEquals(1,course.getSortBy());
        assertEquals(1,course.getSortOrder());
        course.setSortBy(0);
        course.setSortOrder(-1);
        assertEquals(0,course.getSortBy());
        assertEquals(-1,course.getSortOrder());
    }

    @Test
    public void CommentTest(){
        Comment comment=new Comment("1","2","3","content","date");
        assertEquals("1",comment.getCommentId());
        assertEquals("2",comment.getUserId());
        assertEquals("3",comment.getCourseId());
        assertEquals("content",comment.getContent());
        assertEquals("date",comment.getReleaseDate());
    }



}
