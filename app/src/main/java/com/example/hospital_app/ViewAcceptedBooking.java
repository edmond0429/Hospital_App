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

public class ViewAcceptedBooking extends AppCompatActivity {

    RecyclerView mRecyclerView;
    DatabaseReference mDatabaseReference;
    ViewAcceptedAdapter mViewAcceptedAdapter;
    ArrayList<Booking> acceptList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accepted_booking);

        mRecyclerView = findViewById(R.id.rvAcceptBooking);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Accepted Booking");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                acceptList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String name = dataSnapshot.getKey();
                    String uid = dataSnapshot.child(name).getKey();

                   for(DataSnapshot temp: snapshot.child(uid).getChildren()){
                       Booking accept = temp.getValue(Booking.class);
                       acceptList.add(accept);
                   }

                }
                mViewAcceptedAdapter = new ViewAcceptedAdapter(getApplicationContext(),acceptList);
                mRecyclerView.setAdapter(mViewAcceptedAdapter);
                mViewAcceptedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}