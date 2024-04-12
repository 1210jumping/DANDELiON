package com.example.myeducationapp.data.dataUtils;

import android.annotation.SuppressLint;

import com.example.myeducationapp.Global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @author u7532738 Jinhan Tan
 * DataGenerator class
 * this is a util class used to randomly generate data for comment user and course
 */
public class DataGenerator {

    //the basic number of music, user and comment
    public final static int course_num = 50;
    public final static int user_num = 40;
    public final static int review_num = 100;
    public final static int comment_num = 2500;


    /**
     * create 2500 comments data
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static void createData() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document document = db.newDocument();
        Random random = new Random();
        document.setXmlStandalone(true);
        Element commentList = document.createElement("commentList");

        for (int i = 0; i < DataGenerator.comment_num; i++) {
            Element comment = document.createElement("comment");
            Element course = document.createElement("course_id");
            Element user = document.createElement("user_id");
            Element contents = document.createElement("content");
            Element datetime = document.createElement("datetime");

            int user_id = random.nextInt(user_num) + 1;
            user.setTextContent(""+user_id);
            int course_id = random.nextInt(course_num) + 1;
            course.setTextContent(""+course_id);
            String date = getRandomDatetime();
            datetime.setTextContent(date);

            String content = getRandomContent();
            contents.setTextContent(content);

            comment.appendChild(course);
            comment.appendChild(user);
            comment.appendChild(datetime);
            comment.appendChild(contents);

            comment.setAttribute("id",i+1+"");
            commentList.appendChild(comment);
        }

        document.appendChild(commentList);
        //transform a source tree into a result tree
        //Used to process XML from a variety of sources and write the transformation output to a variety of sinks (see transformer documentation)
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        //set encoding
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        //indent the output document
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        //create document
        DOMSource source = new DOMSource(document); //Acts as a holder for a transformation Source tree in the form of a Document Object Model (DOM) tree.
        StreamResult result = new StreamResult(new File(Global.commentPath));//Acts as a holder for a transformation result, which may be XML,..
        transformer.transform(source, result); //Transform the XML Source to a Result.
        System.out.println("create successful");
    }


    /**
     * To get random datetime with the format "yyyy-mm-dd"
     * @return the datetime string
     */
    @SuppressLint("DefaultLocale")
    public static String getRandomDatetime(){
        Random random = new Random();
        return "2022-" +
                String.format("%02d", random.nextInt(9) + 1) +
                "-" +
                String.format("%02d", random.nextInt(30) + 1) +
                " " +
                String.format("%02d", random.nextInt(24)) +
                ":" +
                String.format("%02d", random.nextInt(60)) +
                ":" +
                String.format("%02d", random.nextInt(60));
    }

    /**
     * To get the review list from file
     * @return the list of reviews stored in file
     */
    public static List<String> getContentList(){
        BufferedReader bufferedReader;
        ArrayList<String> result = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new FileReader(Global.contentPath));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                result.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get the random review from review list
     * @return a string of random review
     */
    public static String getRandomContent(){
        List<String> reviewList = getContentList();
        Random random = new Random();
        int index = random.nextInt(review_num);
        return reviewList.get(index);
    }

    /**
     * operate the main method to generate data
     */
    public static void main(String[] args) throws TransformerException, ParserConfigurationException {
        createData();
        System.out.println("Generate data successful!");
    }

}
