package com.example.mamooddiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;


public class DiaryActivity extends AppCompatActivity implements View.OnClickListener {
    DBHelper dbh;
    private int day, month, year;
    TextView diaryBackButton, dateInfoTextView;
    ImageButton moodButton5,moodButton1,moodButton2,moodButton3,moodButton4;

    Button diarySaveButton;
    String mood, diaryString;

    EditText diaryEditText;

    Intent intent;

    MediaPlayer mediaPlayer;

    QueryType queryType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        init();
        closeActionBar();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        setMoodById(v.getId());
        switch (v.getId()){
            case R.id.diaryBackButton:
                finish();
                break;
            case R.id.diarySaveButton:
                if (!queryDatabase()){
                    return;
                }
                mediaPlayer.start();
                finish();
                break;
        }
    }
    public String setMoodById(int id){
        switch (id){
            case R.id.happyImageButton:
                mood = "happy";
                unselectedButton();
                moodButton1.setSelected(true);
                break;
            case R.id.smileImageButton:
                mood = "smile";
                unselectedButton();
                moodButton2.setSelected(true);
                break;
            case R.id.normalImageButton:
                mood = "normal";
                unselectedButton();
                moodButton3.setSelected(true);
                break;
            case R.id.notOkImageButton:
                mood = "notok";
                unselectedButton();
                moodButton4.setSelected(true);
                break;
            case R.id.sadImageButton:
                mood = "sad";
                unselectedButton();
                moodButton5.setSelected(true);
                break;
        }
        return mood;
    }

    public void setMoodButton(String mood){
        if (mood == null) return;
        switch (mood){
            case "happy":
                unselectedButton();
                moodButton1.setSelected(true);
                break;
            case "smile":
                unselectedButton();
                moodButton2.setSelected(true);
                break;
            case "normal":
                unselectedButton();
                moodButton3.setSelected(true);
                break;
            case "notok":
                unselectedButton();
                moodButton4.setSelected(true);
                break;
            case "sad":
                unselectedButton();
                moodButton5.setSelected(true);
                break;
        }
    }

    public void unselectedButton(){
        moodButton1.setSelected(false);
        moodButton2.setSelected(false);
        moodButton3.setSelected(false);
        moodButton4.setSelected(false);
        moodButton5.setSelected(false);
    }

    public void closeActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void init() {
        intent = getIntent();
        day = intent.getIntExtra("day", -1);
        month = intent.getIntExtra("month", -1);
        year = intent.getIntExtra("year", -1);

        initUI();
        initDatabase();
        initMediaPlayer();

        LocalDate date = LocalDate.of(year, month, day);
        if (queryType == QueryType.UPDATE){
            setMoodButton(mood);
            diaryEditText.setText(diaryString);
        }
        dateInfoTextView.setText(
                date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + ", " +
                        date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " +
                        date.getDayOfMonth()
        );

    }

    private void initUI() {
        diaryBackButton = findViewById(R.id.diaryBackButton);
        diarySaveButton = findViewById(R.id.diarySaveButton);
        diaryBackButton.setOnClickListener(this);
        diarySaveButton.setOnClickListener(this);

        diaryEditText = findViewById(R.id.diaryEditText);

        moodButton1 = findViewById(R.id.happyImageButton);
        moodButton2 = findViewById(R.id.smileImageButton);
        moodButton3 = findViewById(R.id.normalImageButton);
        moodButton4 = findViewById(R.id.notOkImageButton);
        moodButton5 = findViewById(R.id.sadImageButton);
        moodButton1.setOnClickListener(this);
        moodButton2.setOnClickListener(this);
        moodButton3.setOnClickListener(this);
        moodButton4.setOnClickListener(this);
        moodButton5.setOnClickListener(this);

        dateInfoTextView=findViewById(R.id.dateInfoTextView);
    }

    private void initDatabase() {
        dbh = new DBHelper(this);
        initQueryType();
    }

    private void initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.click);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer != null)
                    mediaPlayer.release();
            }
        });
    }
    private void initQueryType(){
        // return data is { "message", "mood", "true" or "false" }
        String[] diarys = dbh.selectDiary(String.valueOf(day), String.valueOf(month), String.valueOf(year));

        diaryString = diarys[0];
        mood = diarys[1];

        if(diarys[2].equals("false")){
            queryType = QueryType.INSERT;
        }else
            queryType = QueryType.UPDATE;

    }

    private enum QueryType{
        INSERT,
        UPDATE
    }

    private Boolean queryDatabase(){
        switch (queryType){
            case INSERT:
                if( mood.isEmpty() ){
                    Toast.makeText(this, "โปรดเลือก \"Mood Tracker\" ก่อน Save", Toast.LENGTH_LONG).show();
                    return false;
                }
                dbh.AddData(
                        String.valueOf( day ),
                        String.valueOf( month ),
                        String.valueOf( year ),
                        diaryEditText.getText().toString(),
                        mood);
                break;
            case UPDATE:
                dbh.updateDiary(
                        String.valueOf( day ),
                        String.valueOf( month ),
                        String.valueOf( year ),
                        diaryEditText.getText().toString(), mood);
                break;
        }

        return true;
    }
}
