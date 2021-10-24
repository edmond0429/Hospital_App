package com.example.hospital_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;

public class addAlarm extends AppCompatActivity {

    TextView tvAlarmTime;
    EditText mEditAlarmTitle;
    CheckBox cbRecurring,cbMon,cbTues,cbWed,cbThurs,cbFri,cbSat,cbSun;
    DatabaseReference mDatabaseReference;
    MedicAlarm mMedicAlarm;
    String dayRepeat = "";
    Button btnSchedule, btnSelectTime;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    String time ="";
    private int hours, mins;
    private AlarmManager alarmManager;
    private PendingIntent mPendingIntent;
    private Calendar calendar;
    private NotificationHelper mNotificationHelper;
    LinearLayout recurringOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        tvAlarmTime = findViewById(R.id.tvAlarmTime);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnSchedule = findViewById(R.id.alarm_schedule);
        mEditAlarmTitle = findViewById(R.id.alarm_title);
        recurringOptions = findViewById(R.id.recurringOption);
        cbRecurring = findViewById(R.id.alarm_recurring);
        cbMon = findViewById(R.id.alarm_checkMon);
        cbTues = findViewById(R.id.alarm_checkTue);
        cbWed = findViewById(R.id.alarm_checkWed);
        cbThurs = findViewById(R.id.alarm_checkThu);
        cbFri = findViewById(R.id.alarm_checkFri);
        cbSat = findViewById(R.id.alarm_checkSat);
        cbSun = findViewById(R.id.alarm_checkSun);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Alarm");
        mMedicAlarm = new MedicAlarm();
        mNotificationHelper = new NotificationHelper(this);



        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                hours = calendar.get(Calendar.HOUR_OF_DAY);
                mins = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(addAlarm.this, R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minutes);
                        calendar.setTimeZone(TimeZone.getDefault());
                        SimpleDateFormat format = new SimpleDateFormat("k:mm a");
                        time = format.format(calendar.getTime());
                        tvAlarmTime.setText(time);
                    }
                },hours,mins,false);
                timePickerDialog.show();
            }
        });

        cbRecurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recurringOptions.setVisibility(View.VISIBLE);
                } else {
                    recurringOptions.setVisibility(View.GONE);
                }
            }
        });

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alarmTitle = mEditAlarmTitle.getText().toString();
                String alarmTime = tvAlarmTime.getText().toString();
                //create notification
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                if(cbMon.isChecked()){
                    dayRepeat += " " + cbMon.getText().toString();
                    repeatday(2);
                }
                if(cbTues.isChecked()){
                    dayRepeat += " " + cbTues.getText().toString();
                    repeatday(3);
                }
                if(cbWed.isChecked()){
                    dayRepeat += " " + cbWed.getText().toString();
                    repeatday(4);
                }
                if(cbThurs.isChecked()){
                    dayRepeat += " " + cbThurs.getText().toString();
                    repeatday(5);
                }
                if(cbFri.isChecked()){
                    dayRepeat += " " + cbFri.getText().toString();
                    repeatday(6);
                }
                if(cbSat.isChecked()){
                    dayRepeat += " " + cbSat.getText().toString();
                    repeatday(7);
                }
                if(cbSun.isChecked()){
                    dayRepeat += " " + cbSun.getText().toString();
                    repeatday(1);
                }

                //store data in firebase
                mMedicAlarm = new MedicAlarm(alarmTitle,alarmTime,dayRepeat);
                mDatabaseReference.child(mFirebaseAuth.getCurrentUser().getUid())
                        .child(alarmTitle).setValue(mMedicAlarm);

                Intent intent = new Intent(addAlarm.this, AlarmReceiver.class);
                //pass bundle to store alarm title and time to alarm receiver class
                Bundle bundle = new Bundle();
                bundle.putSerializable("alarmtitle",alarmTitle);
                bundle.putSerializable("time",time);
                intent.putExtra("bundle",bundle);
                mPendingIntent = PendingIntent.getBroadcast(addAlarm.this, 1, intent, 0);
                //check if the alarm is set with past time, then will be set on next day
                if(calendar.before(Calendar.getInstance())){
                    calendar.add(Calendar.DATE,1);
                }

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mPendingIntent);


                Toast.makeText(addAlarm.this, "You had successful added an alarm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void repeatday(int day) {
//        calendar.set(Calendar.DAY_OF_WEEK, day);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        final long RUN_DAILY = 24 * 60 * 60 * 1000;
//
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(), RUN_DAILY, mPendingIntent);

    }
}