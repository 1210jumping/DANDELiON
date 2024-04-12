package com.example.myeducationapp.TokenizerAndParser;
/**
 * @author u7560434 Ethan Yifan Zhu
 * **/
public class CourseToken {

    public enum CourseType{
        CNO,
        CNAME,
        SUB,
        LEC,
        SEPARATOR,
        EQUAL,
        SEARCH_STRING
    }
    // types of Course Token
    private final String token;
    private final CourseType courseType;
    private final int length;

    public CourseToken(String res) {
        this.length = res.length();

        switch (res){
            case "CNO":
                this.courseType = CourseType.CNO;
                this.token = res;
                break;
            case "CNAME":
                this.courseType = CourseType.CNAME;
                this.token = res;
                break;
            case "SUB":
                this.courseType = CourseType.SUB;
                this.token = res;
                break;
            case "LEC":
                this.courseType = CourseType.LEC;
                this.token = res;
                break;
            case ";":
                this.courseType = CourseType.SEPARATOR;
                this.token = ";";
                break;
            case "=":
                this.courseType = CourseType.EQUAL;
                this.token = "=";
                break;
            default:
                this.courseType = CourseType.SEARCH_STRING;
                this.token = res;
                break;
        }
    }

    public String getToken() {
        return token;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "CourseToken{" +
                "token='" + token + '\'' +
                ", courseType=" + courseType +
                ", length=" + length +
                '}';
    }
}
