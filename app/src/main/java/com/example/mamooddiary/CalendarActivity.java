package com.example.mamooddiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener, View.OnClickListener{

    private TextView monthTextview, yearTextview;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private LocalDate oldDate;

    //ImageView previousButton, nextButton;
    TextView btnBackToSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();
        closeActionBar();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecycleView);

        monthTextview = findViewById(R.id.monthTextView);
        monthTextview.setOnClickListener(this);
        yearTextview = findViewById(R.id.yearTextview);
        yearTextview.setOnClickListener(this);

        btnBackToSplash = (TextView) findViewById(R.id.btnBackToSplash);
        btnBackToSplash.setOnClickListener(this);

        // Add the swipe gesture listener to the calendar RecyclerView
        final GestureDetector gestureDetector = new GestureDetector(this, new SwipeGestureListener());
        calendarRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return gestureDetector.onTouchEvent(e);
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    private void setMonthView() {
        // set month and year text
        monthTextview.setText(selectedDate.getMonth().toString().toUpperCase());
        yearTextview.setText(String.valueOf(selectedDate.getYear()));

        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth,
                selectedDate.getMonthValue() ,
                selectedDate.getYear(),
                this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

    }


    // this return specify position day in calendar
    // If first day of month day is WED
    // return data is { "", "" , "", 1, 2, 3 ... ,31, "" ...}
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        // This return position of day. If not right position it will be "". for example
        // date 1 in march 2023 should be at WED. but the the table start with SUN
        // so when it not right. It add "" to array.
        for(int i = 1; i <= 42; i++) {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            }
            else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }


    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    // when click on date on calendar
    @Override
    public void onItemClick(int position, String dayText, int day, int month, int year) {
        if(!dayText.equals("")) {
            Intent intent = new Intent(this, DiaryActivity.class);
            intent.putExtra("day", day );
            intent.putExtra("month", month);
            intent.putExtra("year", year);

            startActivityForResult(intent, 1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setMonthView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBackToSplash:
                startActivity(new Intent(CalendarActivity.this, SplashActivity.class));
                finish();
                break;
            case R.id.monthTextView:
            case R.id.yearTextview:
                showDatePickerDialog();
                break;
        }
    }

    public void closeActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onBackPressed(){

    }

    private void showDatePickerDialog() {
        MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog(
                this,
                selectedDate.getMonthValue(),
                selectedDate.getYear(),
                (month, year) -> {
                    // Update the selectedDate with the selected month and year
                    oldDate = selectedDate;
                    selectedDate = LocalDate.of(year, month, 1);
                    startAnimation(selectedDate, oldDate);
                    setMonthView();
                }
        );

        // Show the MonthYearPickerDialog
        monthYearPickerDialog.show();
    }

    private void onSwipeLeft() {

        if (selectedDate.plusMonths(1).isAfter(LocalDate.now())) return;
        oldDate = selectedDate;
        selectedDate = selectedDate.plusMonths(1);
        startAnimation(selectedDate, oldDate);
        setMonthView();
    }

    private void onSwipeRight() {
        oldDate = selectedDate;
        selectedDate = selectedDate.minusMonths(1);
        startAnimation(selectedDate, oldDate);
        setMonthView();
    }


    class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 100;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private void startAnimation(LocalDate selectedDate, LocalDate oldDate) {
        // animation
        LinearLayout innerLayout = findViewById(R.id.calendarLayout);

        Animation slideIn, slideOut;

        if (oldDate == null) {
            innerLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.popup));
            return;
        } else if (selectedDate != null && selectedDate.isBefore(oldDate)) {
            slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
            slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        } else {
            slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
            slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
            innerLayout.startAnimation(slideIn);
        }

        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Set the view visibility to VISIBLE before starting slide-in animation
                calendarRecyclerView.setVisibility(View.VISIBLE);
                innerLayout.startAnimation(slideIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        // Set the view visibility to INVISIBLE before starting slide-out animation
        calendarRecyclerView.setVisibility(View.INVISIBLE);
        innerLayout.startAnimation(slideOut);
    }

}


