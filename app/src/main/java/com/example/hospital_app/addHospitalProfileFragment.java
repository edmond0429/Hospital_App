package com.example.hospital_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import androidx.fragment.app.Fragment;


public class addHospitalProfileFragment extends Fragment {

    EditText mHospitalName, mHospitalAddress, mContactNo, mHospitalUrl;
    Button btnAddBook;
    DatabaseReference mDatabaseReference;
    private ChipNavigationBar adminNav;
    private Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_add_hospital_profile,container,false);

        mHospitalName = (EditText)view.findViewById(R.id.etHospitalName);
        mHospitalAddress = (EditText)view.findViewById(R.id.etHospitalAddress);
        mContactNo = (EditText)view.findViewById(R.id.etContactNo);
        mHospitalUrl = (EditText)view.findViewById(R.id.etHospitalUrl);
        btnAddBook = (Button)view.findViewById(R.id.btnAdd);

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hospitalName = mHospitalName.getText().toString();
                String hospitalAddress = mHospitalAddress.getText().toString();
                String contactNo = mContactNo.getText().toString();
                String hospitalUrl = mHospitalUrl.getText().toString();

                FetchHospitalProfile mFetchHospital = new FetchHospitalProfile(hospitalName,hospitalAddress,contactNo,hospitalUrl);
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("hospital");
                mDatabaseReference.push().setValue(mFetchHospital);
                Toast.makeText(getActivity(), "Hospital Profile added", Toast.LENGTH_SHORT).show();
            }
        });

        adminNav = view.findViewById(R.id.adminChipNavigation);
        adminNav.setItemSelected(R.id.addHospitalProfile,true);
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
}