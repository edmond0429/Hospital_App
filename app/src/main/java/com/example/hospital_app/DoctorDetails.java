package com.example.hospital_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class DoctorDetails extends AppCompatActivity {

    TextView tvStory;
    Button btnAddBooking;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        tvStory = findViewById(R.id.TStory);
        Intent i = this.getIntent();
        Bundle bundle = i.getExtras();
        FetchData fetchData = (FetchData) bundle.getSerializable("key");
        tvStory.setText(fetchData.getDoctorDescription().replace("_b","\n"));

        btnAddBooking = (Button)findViewById(R.id.btnBooking);
        btnAddBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorDetails.this,userMakeBooking.class);
                intent.putExtra("fetchData", fetchData);
                startActivity(intent);
            }
        });
    }


}