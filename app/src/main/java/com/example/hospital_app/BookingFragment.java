package com.example.hospital_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookingFragment extends Fragment {

    RecyclerView mRecyclerView;
    ArrayList<FetchHospitalProfile> mFetchData;
    HelperAdapter0 mHelperAdapter;
    DatabaseReference mDatabaseReference;
    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_booking,container,false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mFetchData = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("hospital");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    FetchHospitalProfile fetchHospitalList = ds.getValue(FetchHospitalProfile.class);
                    mFetchData.add(fetchHospitalList);
                }
                mHelperAdapter = new HelperAdapter0 (mFetchData);
                mRecyclerView.setAdapter(mHelperAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        chipNavigationBar = view.findViewById(R.id.chipNavigation);
        chipNavigationBar.setItemSelected(R.id.booking,true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener(){
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.home:
                        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getActivity(), UserHomepage.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.booking:
                        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                            fragment = new BlankBookingFragment();
                        }else{
                            fragment = new BookingFragment();
                        }
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case R.id.timer:
                        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                            fragment = new BlankTimeFragment();
                        }else {
                            fragment = new TimerFragment();
                        }
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case R.id.profile:
                        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                            fragment = new LoginProfile();
                        }else{
                            fragment = new UserProfile();
                        }
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                }
            }
        });

        return view;
    }
}