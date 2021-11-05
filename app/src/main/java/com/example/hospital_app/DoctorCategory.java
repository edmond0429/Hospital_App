package com.example.hospital_app;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorCategory extends AppCompatActivity {

    RecyclerView mRecyclerViewDoctorCategory;
    HelperAdapter mHelperAdapater;
    DatabaseReference mDatabaseReference;
    ArrayList<FetchData> mFetchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_category);

        mRecyclerViewDoctorCategory = findViewById(R.id.recyclerViewDoctorCategory);
        Intent i = this.getIntent();
        Bundle bundle = i.getExtras();
        FetchHospitalProfile fetchHospitalProfiles = (FetchHospitalProfile) bundle.getSerializable("key1"); //Pass previous hospital profile details to this activity
        String hospName = bundle.getString("key2");       //pass the hospital name
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Doctor");
        mRecyclerViewDoctorCategory.setLayoutManager(new LinearLayoutManager(this));
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFetchData = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    FetchData fetchDataList = ds.getValue(FetchData.class);
                    if(fetchDataList.getHospitalName().equals(hospName)) {    //check for list of hospital doctor
                        mFetchData.add(fetchDataList);
                    }
                }

                mHelperAdapater = new HelperAdapter(mFetchData);
                mRecyclerViewDoctorCategory.setAdapter(mHelperAdapater);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}