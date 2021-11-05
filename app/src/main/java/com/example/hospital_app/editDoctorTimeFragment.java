package com.example.hospital_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import www.sanju.motiontoast.MotionToast;

public class editDoctorTimeFragment extends Fragment {

    TextView mTvSelectedDoctor, mTvViewDoctor, mTvViewDoctorTime;
    Spinner mSpinnerDoctor, mSpinnerViewDoctor;
    EditText mEtTime;
    Button btnEditTime;
    DatabaseReference mDatabaseReference;
    ArrayList<String> spinnerDoctorList, spinnerViewDoctor;
    ArrayAdapter<String> adapterDoctor, adapaterViewDoctor;
    private ChipNavigationBar adminNav;
    private Fragment fragment = null;
    String doctor, viewdoctor, time, workingTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_edit_doctor_time,container,false);

        mSpinnerDoctor = (Spinner)view.findViewById(R.id.spinnerDoctor);
        mSpinnerViewDoctor = (Spinner)view.findViewById(R.id.spinnerViewDoctor);
//        mSpinnerViewTime = (Spinner)view.findViewById(R.id.spinnerViewTime);
        mEtTime = (EditText)view.findViewById(R.id.etAddDoctorTime);
        btnEditTime = (Button)view.findViewById(R.id.btnEditTime);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Doctor");
        mTvSelectedDoctor = (TextView)view.findViewById(R.id.tvSelectedDoctor);
        mTvViewDoctor = (TextView)view.findViewById(R.id.tvViewDoctor);
        mTvViewDoctorTime = (TextView)view.findViewById(R.id.tvTime);

        spinnerDoctorList = new ArrayList<>();
        adapterDoctor = new ArrayAdapter<String>(getContext(),R.layout.my_selected_item,spinnerDoctorList);
        adapterDoctor.setDropDownViewResource(R.layout.my_dropdown_item);

        spinnerViewDoctor = new ArrayList<>();
        adapaterViewDoctor = new ArrayAdapter<String>(getContext(),R.layout.my_selected_item,spinnerViewDoctor);
        adapaterViewDoctor.setDropDownViewResource(R.layout.my_dropdown_item);

        mSpinnerDoctor.setAdapter(adapterDoctor);
        showDoctor();
        mSpinnerViewDoctor.setAdapter(adapaterViewDoctor);
        ShowViewDoctor();

        btnEditTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addScheduleTime = mEtTime.getText().toString();
                if (addScheduleTime.isEmpty()) {
                    mEtTime.setError("Doctor's working time is required!");
                    mEtTime.requestFocus();
                    return;
                }

                mDatabaseReference.child(doctor).child("time").setValue(addScheduleTime);
                mEtTime.setText("");
                spinnerViewDoctor.clear();
                spinnerDoctorList.clear();
                adapterDoctor.notifyDataSetChanged();
                adapaterViewDoctor.notifyDataSetChanged();
                MotionToast.Companion.darkColorToast(getActivity(),"Edited doctor's working hour!",
                        "Edited a new working time for doctor!",
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(getContext(),R.font.helvetica_regular));

            }
        });

        adminNav = view.findViewById(R.id.adminChipNavigation);
        adminNav.setItemSelected(R.id.adminEditDoctorTime,true);
        adminNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener(){
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.addHospitalProfile:
                        fragment = new addHospitalProfileFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case R.id.addDoctor:
                        fragment = new addDoctorProfileFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case R.id.addBooking:
                        fragment = new addBookingFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case R.id.adminEditDoctorTime:
                        fragment = new editDoctorTimeFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case R.id.adminProfile:
                        fragment = new adminLogInFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                }
            }
        });
        return view;
    }

    private void showDoctor() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot doctor:snapshot.getChildren()){
                    spinnerDoctorList.add(doctor.child("doctorName").getValue().toString());
                }
                adapterDoctor.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mSpinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                doctor = mSpinnerDoctor.getSelectedItem().toString();
                mTvSelectedDoctor.setText(doctor);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void ShowViewDoctor() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot viewdoctor : snapshot.getChildren()) {
                        spinnerViewDoctor.add(viewdoctor.child("doctorName").getValue().toString());
                    }
                    adapaterViewDoctor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        mSpinnerViewDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewdoctor = mSpinnerViewDoctor.getSelectedItem().toString();
                mTvViewDoctor.setText(viewdoctor);
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        time = snapshot.child(viewdoctor).child("time").getValue().toString();
                        mTvViewDoctorTime.setText(time);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


}