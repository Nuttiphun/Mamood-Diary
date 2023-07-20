package com.example.mamooddiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> dayOfMonth;
    int counterDateOfMonth;
    private final int monthOfDay;
    private final int yearOfDay;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<String> dayOfMonth,
                           int monthOfDay,
                           int yearOfDay,
                           OnItemListener onItemListener) {
        this.dayOfMonth = dayOfMonth;
        this.monthOfDay = monthOfDay;
        this.yearOfDay = yearOfDay;
        this.onItemListener = onItemListener;
        counterDateOfMonth = 0;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Retrieve the day value based on the position
        String dayStr = dayOfMonth.get(counterDateOfMonth);

//        int day = dayStr.equals("") ? -1 : Integer.parseInt(dayStr);
        int day;
        // make it empty cell if that date in cell is after date.now()
        if ( dayStr.equals("") ||
                LocalDate.of(
                        yearOfDay,
                        monthOfDay,
                        Integer.parseInt(dayOfMonth.get(counterDateOfMonth))
                ).isAfter(LocalDate.now()) )  {
            day = -1;
        }
        else {
            day = Integer.parseInt(dayStr);
        }

        // set calendar cells
        LayoutInflater inflater;
        View view = null;
        inflater = LayoutInflater.from(parent.getContext());

        String mood = "";
        // getMoodTypeByDate
        if(day != -1)
            mood = CalendarViewHolder.getMoodTypeByDate(parent.getContext() ,Integer.parseInt(dayOfMonth.get(counterDateOfMonth)), monthOfDay,  yearOfDay);


        // non day
        if ( day == -1 ){
            view = inflater.inflate(R.layout.calendar_empty_cell, parent, false);
        }
        else if(!CalendarViewHolder.isDairyDay(parent.getContext(),day, monthOfDay, yearOfDay)){
            view = inflater.inflate(R.layout.calendar_default_cell, parent, false);
        }else if (mood.equals("happy")){
            view = inflater.inflate(R.layout.calendar_happy_cell, parent, false);
        }else if (mood.equals("notok")){
            view = inflater.inflate(R.layout.calendar_notok_cell, parent, false);
        }else if (mood.equals("sad")){
            view = inflater.inflate(R.layout.calendar_sad_cell, parent, false);
        }else if (mood.equals("smile")){
            view = inflater.inflate(R.layout.calendar_smile_cell, parent, false);
        }else{
            view = inflater.inflate(R.layout.calendar_normal_cell, parent, false);
        }

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);

        counterDateOfMonth++;
        return new CalendarViewHolder(view, onItemListener, day, monthOfDay, yearOfDay);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        if(dayOfMonth.get(position) != null)
            holder.dayOfMonth.setText(dayOfMonth.get(position));
    }

    @Override
    public int getItemCount() {
        return dayOfMonth.size();
    }
    public interface OnItemListener{
        void onItemClick(int position, String dayText, int day, int month, int year);
    }
}
