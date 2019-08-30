package com.example.android_getting_started;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.util.Timer;
import android.os.Handler;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.app.Activity;
import android.os.Bundle;


//import GraphView



public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor mLight;
    private Sensor accel;
    private Sensor gyro;
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;
    private float lux;
    private TextView light, x, y, z,x1,y1,z1;
   // long z=-1000;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        light = (TextView) findViewById(R.id.textView);
//        x = (TextView) findViewById(R.id.textView2);
//        y = (TextView) findViewById(R.id.textView3);
//        z = (TextView) findViewById(R.id.textView4);
//        x1 = (TextView) findViewById(R.id.textView5);
//        y1 = (TextView) findViewById(R.id.textView6);
//        z1 = (TextView) findViewById(R.id.textView7);


        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight == null){
                Log.d(TAG, "Light Sensor is not present");
        }

        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // data
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setMinX(0);
        viewport.setMaxX(100);
        viewport.setScrollable(true);

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(final SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        lux = event.values[0];
//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);

        // Do something with this sensor value.
        Log.d(TAG, "onSensorChanged: Lux: " + lux);

        //Log.d(TAG, "Sensor evenType: " + event.sensor.getType());


//        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//            Log.d(TAG, "onSensorChanged: X: " + event.values[0]);
//            Log.d(TAG, "onSensorChanged: Y: " + event.values[1]);
//            Log.d(TAG, "onSensorChanged: Z: " + event.values[2]);
//        }
//        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
//
//            Log.d(TAG, "onSensorChanged: X: " + event.values[0]);
//            Log.d(TAG, "onSensorChanged: Y: " + event.values[1]);
//            Log.d(TAG, "onSensorChanged: Z: " + event.values[2]);
//        }

        runOnUiThread(new Runnable() {
            //z=System.currentTimeMillis();
            ///Date currentTime = Calendar.getInstance().getTime();

                          //sleep(1000);
                @Override
                public void run() {


                    //if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                        light.setText(Float.toString(event.values[0]));
                    //}
//                if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
//                    x.setText("Xgyro="+Float.toString(event.values[0]));
//                    y.setText("Ygyro="+Float.toString(event.values[1]));
//                    z.setText("Zgyro="+Float.toString(event.values[2]));
//                }
//                if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//                    x1.setText(Float.toString(event.values[0]));
//                    y1.setText(Float.toString(event.values[1]));
//                    z1.setText(Float.toString(event.values[2]));
//                }
                }

        });
    }

    @Override
    protected void onResume() {


        super.onResume();
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                for (int i = 0; i < 100; i++) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();
//        new Timer().schedule(
//                new TimerTask(){
//
//
//
//                }, 1000);
//        int c=0;
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        while(c++<10000000);
        sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
//         c=0;
//        while(c++<10000000);
//        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
//        c=0;
//        while(c++<10000000);
//        sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_UI);
//    }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }
    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(lastX++, lux), true, 100);
    }
}
