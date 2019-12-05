package com.example.android.accelerometerplay;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class SubActivity extends Activity {
    public SensorManager sensorManager;
    public Sensor sensor;
    public TextView dash_count;
    private Sensor mAccelerometer;
    public ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenMain();
    }

    private void setScreenMain(){
        setContentView(R.layout.activity_sub);

        Button actionButton1 = findViewById(R.id.button_sub1);
        actionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenSub2();
            }
        });
        Button actionButton2 = findViewById(R.id.button_sub2);
        actionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenSub3();
            }
        });
        Button actionButton3 = findViewById(R.id.button_sub3);
        actionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), AccelerometerPlayActivity.class);
                String msg = "1";
                intent.putExtra("act", msg);
                startActivity(intent);
            }
        });
    }

    private void setScreenSub2(){
        setContentView(R.layout.activity_sub2);
        Button returnButton = findViewById(R.id.button_sub);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenMain();
            }
        });
    }

    private void setScreenSub3(){
        setContentView(R.layout.activity_sub3);
        final ImageView imageView2  = this.findViewById(R.id.enemy_view);
        final ImageView actionView1 = this.findViewById(R.id.action_view1);
        final ImageView actionView2 = this.findViewById(R.id.action_view2);
        final ImageView actionView3 = this.findViewById(R.id.action_view3);
        imageView2.setImageResource(R.drawable.enemy015a);
        actionView1.setImageResource(R.drawable.human_default1);
        actionView2.setImageResource(R.drawable.squwat);
        actionView3.setImageResource(R.drawable.swing);


        final Intent intent = new Intent(getApplication(), AccelerometerPlayActivity.class);
        Button actionButton1 = findViewById(R.id.button_sub1);
        actionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "1";
                intent.putExtra("act", msg);
                startActivity(intent);
            }
        });
        Button actionButton2 = findViewById(R.id.button_sub2);
        actionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "2";
                intent.putExtra("act", msg);
                startActivity(intent);
            }
        });
        Button actionButton3 = findViewById(R.id.button_sub3);
        actionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "3";
                intent.putExtra("act", msg);
                startActivity(intent);
            }
        });
    }

    /***
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub);
        dash_count = this.findViewById(R.id.dash_count);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        bar = findViewById(R.id.progressBar2);
        bar.setMax(0);

        sensorManager.registerListener(new SensorEventListener() {
            private int mCounter = 0;
            @Override
            public void onSensorChanged(SensorEvent event) {
                mCounter++;
                dash_count.setText(String.valueOf(mCounter));
                bar.setProgress(100+(2*Integer.valueOf(dash_count.getText().toString())) );

            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        }, sensor, SensorManager.SENSOR_DELAY_UI);

        Button returnButton = (Button)findViewById(R.id.button_sub);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), AccelerometerPlayActivity.class);
                startActivity(intent);
            }
        });
    }
     ***/
}