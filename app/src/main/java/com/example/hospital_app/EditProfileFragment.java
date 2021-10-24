package com.example.hospital_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import www.sanju.motiontoast.MotionToast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileFragment extends Fragment {

    private EditText fullName, phoneNo, age;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button update, back;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        fullName = view.findViewById(R.id.editFullName);
        phoneNo = view.findViewById(R.id.editPhoneNumber);
        age = view.findViewById(R.id.editAge);
        update = view.findViewById(R.id.update);
        back = view.findViewById(R.id.btnback);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFirebaseAuth.getCurrentUser().getUid().equals("0yH2dbifT9PMtzM7O99M2mQeZHw2") ) {
                    Fragment fragment = new adminLogInFragment();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }else {
                    Fragment fragment = new UserProfile();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String _USERNAME = fullName.getText().toString();
                final String _PHONENO = phoneNo.getText().toString();
                final String _AGE = age.getText().toString();

                if(_USERNAME.isEmpty()){
                    fullName.setError("Username is required!");
                    fullName.requestFocus();
                    return;
                }
                if(_PHONENO.isEmpty()){
                    phoneNo.setError("Phone number is required!");
                    phoneNo.requestFocus();
                    return;
                }
                if(_AGE.isEmpty()){
                    age.setError("Age is required!");
                    age.requestFocus();
                    return;
                }

                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User userProfile = dataSnapshot.getValue(User.class);

                        reference.child(userID).child("fullName").setValue(_USERNAME);
                        reference.child(userID).child("age").setValue(_AGE);
                        reference.child(userID).child("phoneNo").setValue(_PHONENO);

                        MotionToast.Companion.darkColorToast(getActivity(),"Succesful!","You had edited the profile details",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getActivity(),R.font.helvetica_regular));
                        Fragment fragment = new UserProfile();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(),"Somethings wrong happened!",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }
}