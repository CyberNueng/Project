package cs355.project;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity2 extends Activity implements SensorEventListener {
    SensorManager sensorManager;
    Handler hdr = new Handler();
    float acc_x, acc_y, acc_z;
    int POLL_INTERVAL = 500;
    int shake_throw = 20;
    int shake_take = 10;
    int pause_shake = 5;
    BroadcastReceiver batteryInfoReceiver;
    ImageView img1, img2, img3, img4, img5, img6;
    TranslateAnimation ta1, ta2, ta3, ta4, ta5, ta6;
    int state = 0; //0 for throw state, 1 for up state, 2foe fall state
    int stage = 1; //number of pokemon troll
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
        LinearLayout layout =(LinearLayout)findViewById(R.id.background);
        layout.setBackgroundResource(R.drawable.background);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        img1 = (ImageView)findViewById(R.id.img1);
        ta1 = new TranslateAnimation(0, 100, 0, 1000);
        ta1.setDuration(1000);
        ta1.setFillAfter(true);
        img1.setAnimation(ta1);
        ta1.start();
    }

    public void onSensorChanged(SensorEvent event){
        int type = event.sensor.getType();
        if(type==Sensor.TYPE_ACCELEROMETER){
            acc_x=event.values[0];
            acc_y=event.values[1];
            acc_z=event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private Runnable pollTask = new Runnable() {
        public void run() {
            play();
            hdr.postDelayed(pollTask,POLL_INTERVAL);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(
                Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_NORMAL);
        this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        hdr.postDelayed(pollTask, POLL_INTERVAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        hdr.removeCallbacks(pollTask);
    }

    public void play(){
        if(state==0)
        {

        }
        else if(state==1)
        {

        }
        else
        {

        }
    }

    public void throwPoke(){

    }

    public void setPoke(){
        ArrayList<ImageView> list = new ArrayList<ImageView>();
        list.add((ImageView)findViewById(R.id.img1));
        list.add((ImageView)findViewById(R.id.img2));
        list.add((ImageView)findViewById(R.id.img3));
        list.add((ImageView)findViewById(R.id.img4));
        list.add((ImageView)findViewById(R.id.img5));
        list.add((ImageView)findViewById(R.id.img6));
        Collections.shuffle(list);
        list.remove(0).setVisibility(View.GONE);
    }
}
