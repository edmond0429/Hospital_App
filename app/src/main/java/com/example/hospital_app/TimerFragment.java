package com.example.hospital_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.tuann.floatingactionbuttonexpandable.FloatingActionButtonExpandable;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TimerFragment extends Fragment {

    ArrayList<MedicAlarm> medicList;
    RecyclerView mRecyclerView;
    AlarmAdapter mAlarmAdapter;
    DatabaseReference mDatabaseReference, ref2;
    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FloatingActionButtonExpandable fabAdd;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        mRecyclerView = view.findViewById(R.id.rvAlarm);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Alarm");
        ref2 = mDatabaseReference.child(mAuth.getCurrentUser().getUid());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        fabAdd = view.findViewById(R.id.fab);


        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicList = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MedicAlarm medicAlarm = dataSnapshot.getValue(MedicAlarm.class);
                    medicList.add(medicAlarm);
                }
                mAlarmAdapter = new AlarmAdapter(getActivity(),medicList);
                mRecyclerView.setAdapter(mAlarmAdapter);
                mAlarmAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ItemTouchHelper mHelper = new ItemTouchHelper(mCallback);
        mHelper.attachToRecyclerView(mRecyclerView);

        setUpFab();

        chipNavigationBar = view.findViewById(R.id.chipNavigation);
            chipNavigationBar.setItemSelected(R.id.timer,true);
            chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener(){
                @Override
                public void onItemSelected(int i) {
                    switch (i) {
                        case R.id.home:
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
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

    ItemTouchHelper.SimpleCallback mCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Snackbar snackbar = Snackbar.make(getView(),"Selected Alarm Deleted",Snackbar.LENGTH_LONG);
            snackbar.show();

            final MedicAlarm medicAlarmPosition = medicList.get(viewHolder.getAdapterPosition());
            medicList.remove(viewHolder.getAdapterPosition());
            DatabaseReference dbDelete = mDatabaseReference
                    .child(mAuth.getCurrentUser().getUid()).child(medicAlarmPosition.getMedicTitle());
            dbDelete.removeValue();
            mAlarmAdapter.notifyDataSetChanged();
        }
    };

    private void setUpFab() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), addAlarm.class);
                startActivity(intent);
            }
        });
    }
}
