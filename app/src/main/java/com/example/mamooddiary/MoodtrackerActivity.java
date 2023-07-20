package com.example.mamooddiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MoodtrackerActivity extends AppCompatActivity implements View.OnClickListener {

    TextView btnBackToSplashFromLearnMore;

    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moodtracker);


        btnBackToSplashFromLearnMore = (TextView) findViewById(R.id.btnBackToSplashFromLearnMore);
        btnBackToSplashFromLearnMore.setOnClickListener(this);

        closeActionBar();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btnBackToSplashFromLearnMore) {
            startActivity(new Intent(MoodtrackerActivity.this, SplashActivity.class));
            finish();
        }
    }

    public void closeActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}


