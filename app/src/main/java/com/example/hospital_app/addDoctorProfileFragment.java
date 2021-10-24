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

public class addDoctorProfileFragment extends Fragment {

    EditText mHospitalName, mCategory, etDoctorName, etDoctorDescription, mDoctorUrl, etDoctorTime;
    Button btnAddBook;
    DatabaseReference mDatabaseReference;
    private ChipNavigationBar adminNav;
    private Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_add_doctor_profile,container,false);

        mHospitalName = (EditText)view.findViewById(R.id.etHospitalName);
        mCategory = (EditText)view.findViewById(R.id.etCategory);
        etDoctorName = (EditText)view.findViewById(R.id.etDoctorName);
        etDoctorDescription = (EditText)view.findViewById(R.id.etDoctorDescription);
        mDoctorUrl = (EditText)view.findViewById(R.id.etDoctorUrl);
        etDoctorTime= (EditText)view.findViewById(R.id.etDoctorTime);
        btnAddBook = (Button)view.findViewById(R.id.btnAdd);

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hospitalName = mHospitalName.getText().toString();
                String category = mCategory.getText().toString();
                String doctorName = etDoctorName.getText().toString();
                String doctorDescription = etDoctorDescription.getText().toString();
                String doctorUrl = mDoctorUrl.getText().toString();
                String doctorTime = etDoctorTime.getText().toString();

                FetchData mFetchDoctor = new FetchData(hospitalName,category,doctorName,doctorDescription,doctorUrl,doctorTime);
                mDatabaseReference = FirebaseDatabase.getInstance().getReference("Doctor");
                mDatabaseReference.child(doctorName).setValue(mFetchDoctor);
                Toast.makeText(getActivity(), "Doctor added", Toast.LENGTH_SHORT).show();
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
}