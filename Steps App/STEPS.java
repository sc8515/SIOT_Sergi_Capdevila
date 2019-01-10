package com.example.sergi.steps;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.sergi.steps.App.CHANNEL_1_ID;
import android.app.IntentService;
import android.content.Intent;


public class STEPS extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;

    TextView steps;
    TextView delta;
    TextView timer;
    TextView intervalsteps;

    boolean running = false;

    int counter = 0;

    String stepcountstring = "";
    String stepcountstringgood = "";
    int stepcount = 0;
    int initialstepcounttimer = 0;
    int finalstepcounttimer = 0;
    int initialstepcount = 0;
    String printstepcount;
    int count = 0;
    int notificationcount = 1;

    //NotificationManager
    private NotificationManagerCompat notificationManager;

    //Alarm Manage
    public static final String TAG = "STEPS";
    PendingIntent myPendingIntent;
    AlarmManager alarmManager;
    BroadcastReceiver myBroadcastReceiver;

    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        notificationManager = NotificationManagerCompat.from(this);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = 0;
                fab.setEnabled(false);
                StartCount(view);
            }
        });

        steps = (TextView) findViewById(R.id.steps);
        delta = (TextView) findViewById(R.id.delta);
        timer = (TextView) findViewById(R.id.timer);
        intervalsteps = (TextView) findViewById(R.id.intervalsteps);
        intervalsteps.setText("OFF");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Register AlarmManager Broadcaster receive.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 00); // At the hour you want to fire the alarm
        calendar.set(Calendar.MINUTE, 00); // alarm minute
        calendar.set(Calendar.SECOND, 15); // and alarm second

        registerMyAlarmBroadcast();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, myPendingIntent);

        //start service
        Intent intent = new Intent(this, StepsIntentService.class);
        startService(intent);
    }
    public void sendNotification(View v){
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_android)
                .setContentTitle("Step Count: " + steps.getText())
                .setContentText("You are being 26.38% more active! Keep it up!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }
    private void registerMyAlarmBroadcast()
    {
        Log.i(TAG, "Going to register Intent.RegisterAlarmBroadcast");

        //This is the call back function(BroadcastReceiver) which will be call when your
        //alarm time will reached.
        myBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                count = 0;
                intervalsteps.setText("WORKING!");
                steps.setText("0"); //Count the App Starts with!
                Log.i(TAG,"BroadcastReceiver::OnReceive()");
                Toast.makeText(context, "Your Alarm is there", Toast.LENGTH_LONG).show();
            }
        };

        registerReceiver(myBroadcastReceiver, new IntentFilter("com.alarm.example") );
        myPendingIntent = PendingIntent.getBroadcast( this, 0, new Intent("com.alarm.example"),0 );
        alarmManager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
    }
    private void UnregisterAlarmBroadcast()
    {
        alarmManager.cancel(myPendingIntent);
        getBaseContext().unregisterReceiver(myBroadcastReceiver);
    }
    private void notificationsetup(){
        if (Integer.valueOf(String.valueOf(steps.getText())) > 15) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_android)
                    .setContentTitle("25% done!")
                    .setContentText("Your step count is: " + steps.getText())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();
            notificationManager.notify(1, notification);

        } else if (Integer.valueOf(String.valueOf(steps.getText())) > 5000) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_android)
                    .setContentTitle("Half the way there! Keep it up!")
                    .setContentText("Your step count is: " + steps.getText())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();
            notificationManager.notify(1, notification);

        }else if (Integer.valueOf(String.valueOf(steps.getText())) > 7500) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_android)
                    .setContentTitle("Almost there!")
                    .setContentText("Your step count is: " + steps.getText())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();
            notificationManager.notify(1, notification);

        }else if (Integer.valueOf(String.valueOf(steps.getText())) > 10000) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_android)
                    .setContentTitle("You did it! Well done!")
                    .setContentText("Your step count is: " + steps.getText())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();
            notificationManager.notify(1, notification);
        }
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(myBroadcastReceiver);
        super.onDestroy();
    }
    public void StartCount(View v){
        initialstepcounttimer = Integer.valueOf(stepcountstringgood);
        System.out.println(initialstepcounttimer);
        Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_SHORT).show();
        final CountDownTimer Time = new CountDownTimer(604800000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                counter++;
                timer.setText(String.valueOf(counter*5));
                finalstepcounttimer = Integer.valueOf(stepcountstringgood);
                stepcount = finalstepcounttimer - initialstepcounttimer;  //calculate delta
                delta.setText(String.valueOf(stepcount));
                Toast.makeText(getApplicationContext(), "Delta Gathered!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
            }
        };
        Time.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        //if you unregister the hardware will stop detecting steps
        //sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running){
            if (count!=0){
                stepcountstring = String.valueOf(event.values[0]);
                stepcountstringgood = stepcountstring.replace(".0", ""); //get rid of .0 to avoid getting an error when converting to string
                printstepcount = String.valueOf(Integer.valueOf(stepcountstringgood)-initialstepcount);
                steps.setText(printstepcount);
                notificationsetup();
            }
            else { //(count = 0)
                stepcountstring = String.valueOf(event.values[0]);
                stepcountstringgood = stepcountstring.replace(".0", ""); //get rid of .0 to avoid getting an error when converting to string
                initialstepcount = Integer.valueOf(stepcountstringgood);
                count = 1;
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
