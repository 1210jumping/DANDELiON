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
 * @author: u7518626 Pin-Shen Chang 
 * Show the Calendar and display the ToDo List
 * @return
 */
public class DeadlineshowFragment extends Fragment {

    private FragmentDeadlineBinding binding;
    private CalendarView calendarView;
    private Map<String, ArrayList<String>>todoListMap=new HashMap<>();
    private TextView todoTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DeadlineshowViewModel deadlineshowViewModel =
                new ViewModelProvider(this).get(DeadlineshowViewModel.class);
        binding = FragmentDeadlineBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        calendarView=binding.calender;
        getData();
        final TextView textView = binding.textCalender;
        deadlineshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        todoTextView = root.findViewById(R.id.todolist);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                String selectedDate = formatDate(year, month, dayOfMonth);
                displayToDoList(selectedDate);
            }
        });

        return root;
    }

    // function to get Data
    private void getData(){
        String[] dates = {"2025-05-11", "2025-05-12", "2025-05-13", "2025-05-14",
                "2025-06-02", "2025-06-16", "2025-06-23", "2025-06-27",
                "2025-07-06", "2025-07-09", "2025-07-19", "2025-07-22"};
        String[]todoLists={"Finish assignment","Listen to recording","Attend lab","Go to workshop","Group meeting",
        "Do quiz online","See convener","Go to drop-in session","Send emails to tutor","Attend exam",
                "Prepare exam","Do assignment","Check the wattle","Download course material"};
        for(int i=0;i< dates.length;i++){
            ArrayList<String>todoList=new ArrayList<>();
            for(int j=i;j<todoLists.length;j+=2){
                todoList.add(todoLists[j]);
            }
            todoListMap.put(dates[i],todoList);
        }
    }

    /**
    * Cleans up the fragment's view resources before it is destroyed.
    * It calls the superclass's onDestroyView() method to perform any necessary cleanup,
    * and then sets the fragment's binding object to null to release the view binding resources.
    */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // formatted date string
    private String formatDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    // Display ToDo List
    private void displayToDoList(String selectedDate) {
        if (todoListMap.containsKey(selectedDate)) {
            ArrayList<String> todoList = todoListMap.get(selectedDate);

            StringBuilder stringBuilder = new StringBuilder();
            for (String todoItem : todoList) {
                stringBuilder.append("- ").append(todoItem).append("\n");
            }

            todoTextView.setText(stringBuilder.toString());
        } else {
            todoTextView.setText("No ToDo List for selected date");
        }
    }

}
