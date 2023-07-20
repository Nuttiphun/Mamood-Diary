package com.example.mamooddiary;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnStart, btnLearnMore;
    MediaPlayer mediaPlayer;

    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        btnLearnMore = (Button) findViewById(R.id.btnLearnMore);
        btnLearnMore.setOnClickListener(this);

        mediaPlayer = MediaPlayer.create(this, R.raw.click);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(mediaPlayer != null)
                    mediaPlayer.release();
            }
        });


        closeActionBar();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnStart) {
            mediaPlayer.start();
            startActivity(new Intent(SplashActivity.this, CalendarActivity.class));
            finish();
        }
        if (view.getId() == R.id.btnLearnMore) {
            mediaPlayer.start();
            startActivity(new Intent(SplashActivity.this, MoodtrackerActivity.class));
            finish();
        }
    }

    public void closeActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

}
