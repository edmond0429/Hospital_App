package com.example.hospital_app;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class userViewAcceptedBooking extends AppCompatActivity {

    RecyclerView mRecyclerView;
    DatabaseReference mDatabaseReference, ref2;
    ViewAcceptedAdapter mViewAcceptedAdapter;
    ArrayList<Booking> acceptList;

    private FirebaseUser user;
    private String userID;
    private DatabaseReference profileRef;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_accepted_booking);

        mRecyclerView = findViewById(R.id.rvUserAcceptBooking);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Accepted Booking");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        profileRef = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        profileRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.child("fullName").getValue().toString();
                getBooking();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void getBooking(){
        ref2 = mDatabaseReference.child(currentUser);
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                acceptList = new ArrayList<Booking>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Booking request = dataSnapshot.getValue(Booking.class);
                    acceptList.add(request);
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