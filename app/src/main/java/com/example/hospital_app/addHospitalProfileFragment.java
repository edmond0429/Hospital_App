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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import www.sanju.motiontoast.MotionToast;


public class addHospitalProfileFragment extends Fragment {

    EditText mHospitalName, mHospitalAddress, mContactNo, mHospitalUrl;
    Button btnAddBook;
    DatabaseReference mDatabaseReference, reviewRef;
    private ChipNavigationBar adminNav;
    private Fragment fragment = null;
    String hospitalName="", hospitalAddress="", contactNo="", hospitalUrl="";

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
                 hospitalName = mHospitalName.getText().toString();
                 hospitalAddress = mHospitalAddress.getText().toString();
                 contactNo = mContactNo.getText().toString();
                 hospitalUrl = mHospitalUrl.getText().toString();

                String phoneRegex = "[6-9][0-9]{9}";    //this mean the first phone number must between 6-9 and rest can be any number
                Matcher phoneMatcher;
                Pattern phonePattern = Pattern.compile(phoneRegex);
                phoneMatcher = phonePattern.matcher(contactNo);

                if(hospitalName.isEmpty()){
                    mHospitalName.setError("Hospital name is required!");
                    mHospitalName.requestFocus();
                    return;
                }
                if(hospitalAddress.isEmpty()){
                    mHospitalAddress.setError("Hospital address is required!");
                    mHospitalAddress.requestFocus();
                    return;
                }
                if(contactNo.isEmpty()){
                    mContactNo.setError("Hospital contact number is required!");
                    mContactNo.requestFocus();
                    return;
                }else if (!phoneMatcher.find()){
                    mContactNo.setError("Hospital contact Number is not valid");
                    mContactNo.requestFocus();
                    return;
                }
                if(hospitalUrl.isEmpty()){
                    mHospitalUrl.setError("Hospital picture is required!");
                    mHospitalUrl.requestFocus();
                    return;
                }

                FetchHospitalProfile mFetchHospital = new FetchHospitalProfile(hospitalName,hospitalAddress,contactNo,hospitalUrl);
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("hospital");
                mDatabaseReference.push().setValue(mFetchHospital);

                HospitalModel model = new HospitalModel(0,0,0,0,0,0,0,hospitalName);
                reviewRef = FirebaseDatabase.getInstance().getReference().child("Feedback");
                reviewRef.push().setValue(model);
                MotionToast.Companion.darkColorToast(getActivity(),"Add hospital profile",
                        "Succesfully added hospital profile!",
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(getActivity(),R.font.helvetica_regular));
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