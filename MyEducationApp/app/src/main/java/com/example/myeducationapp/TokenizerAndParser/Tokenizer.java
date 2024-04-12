package com.example.myeducationapp.TokenizerAndParser;

/**
 * @author u7560434 Ethan Yifan Zhu
 * **/
public abstract class Tokenizer {

    /**
     * check whether there is a next token in the remaining text.
     * @return true if there is, falser otherwise
     */
    public abstract boolean hasNext();

    /**
     * return the current token extracted by next() method.
     * @return the current token
     */
    public abstract Object current();

    /**
     *  extract next token from the current text and save it.
     */
    public abstract void next();
}
