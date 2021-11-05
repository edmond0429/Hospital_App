package com.example.hospital_app;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import www.sanju.motiontoast.MotionToast;

public class adminMakeRequest extends AppCompatActivity {

    DatePickerDialog.OnDateSetListener setListener;
    TextView mTvBookingTime, mTvMeetMethod, mTvDoctor, mTvSelectedHospital;
    EditText PatientName,BookingDate;
    Button BookingButton;
    private DatabaseReference mDatabaseReference, reference, mDatabaseReference1;
    Booking booking;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private Spinner mSpinnerBookingTime, mSpinnerMethodMeet, mSpinnerDoctor;
    ArrayList<String> spinnerBookingTime, spinnerMeetMethod, spinnerDoctor;
    ArrayAdapter<String> adapterBookingTime, adapterMethod, adapterDoctor;
    FetchData fetchData;
//    String bookingTimeSelected, methodSelected, bookingDateSelected, doctorSelected;
    String[] bookingtime = { "9.00-9.30", "9.30-10.00", "10.00-10.30", "10.30-11.00", "11.00-11.30", "11.30-12.00",
            "12.00-12.30", "12.30-13.00", "13.00-13.30", "13.30-14.00", "14.00-14.30", "14.30-15.00", "15.00-15.30",
            "15.30-16.00", "16.00-16.30", "16.30-17.00", "17.00-17.30", "17.30-18.00", "18.00-18.30", "18.30-19.00", };
    String[] spinnerMeetmethod = {"Choose Meet Method", "Online Consultation", "Face to face"};
    String time;
    String patientName="",bookingDateSelected="",bookingTimeSelected="",methodSelected="",doctorSelected="",hospitalName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_make_request);

        PatientName = (EditText)findViewById(R.id.etPatientName);
        BookingDate = (EditText)findViewById(R.id.etBookingDate);
        BookingDate.setInputType(InputType.TYPE_NULL);      //Hiding keyboard when pressed the date
        mSpinnerBookingTime = (Spinner)findViewById(R.id.spinnerbookingTime);
        mSpinnerMethodMeet = (Spinner)findViewById(R.id.spinnerMeetMethod);
        mSpinnerDoctor = (Spinner)findViewById(R.id.spinnerDoctor);
        BookingButton = (Button)findViewById(R.id.btnMakeBooking);
        mTvBookingTime = (TextView)findViewById(R.id.tvBookingTimeSelected);
        mTvMeetMethod = (TextView)findViewById(R.id.tvMeetMethodSelected);
        mTvDoctor = (TextView)findViewById(R.id.tvSelectdoctor);
        mTvSelectedHospital = (TextView)findViewById(R.id.tvSelecthospital);

        Intent i = getIntent();
        fetchData = (FetchData) i.getSerializableExtra("fetchData");

        reference = FirebaseDatabase.getInstance().getReference("RequestBooking");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Doctor");

        Calendar today  = Calendar.getInstance();
        Calendar oneWeekLater = (Calendar) today.clone();
        oneWeekLater.add(Calendar.DATE, 6);

        final int year = today.get(Calendar.YEAR);
        final int month = today.get(Calendar.MONTH);
        final int day = today.get(Calendar.DAY_OF_MONTH);

        BookingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(adminMakeRequest.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        BookingDate.setText(date);
                        bookingDateSelected = BookingDate.getText().toString();
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(oneWeekLater.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        spinnerMeetMethodFunction();
        spinnerDoctorFunction();

        BookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientName = PatientName.getText().toString().trim();
                bookingDateSelected = BookingDate.getText().toString().trim();
                bookingTimeSelected = mTvBookingTime.getText().toString().trim();
                methodSelected = mTvMeetMethod.getText().toString().trim();
                doctorSelected = mTvDoctor.getText().toString().trim();
                hospitalName = mTvSelectedHospital.getText().toString().trim();

                if(patientName.isEmpty()){
                    PatientName.setError("Patient Name is required!");
                    PatientName.requestFocus();
                    return;
                }
                if(bookingDateSelected.isEmpty()){
                    BookingDate.setError("Booking date is required!");
                    BookingDate.requestFocus();
                    return;
                }
                if(bookingTimeSelected.isEmpty()){
                    mTvBookingTime.setError("Booking time is required!");
                    mTvBookingTime.requestFocus();
                    return;
                }
                if(methodSelected == "Choose Meet Method"){
                    mTvMeetMethod.setError("Meet method is required!");
                    mTvMeetMethod.requestFocus();
                    return;
                }
                if(doctorSelected.isEmpty()){
                    mTvDoctor.setError("Doctor is required!");
                    mTvDoctor.requestFocus();
                    return;
                }
                if(hospitalName.isEmpty()){
                    mTvSelectedHospital.setError("Hospital name is required!");
                    mTvSelectedHospital.requestFocus();
                    return;
                }

                booking = new Booking(patientName,bookingDateSelected,bookingTimeSelected,methodSelected,doctorSelected,hospitalName);
                reference.child(patientName).setValue(booking);
                MotionToast.Companion.darkColorToast(adminMakeRequest.this,"Added a booking request!",
                        "Successful request a booking!",
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(getApplicationContext(),R.font.helvetica_regular));
                Toast.makeText(adminMakeRequest.this, "Successful request a booking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void spinnerDoctorFunction() {
        spinnerDoctor = new ArrayList<>();
        adapterDoctor = new ArrayAdapter<String>(getApplication(),android.R.layout.simple_spinner_dropdown_item,spinnerDoctor);
        adapterDoctor.setDropDownViewResource(R.layout.my_dropdown_item);

        mSpinnerDoctor.setAdapter(adapterDoctor);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot doctor:snapshot.getChildren()){
                    spinnerDoctor.add(doctor.child("doctorName").getValue().toString());
                }
                adapterDoctor.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mSpinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                doctorSelected = mSpinnerDoctor.getSelectedItem().toString();
                mTvDoctor.setText(doctorSelected);
                filterBookingTime(doctorSelected);

                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        hospitalName = snapshot.child(doctorSelected).child("hospitalName").getValue().toString();
                        mTvSelectedHospital.setText(hospitalName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void spinnerMeetMethodFunction() {
        spinnerMeetMethod = new ArrayList<>();
        adapterMethod = new ArrayAdapter<String>(getApplication(),R.layout.my_selected_item,spinnerMeetmethod);
        adapterMethod.setDropDownViewResource(R.layout.my_dropdown_item);

        mSpinnerMethodMeet.setAdapter(adapterMethod);
        mSpinnerMethodMeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).equals("Choose Booking Time")){
//                    Toast.makeText(userMakeBooking.this, "Please select a booking method", Toast.LENGTH_SHORT).show();
                }else{
                    methodSelected = mSpinnerMethodMeet.getSelectedItem().toString();
                    mTvMeetMethod.setText(methodSelected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void filterBookingTime(String doctorSelected) {
        ArrayList<String> temp = new ArrayList<>();

        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference("Doctor").child(doctorSelected);
        mDatabaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   time = snapshot.child("time").getValue().toString();

                String currentString = time;
                String[] separatedStartAndEnd = currentString.split("-");
                String[] startTime = separatedStartAndEnd[0].split("\\.");
                Time starttime = new Time(Integer.parseInt(startTime[0]),Integer.parseInt(startTime[1]),0);

                String[] endTime = separatedStartAndEnd[1].split("\\.");
                Time endtime = new Time(Integer.parseInt(endTime[0]),Integer.parseInt(endTime[1]),0);

                for (String currentString2:bookingtime) {
                    String[] separatedStartAndEnd2 = currentString2.split("-");
                    String[] startTime2 = separatedStartAndEnd2[0].split("\\.");
                    Time starttime2 = new Time(Integer.parseInt(startTime2[0]),Integer.parseInt(startTime2[1]),0);

                    String[] endTime2 = separatedStartAndEnd2[1].split("\\.");
                    Time endtime2 = new Time(Integer.parseInt(endTime2[0]),Integer.parseInt(endTime2[1]),0);

                    if ((starttime.before(starttime2) || starttime.equals(starttime2)) && (endtime.after(endtime2) || endtime.equals(endtime2))) {
                        temp.add(currentString2);
                    }
                }
                Object[] tempArray = temp.toArray();
                String[] filteredTime = new String[tempArray.length];
                System.arraycopy(tempArray, 0, filteredTime, 0, tempArray.length);

                spinnerBookingTime = new ArrayList<>();
                adapterBookingTime = new ArrayAdapter<String>(getApplication(),android.R.layout.simple_spinner_dropdown_item,temp);
                adapterBookingTime.setDropDownViewResource(R.layout.my_dropdown_item);

                mSpinnerBookingTime.setAdapter(adapterBookingTime);
                mSpinnerBookingTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(adapterView.getItemAtPosition(i).equals("Choose Booking Time")){
//                    Toast.makeText(userMakeBooking.this, "Please select a booking time", Toast.LENGTH_SHORT).show();
                        }else{
                            bookingTimeSelected = mSpinnerBookingTime.getSelectedItem().toString();
                            mTvBookingTime.setText(bookingTimeSelected);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
