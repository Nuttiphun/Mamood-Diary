package com.example.mamooddiary;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import java.lang.reflect.Field;
import java.text.DateFormatSymbols;
import java.util.Calendar;

public class MonthYearPickerDialog {
    private final Context context;
    private final OnMonthYearSelectedListener listener;
    private final Calendar calendar;
    private final String[] monthNames;
    private final int inputMonth, inputYear;

    public MonthYearPickerDialog(Context context, int inputMonth, int inputYear, OnMonthYearSelectedListener listener) {
        this.context = context;
        this.listener = listener;
        this.calendar = Calendar.getInstance();
        this.inputMonth = inputMonth - 1;
        this.inputYear = inputYear;
        this.monthNames = new DateFormatSymbols().getMonths();
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.month_year_picker_dialog, null);

        NumberPicker monthPicker = view.findViewById(R.id.month_picker);
        NumberPicker yearPicker = view.findViewById(R.id.year_picker);

        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);
        monthPicker.setDisplayedValues(monthNames);

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);

        yearPicker.setMinValue(currentYear - 100);
        yearPicker.setMaxValue(currentYear);
        yearPicker.setValue(inputYear);

        // Set the max value of the monthPicker based on the selected year
        yearPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (newVal == currentYear) {
                monthPicker.setMaxValue(currentMonth);
            } else {
                monthPicker.setMaxValue(11);
            }
        });

        // Initialize the monthPicker based on the input date and selected year
        if (inputYear == currentYear) {
            monthPicker.setMaxValue(currentMonth);
        } else {
            monthPicker.setMaxValue(11);
        }
        monthPicker.setValue(inputMonth);

        builder.setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    int selectedMonth = monthPicker.getValue();
                    int selectedYear = yearPicker.getValue();
                    listener.onMonthYearSelected(selectedMonth + 1, selectedYear); // Add 1 to the selected month

                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    public interface OnMonthYearSelectedListener {
        void onMonthYearSelected(int month, int year);
    }

}
