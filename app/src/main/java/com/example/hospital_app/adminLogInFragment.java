package com.example.hospital_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import www.sanju.motiontoast.MotionToast;

public class adminLogInFragment extends Fragment {

    private ChipNavigationBar adminChipNav;
    private Fragment fragment = null;
    private Button logout, editProfile;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_log_in, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView tvFullName = (TextView)view.findViewById(R.id.userName);
        final TextView tvEmail = (TextView)view.findViewById(R.id.emailAddress);
        final TextView tvAge = (TextView)view.findViewById(R.id.age);
        final TextView tvPhoneNo = (TextView)view.findViewById(R.id.phoneNum);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.getValue(User.class);

                if(userProfile != null){
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String age = userProfile.age;
                    String phoneNo = userProfile.phoneNo;

                    tvFullName.setText(fullName);
                    tvEmail.setText(email);
                    tvAge.setText(age);
                    tvPhoneNo.setText(phoneNo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Somethings wrong happened!",Toast.LENGTH_LONG).show();
            }
        });


        logout = (Button)view.findViewById(R.id.signOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Fragment fragment = new LoginProfile();
                MotionToast.Companion.darkColorToast(getActivity(),"Logged Out!","You had logged out!",
                        MotionToast.TOAST_WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(getActivity(),R.font.helvetica_regular));
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });

        editProfile = (Button)view.findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new EditProfileFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });

        adminChipNav = view.findViewById(R.id.adminChipNavigation);
        adminChipNav.setItemSelected(R.id.adminProfile,true);
        adminChipNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener(){
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
                        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                            fragment = new LoginProfile();
                        }else{
                            fragment = new adminLogInFragment();
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