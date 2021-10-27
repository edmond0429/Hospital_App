package com.example.hospital_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import www.sanju.motiontoast.MotionToast;

public class LoginProfile extends Fragment implements View.OnClickListener {

    private TextView register, forgetPassword;
    private EditText etEmail, etPassword;
    private Button signIn;
    private int login = 0;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_profile, container, false);

        register = (TextView) view.findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button)view.findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        etEmail = (EditText)view.findViewById(R.id.email);
        etPassword = (EditText)view.findViewById(R.id.password);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        forgetPassword = (TextView)view.findViewById(R.id.forgetPassword);
        forgetPassword.setOnClickListener(this);

        //Hide and show password icon
        ImageView imageViewHideShowPsw = (ImageView)view.findViewById(R.id.imgShowHidePsw);
        imageViewHideShowPsw.setImageResource(R.drawable.show_psw);
        imageViewHideShowPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //If password is visible then hide it
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    imageViewHideShowPsw.setImageResource(R.drawable.hide_psw);
                }else{
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewHideShowPsw.setImageResource(R.drawable.show_psw);
                }
            }
        });


        chipNavigationBar = view.findViewById(R.id.chipNavigation);
        chipNavigationBar.setItemSelected(R.id.profile,true);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(getActivity(), RegisterUser.class));
                break;
            case R.id.signIn:
                userLogin();
                break;
            case R.id.forgetPassword:
                startActivity(new Intent(getActivity(), ForgetPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please enter a valid email!");
            etEmail.requestFocus();
            return;
        }

        if(password.length() < 6){
            etPassword.setError("Min password length is 6 characters!");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()) {
                        //redirected to user profile
                        MotionToast.Companion.darkColorToast(getActivity(),
                                "Success",
                                "Login successfully!",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getActivity(),R.font.helvetica_regular));
                        if(mFirebaseAuth.getCurrentUser().getUid().equals("5C83E5jc2pakd3A6PWd7dwwvQF12") ) {
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
                    }else{
                        user.sendEmailVerification();
                        MotionToast.Companion.darkToast(getActivity(),"Verify Before Login","Please Check Your Email to Verify Account",
                                MotionToast.TOAST_INFO,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getActivity(),R.font.helvetica_regular));
                        progressBar.setVisibility(View.GONE);
                    }
                }else{
                    MotionToast.Companion.darkColorToast(getActivity(),"Failed to login!","Please Check Your Credentials To Perform login!",
                            MotionToast.TOAST_ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getActivity(),R.font.helvetica_regular));
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

}