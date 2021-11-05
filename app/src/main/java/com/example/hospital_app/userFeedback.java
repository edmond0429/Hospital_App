package com.example.hospital_app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
import butterknife.ButterKnife;

public class userFeedback extends AppCompatActivity {

    private hospAdapter mHospAdapter;
    private DatabaseReference mDatabaseReference;
    ProgressDialog progressDialog ;
    ArrayList<ReviewModel> mHospitalModelArrayList;
    RecyclerView rvHosp;
    Button btnFeedback;
    private FirebaseUser user;
    private String userID;
    double ratingSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        ButterKnife.bind(this);
        rvHosp = findViewById(R.id.rv_hosp);
        btnFeedback = findViewById(R.id.btnMakeFeedback);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Load hosp data...");
        progressDialog.show();
        rvHosp.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Review");
        mDatabaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                mHospitalModelArrayList = new ArrayList<>();
                ratingSum = 0;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    double rating = Double.parseDouble(""+dataSnapshot.child("totalStarGiven").getValue());
                    ratingSum += rating;

                    ReviewModel model = dataSnapshot.getValue(ReviewModel.class);
                    mHospitalModelArrayList.add(model);
                }
                mHospAdapter = new hospAdapter(getApplicationContext(),mHospitalModelArrayList);
                mHospAdapter.notifyDataSetChanged();
                rvHosp.setAdapter(mHospAdapter);

                long numOfReviewer = snapshot.getChildrenCount();
                double avgRating = ratingSum/numOfReviewer;


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private void openDialog() {
        FeedbackDialog feedbackDialog = new FeedbackDialog();
        feedbackDialog.show(getSupportFragmentManager(),"Feedback dialog");
    }

}