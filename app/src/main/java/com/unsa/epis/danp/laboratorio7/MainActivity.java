package com.unsa.epis.danp.laboratorio7;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import com.unsa.epis.danp.laboratorio7.databinding.ActivityMainBinding;

import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private SensorManager sensorManager;

    private Sensor sensor_accelerometer;
    private SensorEventListener sensorEventListener_accelerometer;

    private Sensor sensor_magnetric;
    private SensorEventListener sensorEventListener_magnetric;

    private TextView x_accelerometer_textview;
    private TextView y_accelerometer_textview;
    private TextView z_accelerometer_textview;
    private TextView accelerometer_error_textview;

    private TextView x_magnetometer_textview;
    private TextView y_magnetometer_textview;
    private TextView z_magnetometer_textview;
    private TextView magnetometer_error_textview;


    private TextView x_direcion_textview;
    private TextView y_direcion_textview;
    private TextView z_direcion_textview;

    private TextView xy_angle_textview;
    private TextView zy_angle_textview;
    private TextView xz_angle_textview;


    private DecimalFormat decimalFormat;

    private float x_direction;
    private float y_direction;
    private float z_direction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        decimalFormat = new DecimalFormat("00.#####");

        x_accelerometer_textview = (TextView) findViewById(R.id.x_accelerometer_text_view);
        y_accelerometer_textview = (TextView) findViewById(R.id.y_accelerometer_text_view);
        z_accelerometer_textview = (TextView) findViewById(R.id.z_accelerometer_text_view);
        accelerometer_error_textview = (TextView) findViewById(R.id.accelerometer_error_textview);

        x_magnetometer_textview = (TextView) findViewById(R.id.x_magnetometer_text_view);
        y_magnetometer_textview = (TextView) findViewById(R.id.y_magnetometer_text_view);
        z_magnetometer_textview = (TextView) findViewById(R.id.z_magnetometer_text_view);
        magnetometer_error_textview = (TextView) findViewById(R.id.magnetometer_error_textview2);

        x_direcion_textview = (TextView) findViewById(R.id.x_direction_text_view);
        y_direcion_textview = (TextView) findViewById(R.id.y_direction_text_view);
        z_direcion_textview = (TextView) findViewById(R.id.z_direction_text_view);

        xy_angle_textview = (TextView) findViewById(R.id.xy_angle_text_view);
        zy_angle_textview = (TextView) findViewById(R.id.yz_angle_text_view);
        xz_angle_textview = (TextView) findViewById(R.id.xz_angle_text_view);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor_accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor_magnetric = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (sensor_accelerometer ==null){
            accelerometer_error_textview.setText("Dispositivo no cuenta con sensor");
        } else {
            sensorEventListener_accelerometer = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    MainActivity.this.calculate(
                            event.values[0],
                            event.values[1],
                            event.values[0]);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
            sensorManager.registerListener(
                    sensorEventListener_accelerometer,
                    sensor_accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }
        if (sensor_magnetric == null){
            magnetometer_error_textview.setText("Dispositivo no cuenta con sensor");
        } else {
            sensorEventListener_magnetric = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    String result = "X: " + event.values[0] + " Y: " + event.values[1] + " Z: " + event.values[2];
                    Toast.makeText(MainActivity.this,result, Toast.LENGTH_LONG).show();
                    System.out.println(result);
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
            sensorManager.registerListener(sensorEventListener_magnetric, sensor_magnetric, SensorManager.SENSOR_MAGNETIC_FIELD);
        }

    }

    protected void calculate(float x, float y, float z){
        this.show_accelerometer(x,y,z);
        x_direction = x/SensorManager.GRAVITY_EARTH;
        y_direction = y/SensorManager.GRAVITY_EARTH;
        z_direction = z/SensorManager.GRAVITY_EARTH;
        this.show_direction(x_direction, y_direction, z_direction);
        float xy_angle = this.angle_between_vectors(new float[]{x_direction, y_direction}, new float[]{1, 0});
        float zy_angle = this.angle_between_vectors(new float[]{z_direction, y_direction}, new float[]{1, 0});;
        float xz_angle = this.angle_between_vectors(new float[]{x_direction, z_direction}, new float[]{1, 0});;
        show_angles(xy_angle, zy_angle, xz_angle);
    }

    protected void show_accelerometer(float x, float y, float z){
        x_accelerometer_textview.setText(decimalFormat.format(x));
        y_accelerometer_textview.setText(decimalFormat.format(y));
        z_accelerometer_textview.setText(decimalFormat.format(z));
    }

    protected void show_direction(float x, float y, float z){
        x_direcion_textview.setText(decimalFormat.format(x));
        y_direcion_textview.setText(decimalFormat.format(y));
        z_direcion_textview.setText(decimalFormat.format(z));
    }

    protected void show_angles(float xy, float zy, float xz){
        xy_angle_textview.setText(decimalFormat.format(Math.toDegrees(xy)));
        zy_angle_textview.setText(decimalFormat.format(Math.toDegrees(zy)));
        xz_angle_textview.setText(decimalFormat.format(Math.toDegrees(xz)));
    }

    protected float module_vector(float x, float y){
        return (float) Math.sqrt(x*x + y*y);
    }

    protected float angle_between_vectors(float[] u, float[]v){
        return (float) Math.acos(
                (u[0]*v[0] + u[1]*v[1]) / (this.module_vector(u[0], u[1]) * this.module_vector(v[0], v[1]))
        );
    }
}