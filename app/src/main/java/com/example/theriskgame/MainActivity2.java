package com.example.theriskgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor photometerSensor;
    private TextView lightLevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        lightLevelTextView = findViewById(R.id.lumiere);

        // Get the SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Get the photometer sensor
        photometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Register the sensor listener
        sensorManager.registerListener(this, photometerSensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lightLevel = event.values[0];

        // Update the UI with the light level
        lightLevelTextView.setText(String.format("Votre niveau de lumière : %.2f lux", lightLevel));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}