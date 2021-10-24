package com.example.hospital_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class snoozePage extends AppCompatActivity {
    private TextView mTvTitle, mTvTime;
    Button btnHomePage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze_page);

        mTvTitle = findViewById(R.id.tvTitle);
        mTvTime = findViewById(R.id.tvalarmtime);
        btnHomePage = findViewById(R.id.btnHome);

        // get the string alarmtitle and time
        String title = getIntent().getExtras().getString("alarmtitle");
        String time = getIntent().getExtras().getString("time");

        mTvTitle.setText(title);
        mTvTime.setText(time);

        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
            }
        });

    }
}