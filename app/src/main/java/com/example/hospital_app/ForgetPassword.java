package com.example.hospital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import www.sanju.motiontoast.MotionToast;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private EditText etEmail;
    private Button btnResetPassword;
    private ProgressBar progressBar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        etEmail = (EditText)findViewById(R.id.email);
        btnResetPassword = (Button)findViewById(R.id.btnReset);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = etEmail.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please provided a valid email!");
            etEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    MotionToast.Companion.darkColorToast(ForgetPassword.this,"Check Email!",
                            "Please Check Your Email to Perform Reset Password!",
                            MotionToast.TOAST_INFO,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ForgetPassword.this,R.font.helvetica_regular));
                    progressBar.setVisibility(View.GONE);
                }else{
                    MotionToast.Companion.darkColorToast(ForgetPassword.this,"Wrong Email Address!!",
                            "Please Re-enter The Existed Email!",
                            MotionToast.TOAST_ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ForgetPassword.this,R.font.helvetica_regular));
                    Toast.makeText(ForgetPassword.this,"Try again! Somethings wrong happend!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}