package com.example.hospital_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FeedbackDialog extends AppCompatDialogFragment {

    private DatabaseReference profileRef, mDatabaseReference, hospRef;
    private FirebaseUser user;
    private String userID;
    TextView tvName, tvHospital;
    EditText etReview;
    MaterialRatingBar mRatingBar;
    String userName;
    Spinner mSpinnerHospital;
    ArrayList<String> spinnerHospitalList;
    ArrayAdapter<String> adapterHospital;
    String hospital;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_review,null);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Review");
        profileRef = FirebaseDatabase.getInstance().getReference("Users");
        hospRef = FirebaseDatabase.getInstance().getReference("hospital");
        mSpinnerHospital = (Spinner)view.findViewById(R.id.spinnerHospital);
        tvHospital = view.findViewById(R.id.tv_hospital);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();


        profileRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.child("fullName").getValue().toString();
                tvName.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinnerHospitalList = new ArrayList<>();
        adapterHospital = new ArrayAdapter<String>(getContext(),R.layout.my_selected_item,spinnerHospitalList);
        adapterHospital.setDropDownViewResource(R.layout.my_dropdown_item);
        mSpinnerHospital.setAdapter(adapterHospital);
        showHospital();


        builder.setView(view).setTitle("Making a Feedback")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "You had cancel to make a feedback", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Send Feedback", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (etReview.getText().toString().isEmpty()) {
                            etReview.setError("Required field");
                            etReview.requestFocus();
                        } else {
                            ReviewModel reviewModel = new ReviewModel();
                            reviewModel.setName(tvName.getText().toString());
                            reviewModel.setReview(etReview.getText().toString());
                            reviewModel.setTotalStarGiven(Math.round(mRatingBar.getRating()));
                            reviewModel.setHospitalName(tvHospital.getText().toString());
                            mDatabaseReference.child(userID).push().setValue(reviewModel);
//                            Intent intent = new Intent(getContext(),userFeedback.class);
//                            startActivity(intent);
                        }
                    }
                });

        tvName = view.findViewById(R.id.tv_name);
        etReview = view.findViewById(R.id.et_review);
        mRatingBar = view.findViewById(R.id.rate_star);
        return builder.create();
    }

    private void showHospital() {
        hospRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot doctor:snapshot.getChildren()){
                    spinnerHospitalList.add(doctor.child("hospitalName").getValue().toString());
                }
                adapterHospital.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mSpinnerHospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hospital = mSpinnerHospital.getSelectedItem().toString();
                tvHospital.setText(hospital);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
