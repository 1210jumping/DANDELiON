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
 * DataGenerator 類別。
 * 這是一個工具類別，用於隨機產生評論、使用者和課程的資料。
 */
public class DataGenerator {

    // 音樂、使用者和評論的基本數量
    public final static int course_num = 50; // 課程數量
    public final static int user_num = 40; // 使用者數量
    public final static int review_num = 100; // 評論數量
    public final static int comment_num = 2500; // 留言數量


    /**
     * 建立 2500 筆留言資料。
     * @throws ParserConfigurationException 如果配置剖析器時發生錯誤。
     * @throws TransformerException 如果轉換過程中發生錯誤。
     */
    public static void createData() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document document = db.newDocument();
        Random random = new Random();
        document.setXmlStandalone(true); // 設定 XML 是否為獨立文件
        Element commentList = document.createElement("commentList"); // 建立 commentList 根元素

        // 迴圈產生指定數量的留言
        for (int i = 0; i < DataGenerator.comment_num; i++) {
            Element comment = document.createElement("comment"); // 建立 comment 元素
            Element course = document.createElement("course_id"); // 建立 course_id 元素
            Element user = document.createElement("user_id"); // 建立 user_id 元素
            Element contents = document.createElement("content"); // 建立 content 元素
            Element datetime = document.createElement("datetime"); // 建立 datetime 元素

            int user_id = random.nextInt(user_num) + 1; // 隨機產生使用者 ID
            user.setTextContent(""+user_id); // 設定使用者 ID
            int course_id = random.nextInt(course_num) + 1; // 隨機產生課程 ID
            course.setTextContent(""+course_id); // 設定課程 ID
            String date = getRandomDatetime(); // 取得隨機日期時間
            datetime.setTextContent(date); // 設定日期時間

            String content = getRandomContent(); // 取得隨機內容
            contents.setTextContent(content); // 設定內容

            // 將子元素加入 comment 元素
            comment.appendChild(course);
            comment.appendChild(user);
            comment.appendChild(datetime);
            comment.appendChild(contents);

            comment.setAttribute("id",i+1+""); // 設定 comment 元素的 id 屬性
            commentList.appendChild(comment); // 將 comment 元素加入 commentList
        }

        document.appendChild(commentList); // 將 commentList 加入文件
        // 將來源樹轉換為結果樹
        // 用於處理來自各種來源的 XML，並將轉換輸出寫入各種接收器 (請參閱 transformer 文件)
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        // 設定編碼
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        // 縮排輸出文件
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        // 建立文件
        DOMSource source = new DOMSource(document); // 作為文件物件模型 (DOM) 樹形式的轉換來源樹的持有者。
        StreamResult result = new StreamResult(new File(Global.commentPath)); // 作為轉換結果的持有者，可以是 XML 等。
        transformer.transform(source, result); // 將 XML 來源轉換為結果。
        System.out.println("create successful"); // 印出建立成功訊息
    }


    /**
     * 取得格式為 "yyyy-mm-dd HH:MM:SS" 的隨機日期時間。
     * @return 日期時間字串。
     */
    @SuppressLint("DefaultLocale")
    public static String getRandomDatetime(){
        Random random = new Random();
        // 產生隨機年、月、日、時、分、秒，並格式化為指定字串
        return "2022-" +
                String.format("%02d", random.nextInt(9) + 1) + // 月份 (1-9，補零)
                "-" +
                String.format("%02d", random.nextInt(30) + 1) + // 日期 (1-30，補零)
                " " +
                String.format("%02d", random.nextInt(24)) + // 小時 (0-23，補零)
                ":" +
                String.format("%02d", random.nextInt(60)) + // 分鐘 (0-59，補零)
                ":" +
                String.format("%02d", random.nextInt(60)); // 秒鐘 (0-59，補零)
    }

    /**
     * 從檔案中取得評論列表。
     * @return 儲存在檔案中的評論列表。
     */
    public static List<String> getContentList(){
        BufferedReader bufferedReader;
        ArrayList<String> result = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new FileReader(Global.contentPath)); // 建立 BufferedReader 以讀取檔案
            String line;
            // 逐行讀取檔案內容
            while ((line = bufferedReader.readLine()) != null){
                result.add(line); // 將每一行加入結果列表
            }
            bufferedReader.close(); // 關閉 BufferedReader
        } catch (IOException e) {
            e.printStackTrace(); // 印出 IO 例外堆疊追蹤
        }
        return result; // 回傳結果列表
    }

    /**
     * 從評論列表中取得隨機評論。
     * @return 隨機評論的字串。
     */
    public static String getRandomContent(){
        List<String> reviewList = getContentList(); // 取得評論列表
        Random random = new Random();
        int index = random.nextInt(review_num); // 隨機產生索引
        return reviewList.get(index); // 回傳對應索引的評論
    }

    /**
     * 執行 main 方法以產生資料。
     * @param args 命令列參數。
     * @throws TransformerException 如果轉換過程中發生錯誤。
     * @throws ParserConfigurationException 如果配置剖析器時發生錯誤。
     */
    public static void main(String[] args) throws TransformerException, ParserConfigurationException {
        createData(); // 呼叫 createData 方法產生資料
        System.out.println("Generate data successful!"); // 印出資料產生成功訊息
    }

}