package com.example.court.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class snoozeSetNight extends AppCompatActivity {

    public Integer minutes;
    Spinner num_Mins;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze_set);

        num_Mins = (Spinner) findViewById(R.id.snooze_Time);
        submitButton = (Button) findViewById(R.id.done_Button);

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                minutes = Integer.valueOf(String.valueOf(num_Mins.getSelectedItem()));

                Intent intent = new Intent(snoozeSetNight.this, NightAlarmActivity.class);
                intent.putExtra("numberTimeNight", minutes);
                Toast.makeText(snoozeSetNight.this, "Your snooze time (" + minutes + " minutes) has been saved!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
