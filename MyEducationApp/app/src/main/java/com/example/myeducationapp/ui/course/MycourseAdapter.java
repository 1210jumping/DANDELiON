package com.example.myeducationapp.ui.course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myeducationapp.DAO.CourseDAO.Course;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;

import java.util.List;
import java.util.Random;
/**
 * @author u7516976 Zixuan Wang
 * CourseHomeActivity class
 * this is an adapter for user to display the courses their selected
 */
public class MycourseAdapter extends RecyclerView.Adapter<MycourseAdapter.ViewHolder> {

    private List<Course> myCourseList;
    private Context context;
    /**
     * constructor
     * @param list
     * @param context
     */
    public MycourseAdapter(List<Course> list, Context context) {
        this.myCourseList = list;
        this.context = context;
    }

    /**
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
//   Creating a ViewHolder object
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int
            viewType) {

        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,
                        false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    /**
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = myCourseList.get(position);
        Random r = new Random();
        int i = r.nextInt(Global.drawables.size());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CourseHomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Course", course);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.course_img.setImageResource(Integer.parseInt(course.getImgUrl()));
        holder.course_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseHomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Course", course);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.course_cno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CourseHomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Course", course);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.course_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CourseHomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Course", course);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.course_title.setText(course.getCname());
        holder.course_cno.setText(course.getCno());
        holder.course_date.setText(course.getReleaseDate());
    }
    /**
     * get item number
     * @return
     */
// Count the all courses
    @Override
    public int getItemCount() {
        return myCourseList.size();
    }
    /**
     * ViewHolder
     */
//  Conjunction with the RecyclerView adapter to hold and manage for each item.
    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView course_img;
        TextView course_title;
        TextView course_cno;
        TextView course_date;

        /**
         * constructor
         * @param itemView
         */

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            course_img = itemView.findViewById(R.id.course_img);
            course_title = itemView.findViewById(R.id.course_title);
            course_cno=itemView.findViewById(R.id.course_ids);
            course_date=itemView.findViewById(R.id.course_dates);
        }
    }
}
