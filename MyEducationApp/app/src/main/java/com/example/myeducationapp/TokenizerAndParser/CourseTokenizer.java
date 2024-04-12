package com.example.myeducationapp.TokenizerAndParser;

import android.widget.MultiAutoCompleteTextView;

import java.nio.Buffer;

/**
 * @author u7560434 Ethan Yifan Zhu
 *
 * **/
public class CourseTokenizer extends Tokenizer {
    private String buffer;
    private CourseToken currentToken;

    /**
     * Tokenizer class constructor
     * The constructor extracts the first token and save it to currentToken
     */
    public CourseTokenizer(String buffer) {
        this.buffer = buffer;
        next();
    }

    /**
     * This function will find and extract a next token from {@code _buffer} and
     * save the token to {@code currentToken}.
     */
    public void next(){
        buffer = buffer.trim();

        if(buffer.isEmpty()){
            currentToken = null;
            return;
        }

        String firstToken = "";
        char firstString = buffer.charAt(0);
        switch (firstString){
            case '=':
                currentToken = new CourseToken("=");
                break;
            case ';':
                currentToken = new CourseToken(";");
                break;
            default:
                int i = 0;
                while(true){
                    if(!((buffer.charAt(i)>='A' && buffer.charAt(i)<='Z') || (buffer.charAt(i)>='a' && buffer.charAt(i)<='z') || (buffer.charAt(i)>='0' && buffer.charAt(i)<='9') || buffer.charAt(i) == ' '))
                        break;
                    firstToken += buffer.charAt(i);
                    if(i == buffer.length()-1)
                        break;
                    i++;

                }
                currentToken = new CourseToken(firstToken.trim());
                break;
        }

        int tokenLen = currentToken.getLength();
        buffer = buffer.substring(tokenLen);
    }

    /**
     * Returns the current token extracted by {@code next()}
     *
     * @return type: Token
     */
    public CourseToken current() {
        return currentToken;
    }

    /**
     * Check whether there still exists another tokens in the buffer or not
     * @return type: boolean
     */
    public boolean hasNext() {
        return currentToken != null;
    }
}
