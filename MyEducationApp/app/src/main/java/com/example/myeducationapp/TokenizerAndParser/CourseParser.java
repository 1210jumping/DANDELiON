package com.example.myeducationapp.TokenizerAndParser;

import com.example.myeducationapp.DAO.CourseDAO.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * @author u7560434 Ethan Yifan Zhu
 * **/
public class CourseParser {
    private List<CourseToken> tokenList = new ArrayList<>();
    private CourseTokenizer tokenizer;
    public static class IllegalParserException extends IllegalArgumentException {
        public IllegalParserException(String errorMessage) {
            super(errorMessage);
        }
    }

    public CourseParser(String searchText) {
        tokenizer = new CourseTokenizer(searchText);
        parseExp();
    }

    /**
     * Adheres to the grammar rule:
     * <exp>    ::= <term> | <term> ; <exp>
     *
     */
    public void parseExp(){
        parseTerm();
        if(tokenizer.hasNext() &&  tokenizer.current().getCourseType() == CourseToken.CourseType.SEPARATOR){
            tokenizer.next();
            parseExp();
        }
    }

    /**
     * Adheres to the grammar rule:
     * <term>    ::= <CNO> = <SEARCH_STRING> | <CNAME> = <SEARCH_STRING> | <SUB> = <SEARCH_STRING> | <LEC> = <SEARCH_STRING>
     *
     */
    public void parseTerm(){
        if(tokenizer.hasNext() && tokenizer.current().getCourseType() != CourseToken.CourseType.SEPARATOR){
            if(!(tokenizer.current().getCourseType().equals(CourseToken.CourseType.CNAME) || tokenizer.current().getCourseType().equals(CourseToken.CourseType.CNO) || tokenizer.current().getCourseType().equals(CourseToken.CourseType.SUB) || tokenizer.current().getCourseType().equals(CourseToken.CourseType.LEC)))
                throw new IllegalParserException("Wrong search string");
            tokenList.add(tokenizer.current());
            tokenizer.next();
            if(!(tokenizer.current().getCourseType().equals(CourseToken.CourseType.EQUAL)))
                throw new IllegalParserException("Wrong search string");
            tokenizer.next();
            if(!(tokenizer.current().getCourseType().equals(CourseToken.CourseType.SEARCH_STRING)))
                throw new IllegalParserException("Wrong search string");
            tokenList.add(tokenizer.current());
            tokenizer.next();
        }
    }

    public List<CourseToken> getTokenList() {
        return tokenList;
    }

    public boolean isMatched(Course course){
        List<Boolean> condition = new ArrayList<>();

        for(int i=0;i<tokenList.size();i++){
            CourseToken token=tokenList.get(i++);
            switch (token.getCourseType()){
                case CNO:
                    token=tokenList.get(i);
                    if(!course.getCno().contains(token.getToken())){
                        return false;
                    }
                    break;
                case CNAME:
                    token=tokenList.get(i);
                    if(!course.getCname().contains(token.getToken())){
                    return false;
                }
                    break;
                case LEC:
                    token=tokenList.get(i);
                    if(!course.getLecturer().contains(token.getToken())){
                        return false;
                    }
                    break;
                case SUB:
                    token=tokenList.get(i);
                    if(!course.getSubject().contains(token.getToken())){
                        return false;
                    }
                    break;
            }
        }

        return true;
    }


}
