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
    public int talkcounter = 0 ;
    public TextView talk_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setScreenTalk();
        setScreenMain();

    }

    private void setScreenMain(){
        setContentView(R.layout.activity_sub);
        final ImageView onayamiman = this.findViewById(R.id.onayamiman);
        onayamiman.setImageResource(R.drawable.onayami);

        Button actionButton1 = findViewById(R.id.button_sub1);
        actionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenSub3();
            }
        });
        Button actionButton2 = findViewById(R.id.button_sub2);
        actionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenTalk();
            }
        });
        Button actionButton3 = findViewById(R.id.button_sub3);
        actionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreenSub2();
            }
        });
    }

    private void setScreenTalk(){
        setContentView(R.layout.activity_talk);
        talk_message = (TextView)this.findViewById(R.id.text);
        final ImageView onayamiman = this.findViewById(R.id.onayamiman);
        onayamiman.setImageResource(R.drawable.onayami);

        Button actionButtonNext = findViewById(R.id.tsugihe);

        actionButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talkcounter++;
                switch (talkcounter){
                    case 1:
                        talk_message.setText(R.string.b_talk1);
                        break;
                    case 2:
                        talk_message.setText(R.string.a_talk2);
                        break;
                    case 3:
                        talk_message.setText(R.string.b_talk2);
                        break;
                    case 4:
                        talk_message.setText(R.string.a_talk3);
                        break;
                    case 5:
                        talk_message.setText(R.string.b_talk3);
                        break;
                    case 6:
                        talkcounter = 0;
                        setScreenSub2();
                }
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

        Button actionButton1 = findViewById(R.id.button_sub1);
        actionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "1";
                setScreenPreDescription(msg);
            }
        });
        Button actionButton2 = findViewById(R.id.button_sub2);
        actionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "2";
                setScreenPreDescription(msg);
            }
        });
        Button actionButton3 = findViewById(R.id.button_sub3);
        actionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "3";
                setScreenPreDescription(msg);
             }
        });
    }

    private void setScreenPreDescription(final String _msg){
        setContentView(R.layout.activity_predescription);

        final ImageView imageView_pre  = this.findViewById(R.id.enemy_view);
        final ImageView actionView_pre = this.findViewById(R.id.action_description);
        //imageView_pre.setImageResource(R.drawable.enemy015a);
        switch (Integer.valueOf(_msg)) {
            case 1:
                actionView_pre.setImageResource(R.drawable.human_default1);
                break;
            case 2:
                actionView_pre.setImageResource(R.drawable.squwat);
                break;
            case 3:
                actionView_pre.setImageResource(R.drawable.swing);
                break;
        }
        Button returnButton = findViewById(R.id.button_next);
        final Intent intent = new Intent(getApplication(), AccelerometerPlayActivity.class);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                intent.putExtra("act", _msg);
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