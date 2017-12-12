package com.example.court.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Night_Alarm_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("In the night receiver ", "Yay");

        // Fetch extra strings from intent
        // Tells app whether or not user pressed 'alarm on' or 'alarm off' button
        String get_string = intent.getExtras().getString("extra");
        Log.e("What is the night key? ", get_string);

        // Fetch extra longs from intent
        // Tells app which sound picked from spinner
        Integer get_sound_choice = intent.getExtras().getInt("sound_choice");
        Log.e("The night sound is ", get_sound_choice.toString());

        // Go to Ringtone Service
        Intent service_intent = new Intent(context, Night_RingtoneService.class);

        // Pass extra strings from main activity to ringtone service
        service_intent.putExtra("extra", get_string);
        service_intent.putExtra("sound_choice", get_sound_choice);

        // Start ringtone service
        context.startService(service_intent);
    }
}
