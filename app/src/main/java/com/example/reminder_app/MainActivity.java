package com.example.reminder_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,OptionsDialog.getOption {

    TextView tv_set_rem;
    MaterialButton set,cancel;
    RadioButton radioButton;
    Calendar c;
    String selectedInterval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_set_rem=findViewById(R.id.tv_rem_time);

        set=findViewById(R.id.bt_set_rem);
        cancel=findViewById(R.id.bt_cancel_rem);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker=new TimePicker();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), ReminderReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);

                alarmManager.cancel(pendingIntent);
                tv_set_rem.setText("Alarm canceled");
            }
        });
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        OptionsDialog exampleDialog = new OptionsDialog();
        exampleDialog.show(getSupportFragmentManager(), "Options dialog");
    }

    public void startAlarm(int i) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        if(i==1)
        {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            updateTimeText(c);
        }
        else if(i==2)
        {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_HOUR,pendingIntent);
            updateTimeText(c);
        }
        else if(i==3)
        {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
            updateTimeText(c);
        }
        else
        {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7,pendingIntent);
            updateTimeText(c);
        }


    }


    public void updateTimeText(Calendar c) {
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        tv_set_rem.setText(timeText);

    }

    @Override
    public void getInterval(String option) {
        selectedInterval=option;
        if(selectedInterval.equals("Don't repeat"))
        {
            startAlarm(1);
        }
        else if(selectedInterval.equals("Repeat hourly"))
        {
            startAlarm(2);
        }
        else if(selectedInterval.equals("Repeat daily"))
        {
            startAlarm(3);
        }
        else{
            startAlarm(4);
        }
    }
}
