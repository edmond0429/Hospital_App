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
import android.widget.Toast;

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

public class addDoctorProfileFragment extends Fragment {

    TextView mHospitalName;
    EditText  mCategory, etDoctorName, etDoctorDescription, mDoctorUrl, etDoctorTime;
    Button btnAddBook;
    DatabaseReference mDatabaseReference, hospRef;
    private ChipNavigationBar adminNav;
    private Fragment fragment = null;
    private String hospitalName="",category="",doctorName="",doctorDescription="",doctorUrl="",doctorTime="", hospName;
    Spinner mSpinnerHosp;
    ArrayList<String> spinnerHospitalList;
    ArrayAdapter<String> adapterHospital;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_add_doctor_profile,container,false);

        mHospitalName = (TextView)view.findViewById(R.id.TVHospitalName);
        mCategory = (EditText)view.findViewById(R.id.etCategory);
        etDoctorName = (EditText)view.findViewById(R.id.etDoctorName);
        etDoctorDescription = (EditText)view.findViewById(R.id.etDoctorDescription);
        mDoctorUrl = (EditText)view.findViewById(R.id.etDoctorUrl);
        etDoctorTime= (EditText)view.findViewById(R.id.etDoctorTime);
        btnAddBook = (Button)view.findViewById(R.id.btnAdd);
        mSpinnerHosp = (Spinner)view.findViewById(R.id.spinnerHospital);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Doctor");
        hospRef = FirebaseDatabase.getInstance().getReference("hospital");

        spinnerHospitalList = new ArrayList<>();
        adapterHospital = new ArrayAdapter<String>(getContext(),R.layout.my_selected_item,spinnerHospitalList);
        adapterHospital.setDropDownViewResource(R.layout.my_dropdown_item);
        mSpinnerHosp.setAdapter(adapterHospital);
        showHospital();

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 hospitalName = mHospitalName.getText().toString();
                 category = mCategory.getText().toString();
                 doctorName = etDoctorName.getText().toString();
                 doctorDescription = etDoctorDescription.getText().toString();
                 doctorUrl = mDoctorUrl.getText().toString();
                 doctorTime = etDoctorTime.getText().toString();

                if(hospitalName.isEmpty()){
                    mHospitalName.setError("Hospital name is required!");
                    mHospitalName.requestFocus();
                    return;
                }
                if(category.isEmpty()){
                    mCategory.setError("Doctor category is required!");
                    mCategory.requestFocus();
                    return;
                }
                if(doctorName.isEmpty()){
                    etDoctorName.setError("Doctor name is required!");
                    etDoctorName.requestFocus();
                    return;
                }
                if(doctorDescription.isEmpty()){
                    etDoctorDescription.setError("Doctor description is required!");
                    etDoctorDescription.requestFocus();
                    return;
                }
                if(doctorUrl.isEmpty()){
                    mDoctorUrl.setError("Doctor picture is required!");
                    mDoctorUrl.requestFocus();
                    return;
                }
                if(doctorTime.isEmpty()){
                    etDoctorTime.setError("Doctor working time is required!");
                    etDoctorTime.requestFocus();
                    return;
                }

                FetchData mFetchDoctor = new FetchData(hospitalName,category,doctorName,doctorDescription,doctorUrl,doctorTime);
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot doctor:snapshot.getChildren()) {
                            if(doctor.toString() == mFetchDoctor.toString()){
                                Toast.makeText(getActivity(), "This doctor profile had been created, please re-create a doctor profile!", Toast.LENGTH_SHORT).show();
                            }else{
                                mDatabaseReference.child(doctorName).setValue(mFetchDoctor);
                                MotionToast.Companion.darkColorToast(getActivity(),"Added Doctor Successfully!",
                                        "You had added a doctor profile!",
                                        MotionToast.TOAST_SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(getContext(),R.font.helvetica_regular));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        adminNav = view.findViewById(R.id.adminChipNavigation);
        adminNav.setItemSelected(R.id.addDoctor,true);
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
        mSpinnerHosp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hospName = mSpinnerHosp.getSelectedItem().toString();
                mHospitalName.setText(hospName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}