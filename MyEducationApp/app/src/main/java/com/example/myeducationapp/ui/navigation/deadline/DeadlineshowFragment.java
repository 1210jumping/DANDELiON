package com.example.myeducationapp.ui.navigation.deadline;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.myeducationapp.R;
import com.example.myeducationapp.databinding.FragmentDeadlineBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * DeadlineshowFragment 用於顯示日曆以及對應日期的待辦事項列表。
 * 它允許使用者選擇日期並查看該日期的待辦事項。
 */
public class DeadlineshowFragment extends Fragment {

    // View binding 物件，用於存取佈局檔案中的視圖
    private FragmentDeadlineBinding binding;
    // 日曆視圖元件，用於顯示日曆
    private CalendarView calendarView;
    // 儲存日期與對應待辦事項列表的 Map
    private Map<String, ArrayList<String>>todoListMap=new HashMap<>();
    // TextView 用於顯示選定日期的待辦事項列表
    private TextView todoTextView;

    /**
     * 當 Fragment 第一次繪製其使用者介面時呼叫。
     *
     * @param inflater 用於將佈局檔案轉換為 View 物件的 LayoutInflater。
     * @param container 如果 Fragment 是附加到父 ViewGroup，則為該 ViewGroup；否則為 null。
     * @param savedInstanceState 如果 Fragment 是從先前儲存的狀態重新建立，則為包含該狀態的 Bundle；否則為 null。
     * @return Fragment 的根 View。
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 初始化 DeadlineshowViewModel
        DeadlineshowViewModel deadlineshowViewModel =
                new ViewModelProvider(this).get(DeadlineshowViewModel.class);
        // 初始化 View binding
        binding = FragmentDeadlineBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // 獲取日曆視圖的參照
        calendarView=binding.calender;
        // 載入待辦事項資料
        getData();
        // 獲取用於顯示標題文字的 TextView
        final TextView textView = binding.textCalender;
        // 觀察 ViewModel 中的文字變化，並更新 TextView
        deadlineshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // 獲取用於顯示待辦事項列表的 TextView
        todoTextView = root.findViewById(R.id.todolist);
        // 設定日曆日期選擇變更的監聽器
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                // 將選擇的年、月、日格式化為日期字串
                String selectedDate = formatDate(year, month, dayOfMonth);
                // 顯示選定日期的待辦事項列表
                displayToDoList(selectedDate);
            }
        });

        // 初始顯示目前日期的待辦事項列表
        Calendar today = Calendar.getInstance();
        String currentFormattedDate = formatDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        displayToDoList(currentFormattedDate);

        return root;
    }

    /**
     * 獲取並初始化待辦事項資料。
     * 此方法使用預先定義的日期和待辦事項內容來填充 {@code todoListMap}。
     */
    private void getData(){
        // 預先定義的日期陣列
        String[] dates = {"2025-05-11", "2025-05-12", "2025-05-13", "2025-05-14",
                "2025-06-02", "2025-06-16", "2025-06-23", "2025-06-27",
                "2025-07-06", "2025-07-09", "2025-07-19", "2025-07-22"};
        // 預先定義的待辦事項陣列
        String[]todoLists={"Finish assignment","Listen to recording","Attend lab","Go to workshop","Group meeting",
        "Do quiz online","See convener","Go to drop-in session","Send emails to tutor","Attend exam",
                "Prepare exam","Do assignment","Check the wattle","Download course material"};
        // 遍歷日期陣列，為每個日期建立待辦事項列表
        for(int i=0;i< dates.length;i++){
            ArrayList<String>todoList=new ArrayList<>();
            // 為每個日期分配部分待辦事項
            for(int j=i;j<todoLists.length;j+=2){
                todoList.add(todoLists[j]);
            }
            // 將日期和對應的待辦事項列表存入 Map
            todoListMap.put(dates[i],todoList);
        }
    }

    /**
    * 在 Fragment 的視圖被銷毀之前清理資源。
    * 它會呼叫父類的 onDestroyView() 方法來執行任何必要的清理，
    * 然後將 Fragment 的 binding 物件設為 null 以釋放視圖綁定資源。
    */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // 釋放 binding 物件，避免記憶體洩漏
    }

    /**
     * 將年、月、日格式化為 "yyyy-MM-dd" 格式的日期字串。
     *
     * @param year 年份。
     * @param month 月份 (0-11，0 代表一月)。
     * @param dayOfMonth 日期。
     * @return 格式化後的日期字串。
     */
    private String formatDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        // 設定日曆的年、月、日
        calendar.set(year, month, dayOfMonth);

        // 定義日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 格式化日期並回傳字串
        return sdf.format(calendar.getTime());
    }

    /**
     * 根據選定的日期顯示待辦事項列表。
     * 如果選定日期存在待辦事項，則將其顯示在 {@code todoTextView} 中；
     * 否則，顯示 "No ToDo List for selected date"。
     *
     * @param selectedDate "yyyy-MM-dd" 格式的選定日期字串。
     */
    private void displayToDoList(String selectedDate) {
        // 檢查 Map 中是否包含選定日期的待辦事項
        if (todoListMap.containsKey(selectedDate)) {
            // 獲取選定日期的待辦事項列表
            ArrayList<String> todoList = todoListMap.get(selectedDate);

            StringBuilder stringBuilder = new StringBuilder();
            // 遍歷待辦事項列表，建立顯示字串
            for (String todoItem : todoList) {
                stringBuilder.append("- ").append(todoItem).append("\n");
            }
            // 在 TextView 中顯示待辦事項列表
            todoTextView.setText(stringBuilder.toString());
        } else {
            // 如果沒有待辦事項，則顯示提示訊息
            todoTextView.setText("No ToDo List for selected date");
        }
    }

}