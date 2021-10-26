package com.example.hospital_app;

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

public class ManageBookingRequest extends AppCompatActivity {

    RecyclerView mRecyclerView;
    DatabaseReference mDatabaseReference;
    bookRequestAdapter mBookRequestAdapter;
    ArrayList<Booking> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_booking_request);

        mRecyclerView = findViewById(R.id.rvRequestBooking);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("RequestBooking");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Booking request = dataSnapshot.getValue(Booking.class);
                    requestList.add(request);
                }
                mBookRequestAdapter = new bookRequestAdapter(getApplicationContext(),requestList);
                mRecyclerView.setAdapter(mBookRequestAdapter);
                mBookRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}