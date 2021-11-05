package com.example.hospital_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class UserHomepage extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;
    ImageSlider mImageSlider;
    DatabaseReference mDatabaseReference;
    GridLayout homegrid;
    CardView cardChat, cardViewReq, cardLocation, cardFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_homepage);

        cardChat = findViewById(R.id.chatcard);
        cardViewReq = findViewById(R.id.viewcard);
        cardLocation = findViewById(R.id.locationcard);
        cardFeedback = findViewById(R.id.feedbackcard);
        homegrid = (GridLayout)findViewById(R.id.homegrid);
        mImageSlider = findViewById(R.id.sliderIMG);

        final List<SlideModel> hosImage = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("hospital");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    hosImage.add(new SlideModel(ds.child("hospitalUrl").getValue().toString(),ds.child("hospitalName").getValue().toString(), ScaleTypes.FIT));
                    mImageSlider.setImageList(hosImage,ScaleTypes.FIT);

                    mImageSlider.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemSelected(int position) {
                            Toast.makeText(UserHomepage.this, hosImage.get(position).getTitle().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cardChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), chatbot.class);   //change to chatbot activity
                startActivity(intent);
            }
        });

        cardViewReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), userViewAcceptedBooking.class);
                startActivity(intent);
            }
        });

        cardLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), hospLocation.class);
                startActivity(intent);
            }
        });

        cardFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), userFeedback.class);
                startActivity(intent);
            }
        });



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