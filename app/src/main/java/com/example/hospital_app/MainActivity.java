package com.example.hospital_app;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.chipNavigation);
        chipNavigationBar.setItemSelected(R.id.home,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new Fragment()).commit();

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener(){
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.home:
                        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getBaseContext(), UserHomepage.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.booking:
                        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                            fragment = new BlankBookingFragment();
                        }else{
                            fragment = new BookingFragment();
                        }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case R.id.timer:
                        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                            fragment = new BlankTimeFragment();
                        }else {
                            fragment = new TimerFragment();
                        }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                        break;
                    case R.id.profile:
                        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                            fragment = new LoginProfile();
                        }else{
                            fragment = new UserProfile();
                        }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                }
            }
        });
    }
}