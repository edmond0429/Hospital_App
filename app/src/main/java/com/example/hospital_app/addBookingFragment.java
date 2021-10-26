package com.example.hospital_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import androidx.fragment.app.Fragment;


public class addBookingFragment extends Fragment  {

    Button btnRequestBooking, btnManageBooking, btnViewComplete;
    private ChipNavigationBar adminNav;
    private Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_add_booking,container,false);

        btnRequestBooking = (Button)view.findViewById(R.id.btnAddBooking);
        btnManageBooking = (Button)view.findViewById(R.id.btnManageBooking);
        btnViewComplete = (Button)view.findViewById(R.id.btnViewComplete);

        btnRequestBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),adminMakeRequest.class);
                startActivity(intent);
            }
        });
        btnManageBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ManageBookingRequest.class);
                startActivity(intent);
            }
        });
        btnViewComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ViewAcceptedBooking.class);
                startActivity(intent);
            }
        });


        adminNav = view.findViewById(R.id.adminChipNavigation);
        adminNav.setItemSelected(R.id.addBooking,true);
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