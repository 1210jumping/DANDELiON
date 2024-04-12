package com.example.myeducationapp;

import static org.junit.Assert.assertEquals;

import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.TokenizerAndParser.CourseParser;
import com.example.myeducationapp.TokenizerAndParser.CourseToken;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Ethan Yifan Zhu
 * To check whether parser works properly
 * **/
public class ParserTest {
    @Test
    public void tokenlistTest1(){
        String s = "CNO = AN12 ";
        CourseParser courseParser = new CourseParser(s);
        List<CourseToken> list = new ArrayList<>();
        list.add(new CourseToken("CNO"));
        list.add(new CourseToken("AN12"));
        assertEquals(list.toString(), courseParser.getTokenList().toString());
    }

    @Test(expected = CourseParser.IllegalParserException.class)
    public void tokenlistTest2() throws CourseParser.IllegalParserException {
        String s = "CNA = LL80";
        CourseParser courseParser = new CourseParser(s);
    }

    @Test(expected = CourseParser.IllegalParserException.class)
    public void tokenlistTest3() throws CourseParser.IllegalParserException {
        String s = "CNO ;LLS";
        CourseParser courseParser = new CourseParser(s);
    }

    @Test
    public void tokenlistTest4() throws CourseParser.IllegalParserException {
        String s = "CNO = AL12; CNAME = DSC D S DD; SUB = AAA; LEC = SSSS";
        CourseParser courseParser = new CourseParser(s);
        List<CourseToken> list = new ArrayList<>();
        list.add(new CourseToken("CNO"));
        list.add(new CourseToken("AL12"));
        list.add(new CourseToken("CNAME"));
        list.add(new CourseToken("DSC D S DD"));
        list.add(new CourseToken("SUB"));
        list.add(new CourseToken("AAA"));
        list.add(new CourseToken("LEC"));
        list.add(new CourseToken("SSSS"));
        System.out.println(courseParser.getTokenList().toString());
        assertEquals(list.toString(), courseParser.getTokenList().toString());
    }
    @Test
    public void isMatched(){
        CourseParser parser = new CourseParser("CNAME=1;");
        Course course=new Course("1","1","","111","","");
        assertEquals(true,parser.isMatched(course));
    }
}
