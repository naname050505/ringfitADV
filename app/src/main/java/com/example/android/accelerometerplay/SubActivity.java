package com.example.android.accelerometerplay;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SubActivity extends Activity {
    public SensorManager sensorManager;
    public Sensor sensor;
    public TextView dash_count;
    private Sensor mAccelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub);
        dash_count = (TextView)this.findViewById(R.id.dash_count);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        sensorManager.registerListener(new SensorEventListener() {
            private int mCounter = 0;
            @Override
            public void onSensorChanged(SensorEvent event) {
                mCounter++;
                dash_count.setText(String.valueOf(mCounter));
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        }, sensor, SensorManager.SENSOR_DELAY_UI);

        Button returnButton = (Button)findViewById(R.id.button_sub);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}