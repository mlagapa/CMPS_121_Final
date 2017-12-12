package com.example.court.finalproject;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.TimePicker;
import android.widget.TextView;
import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import static com.example.court.finalproject.R.array.music_array;

public class NightAlarmActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;
    ArrayList<String> arrayList;
    CharSequence[] combinedArray;
    int choose_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.NightTheme);
        setTitle("Stay Sleep");
        setContentView(R.layout.night_alarm_layout);
        this.context = this;

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker1);
        update_text = (TextView) findViewById(R.id.update_text1);
        final Calendar calendar = Calendar.getInstance();
        final Intent alarm_receiver = new Intent(this.context, Night_Alarm_Receiver.class);

        // Spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        String[] musicArray = context.getResources().getStringArray(music_array);
        arrayList = new ArrayList<String>();
        getMusic();
        List<String> musicList = new ArrayList<String>(Arrays.asList(musicArray));
//        musicArray = arrayList.toArray(musicArray);
        musicList.addAll(arrayList);
        this.combinedArray = new String[musicList.size()];
        combinedArray = musicList.toArray(combinedArray);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, combinedArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set onClickListener to onItemSelected method
        spinner.setOnItemSelectedListener(this);

        // Click 'Set Alarm' button
        final Button alarm_on = (Button) this.findViewById(R.id.alarm_on1);
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set calendar
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                // Displaying time of alarm
                // int values of hour and minute
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                // Convert the int values to Strings
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                // Convert 24-hour time to 12-hour time
                if(hour == 0) {
                    hour_string = String.valueOf(12);
                    minute_string = String.valueOf(minute) + " AM";
                } else if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                    minute_string = String.valueOf(minute) + " PM";
                } else {
                    minute_string = String.valueOf(minute) + " AM";
                }

                if (minute < 10) {
                    minute_string = "0" + String.valueOf(minute);
                }

                set_alarm_text("Alarm set to " + hour_string + ":" + minute_string);

                // Tells clock that you pressed "alarm on" button
                alarm_receiver.putExtra("extra", "alarm on");

                // Tells clock which sound you selected from spinner
                alarm_receiver.putExtra("sound_choice", choose_sound);

                Log.e("The night sound id is ", String.valueOf(choose_sound));

                // Ringtone will play at set time
                pending_intent = PendingIntent.getBroadcast(NightAlarmActivity.this, 0, alarm_receiver,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
                Log.d("ALARM", "" + alarm_manager.toString());
            }
        });

        // Click 'Alarm Off' button
        Button alarm_off = (Button) this.findViewById(R.id.alarm_off1);
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display cancellation
                set_alarm_text("Alarm off!");

                // Tells clock that you pressed "alarm off" button
                alarm_receiver.putExtra("extra", "alarm off");

                // Tells clock which sound you selected from spinner
                // Prevents crash from Null Pointer Exception
                alarm_receiver.putExtra("sound_choice", choose_sound);

                // Cancel alarm, stop ringtone
                try {
                    alarm_manager.cancel(pending_intent);
                    sendBroadcast(alarm_receiver);
                } catch (NullPointerException e){
                    Log.e("Alarm has not been set ", "and user pressed alarm off");
                }
            }
        });

        // Click 'Snooze' button
        Button snooze_Button = (Button) this.findViewById(R.id.snooze_Button);
        Intent intent = getIntent();
        final int numSno = intent.getIntExtra("numberTimeNight",0);

        snooze_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display cancellation
                set_alarm_text("Alarm has been snoozed! Snooze time set to " + numSno + "mins.");

                // Tells clock that you pressed "alarm off" button
                alarm_receiver.putExtra("extra", "alarm off");

                // Tells clock which sound you selected from spinner
                // Prevents crash from Null Pointer Exception
                alarm_receiver.putExtra("sound_choice", choose_sound);

                // Cancel alarm, stop ringtone
                try {
                    alarm_manager.cancel(pending_intent);
                    sendBroadcast(alarm_receiver);

                } catch (NullPointerException e) {
                    Log.e("Alarm has not been set ", "and user pressed alarm snooze");
                }

                // Snooze for certain amount of minutes and then plays tone again.
                alarm_receiver.putExtra("extra", "alarm on");

                // Tells clock which sound you selected from spinner
                alarm_receiver.putExtra("sound_choice", choose_sound);
                Log.e("The sound id is ", String.valueOf(choose_sound));


                pending_intent = PendingIntent.getBroadcast(NightAlarmActivity.this, 0, alarm_receiver,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                long currentTimeMillis = System.currentTimeMillis();
                long nextUpdateTimeMillis = currentTimeMillis + numSno * DateUtils.MINUTE_IN_MILLIS;

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, nextUpdateTimeMillis, pending_intent);
            }
        });

        // Click 'Back to Main Alarm' Button
        Button back_to_main = (Button) this.findViewById(R.id.backToMain);
        back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMain = new Intent(NightAlarmActivity.this, MainActivity.class);
                startActivity(backToMain);
            }
        });

        // Click 'Snooze Settings' button
        Button snoozeSettings_Button = (Button) this.findViewById(R.id.Snooze_setting);
        snoozeSettings_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewActivity = new Intent(NightAlarmActivity.this, snoozeSetNight.class);
                startActivity(startNewActivity);
            }
        });
    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Outputting sound user selects from spinner
        Toast.makeText(parent.getContext(), "the spinner item is " + id, Toast.LENGTH_SHORT).show();
        choose_sound = (int) id;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                String currentTitle = songCursor.getString(songArtist) + " - " + songCursor.getString(songTitle);
                arrayList.add(currentTitle);
            } while (songCursor.moveToNext());
        }
    }
}
