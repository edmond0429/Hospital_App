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

import com.google.firebase.auth.FirebaseUser;
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

public class userMakeBooking extends AppCompatActivity {

    DatePickerDialog.OnDateSetListener setListener;
    TextView mTvBookingTime, mTvMeetMethod;
    EditText PatientName,BookingDate;
    Button BookingButton;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    Booking bookingdata;
    Spinner mSpinnerBookingTime, mSpinnerMethodMeet;
    ArrayList<String> spinnerBookingTime, spinnerMeetMethod;
    ArrayAdapter<String> adapterBookingTime, adapterMethod;
    FetchData fetchData;
    int bookingId = 0;
    String bookingTimeSelected, methodSelected, bookingDateSelected;
    String[] bookingtime = { "9.00-9.30", "9.30-10.00", "10.00-10.30", "10.30-11.00", "11.00-11.30", "11.30-12.00",
                            "12.00-12.30", "12.30-13.00", "13.00-13.30", "13.30-14.00", "14.00-14.30", "14.30-15.00", "15.00-15.30",
                            "15.30-16.00", "16.00-16.30", "16.30-17.00", "17.00-17.30", "17.30-18.00", "18.00-18.30", "18.30-19.00", };
    String[] spinnerMeetmethod = {"Choose Meet Method", "Online Consultation", "Face to face"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_make_booking);

        PatientName = (EditText)findViewById(R.id.etPatientName);
        BookingDate = (EditText)findViewById(R.id.etBookingDate);
        BookingDate.setInputType(InputType.TYPE_NULL);      //Hiding keyboard when pressed the date
        mSpinnerBookingTime = (Spinner)findViewById(R.id.spinnerbookingTime);
        mSpinnerMethodMeet = (Spinner)findViewById(R.id.spinnerMeetMethod);
        BookingButton = (Button)findViewById(R.id.btnMakeBooking);
        mTvBookingTime = (TextView)findViewById(R.id.tvBookingTimeSelected);
        mTvMeetMethod = (TextView)findViewById(R.id.tvMeetMethodSelected);

        Intent i = getIntent();
        fetchData = (FetchData) i.getSerializableExtra("fetchData");

        reference = FirebaseDatabase.getInstance().getReference("RequestBooking");

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        BookingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(userMakeBooking.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        BookingDate.setText(date);
                        bookingDateSelected = BookingDate.getText().toString();
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        spinnerBookingTimeFunction();
        spinnerMeetMethodFunction();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    bookingId= (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String patientName = PatientName.getText().toString().trim();
                if(patientName.isEmpty()){
                    PatientName.setError("Patient Name is required!");
                    PatientName.requestFocus();
                    return;
                }


                Booking booking = new Booking(patientName,bookingDateSelected,bookingTimeSelected,methodSelected);
                reference.child(String.valueOf(bookingId+1)).setValue(booking);
                Toast.makeText(userMakeBooking.this, "Successful request a booking", Toast.LENGTH_SHORT).show();
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

    private void spinnerBookingTimeFunction() {
        spinnerBookingTime = new ArrayList<>();
        String[] temp = filterBookingTime();
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
    private String[] filterBookingTime() {
        ArrayList<String> temp = new ArrayList<>();
//        reference.child(doctor)
        String currentString = fetchData.getTime();
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
        return filteredTime;
    }


//    private void showDateDialog(EditText BookingDate) {
//        Calendar calendar = Calendar.getInstance();
//        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                calendar.set(Calendar.YEAR,year);
//                calendar.set(Calendar.MONTH,month);
//                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                BookingDate.setText(simpleDateFormat.format(calendar.getTime()));
//            }
//        };
//        new DatePickerDialog(getApplication(),dateSetListener,
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)).show();
//    }

}