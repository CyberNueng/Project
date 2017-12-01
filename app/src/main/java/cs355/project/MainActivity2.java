package cs355.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity2 extends Activity implements SensorEventListener {
    SensorManager sensorManager;
    Handler hdr = new Handler();
    float acc_x, acc_y, acc_z;
    int POLL_INTERVAL = 500;
    int shake_throw = 15;
    int shake_take = 10;
    int pause_shake = 5;
    int numberThrow = 0;
    BroadcastReceiver batteryInfoReceiver;
    ArrayList<ImageView> list = new ArrayList<ImageView>();
    TranslateAnimation ta, ta1, ta2, ta3, ta4, ta5, ta6;
    int state = 0; //0 for throw state, 1 for up state, 2foe fall state
    int stage = 1; //number of pokemon troll
    int floor = 1450;
    int deadline = 1300;
    Vibrator v;
    int x[] = new int[5];
    int y[] = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
        FrameLayout layout =(FrameLayout)findViewById(R.id.background);
        layout.setBackgroundResource(R.drawable.background);
        v = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        setPoke();
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
            if( (Math.abs(acc_x)>shake_throw) || (Math.abs(acc_y)>shake_throw) || (Math.abs(acc_z)>shake_throw) ) {
                v.vibrate(500);
                throwPoke();
                state=1;
            }
        }
        else if(state==1)
        {

        }
        else
        {

        }
    }

    public void throwPoke(){
        Random r = new Random();
        if(stage==1){
            int rand_x2 = r.nextInt(800);
            int rand_y2 = r.nextInt(200)-400;
            ta = new TranslateAnimation(x[numberThrow], rand_x2, floor, rand_y2);
            int rand_speed = r.nextInt(200)+400;
            ta.setDuration(rand_speed);
            ta.setFillAfter(true);
            list.get(numberThrow).startAnimation(ta);
            x[numberThrow] = rand_x2;
            y[numberThrow] = rand_y2;
            numberThrow++;
        }
    }

    public void setPoke(){
        list.add((ImageView)findViewById(R.id.img1));
        list.add((ImageView)findViewById(R.id.img2));
        list.add((ImageView)findViewById(R.id.img3));
        list.add((ImageView)findViewById(R.id.img4));
        list.add((ImageView)findViewById(R.id.img5));
        list.add((ImageView)findViewById(R.id.img6));
        Collections.shuffle(list);
        list.remove(0).setVisibility(View.GONE);
        for(int i = 0; i<5; i++) {
            Random r = new Random();
            x[i] = r.nextInt(800);
            y[i] = floor;
            int rand_y = r.nextInt(500);
            int rand_x2 = r.nextInt(800);
            ta = new TranslateAnimation(x[i], rand_x2, rand_y, floor);
            int rand_drop = r.nextInt(400)+800;
            ta.setDuration(rand_drop);
            ta.setFillAfter(true);
            list.get(i).setAnimation(ta);
            ta.start();
        }
    }
}
