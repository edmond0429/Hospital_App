package com.example.hospital_app;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import www.sanju.motiontoast.MotionToast;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private TextView banner, registerUser;
    private EditText etFullName, etAge, etEmail, etPassword, etPhoneNo;
    private ProgressBar progressBar;
    private static final String TAG = "RegisterUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register_user);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        banner = (TextView)findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button)findViewById(R.id.btnRegisterUser);
        registerUser.setOnClickListener(this);

        etFullName = (EditText)findViewById(R.id.fullName);
        etAge = (EditText)findViewById(R.id.age);
        etEmail = (EditText)findViewById(R.id.email);
        etPassword = (EditText)findViewById(R.id.password);
        etPhoneNo = (EditText)findViewById(R.id.phoneNumber);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.banner:
                finish();
                break;
            case R.id.btnRegisterUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String fullName = etFullName.getText().toString().trim();
        final String age = etAge.getText().toString().trim();
        final String phoneNo = etPhoneNo.getText().toString().trim();

        String phoneRegex = "[6-9][0-9]{9}";    //this mean the first phone number must between 6-9 and rest can be any number
        Matcher phoneMatcher;
        Pattern phonePattern = Pattern.compile(phoneRegex);
        phoneMatcher = phonePattern.matcher(phoneNo);

        if(fullName.isEmpty()){
            etFullName.setError("Full Name is required!");
            etFullName.requestFocus();
            return;
        }
        if(age.isEmpty()){
            etAge.setError("Age is required!");
            etAge.requestFocus();
            return;
        }
        if(email.isEmpty()){
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please provide valid email!");
            etEmail.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            etPassword.setError("Min password length should be 6 characters!");
            etPassword.requestFocus();
            return;
        }

        if(phoneNo.isEmpty()){
            etPhoneNo.setError("Phone Number is required!");
            etPhoneNo.requestFocus();
            return;
        } else if (!phoneMatcher.find()){
            Toast.makeText(this, "Please re-enter your phone number. ", Toast.LENGTH_SHORT).show();
            etPhoneNo.setError("Phone Number is not valid");
            etPhoneNo.requestFocus();
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullName,age,email,password,phoneNo);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        MotionToast.Companion.darkColorToast(RegisterUser.this,"Registered Succesful!",
                                                "Profile has been registered successfully!",
                                                MotionToast.TOAST_SUCCESS,
                                                MotionToast.GRAVITY_BOTTOM,
                                                MotionToast.LONG_DURATION,
                                                ResourcesCompat.getFont(RegisterUser.this,R.font.helvetica_regular));
                                        progressBar.setVisibility(View.GONE);
                                    }else{
                                        Toast.makeText(RegisterUser.this,"Failed to register! Please try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            try{
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e ){
                                etPassword.setError("Your password is too weak, Kindly use alphabet,number and special characters.");
                                etPassword.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                etEmail.setError("Your email is invalid or already in used. Kindly re-enter again!");
                                etEmail.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e){
                                etEmail.setError("The email had been registered before. Please re-enter a new email to perform register!");
                                etEmail.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(RegisterUser.this,e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}