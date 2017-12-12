package com.example.court.finalproject;

import android.app.Notification;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class RingtoneService extends Service {
    MediaPlayer media_song;
    int startId;
    boolean isRunning;
    private ArrayList<Songs> arrayList;

    private static final int notification_one = 101;          //ID for the notif channel
    private NotificationHelper notificationHelper;

    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("LocalService", "Received start id " + startId + ": " + intent);

        // Fetch alarm on/off values
        String state = intent.getExtras().getString("extra");

        // Fetch sound choice
        Integer sound_choice = intent.getExtras().getInt("sound_choice");

        Log.e("Ringtone extra is ", state);
        Log.e("Sound choice is ", sound_choice.toString());

        //Notifications
        notificationHelper = new NotificationHelper(this);

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                Log.e("Start ID is ", state);
                break;
            default:
                startId = 0;
                break;
        }

        if(!this.isRunning && startId == 1) {
            Log.e("There is no music, ", "and user pressed alarm on");

            // Play sound depending on passed sound id
            if (sound_choice == 0) {
                // Play random audio file
                int min = 1;
                int max = 10;

                Random random_number = new Random();
                int random_sound_choice = random_number.nextInt(max + min);
                Log.e("random number is ", String.valueOf(random_sound_choice));
                playMusic(random_sound_choice);
            } else {
                playMusic(sound_choice);

//            } else if (sound_choice < 10){
//                 Play audio file selected from spinner
//                playMusic(sound_choice);
//            } else {
                // Play music from sd
//                playfromSd();
            }

            this.isRunning = true;
            this.startId = 0;
            postNotification(101, "WAKE UP!");

        } else if(this.isRunning && startId == 0) {
            Log.e("There is music, ", "and user pressed alarm off");
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;

        } else if(!this.isRunning && startId == 0) {
            Log.e("There is no music, ", "and user pressed alarm off");
            this.isRunning = false;
            this.startId = 0;

        } else if (this.isRunning && startId == 1) {
            Log.e("There is music, ", "and user pressed alarm on");
            this.isRunning = true;
            this.startId = 1;
        } else {
            Log.e("else statement ", " how did you reach this");
        }
        return START_NOT_STICKY;
    }

//    private void playfromSd(){
//        Log.e("In playFromSd() ", "test");
//        for(int i=11; i < array.length; i++){
//            if(array[i] == i){
//                // Array of file names
//                // Play the music file
//                media_song = MediaPlayer.create(this, array[i]);
//                media_song = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/Slow And Easy.mp3"));
//                media_song.start();
//            }
//        }
//    }

    private void playMusic(int sound_number) {
//        ContentResolver contentResolver = getContentResolver();
//        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Cursor songCursor = contentResolver.query(songUri,n)
        Log.e("In playMusic() ", "test");
        if(sound_number == 1){
            media_song = MediaPlayer.create(this, R.raw.ac_1pm);
            media_song.start();
        }  else if (sound_number == 2) {
            media_song = MediaPlayer.create(this, R.raw.ac_8pm);
            media_song.start();
        } else if (sound_number == 3) {
            media_song = MediaPlayer.create(this, R.raw.ac_save);
            media_song.start();
        } else if (sound_number == 4) {
            media_song = MediaPlayer.create(this, R.raw.kdl_greengreens);
            media_song.start();
        } else if (sound_number == 5) {
            media_song = MediaPlayer.create(this, R.raw.ktam_rainbowroute);
            media_song.start();
        } else if (sound_number == 6) {
            media_song = MediaPlayer.create(this, R.raw.ktam_stageclear);
            media_song.start();
        } else if (sound_number == 7) {
            media_song = MediaPlayer.create(this, R.raw.lozminish_fairy);
            media_song.start();
        } else if (sound_number == 8) {
            media_song = MediaPlayer.create(this, R.raw.mother3_love);
            media_song.start();
        } else if (sound_number == 9) {
            media_song = MediaPlayer.create(this, R.raw.pkm_dive);
            media_song.start();
        } else if (sound_number == 10) {
            media_song = MediaPlayer.create(this, R.raw.sms_delfino);
            media_song.start();
        }else if (sound_number == 11) {
            media_song = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/Pork Soda.mp3"));
            media_song.start();
        }else if (sound_number == 12) {
            media_song = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/Treat Me Like Somebody.mp3"));
            media_song.start();
        }else if (sound_number == 13) {
            media_song = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Music/Slow And Easy.mp3"));
            media_song.start();
        } else {
            // Not supposed to get here, just play Delfino
            media_song = MediaPlayer.create(this, R.raw.sms_delfino);
            media_song.start();
        }
    }

    //------------------------- NOTIFICATIONS -------------------------
    //Notification testing
    public void postNotification(int id, String title) {
        Notification.Builder notificationBuilder = null;
        switch (id) {
            case notification_one:
                notificationBuilder = notificationHelper.getNotification1(title,
                        getString(R.string.channel_one_body));
                break;

        }
        if (notificationBuilder != null) {
            notificationHelper.notify(id, notificationBuilder);
        }
    }

    public void goToNotificationSettings(String channel) {
        Intent i = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        i.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        i.putExtra(Settings.EXTRA_CHANNEL_ID, channel);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        Log.e("onDestroy called ", "app closed");
    }
}
