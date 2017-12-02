package cs355.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity2 extends Activity implements SensorEventListener {
    SensorManager sensorManager, sensorManager2;
    Sensor accelerometer;
    Sensor magnetometer;
    Handler hdr = new Handler();
    float acc_x, acc_y, acc_z, orien_y;
    float[] mGravity;
    float[] mGeomagnetic;
    int POLL_INTERVAL = 500;
    int shake_throw = 15;
    int shake_take = 10;
    int pause_shake = 5;
    int canTake = 0;
    BroadcastReceiver batteryInfoReceiver;
    ArrayList<ImageView> list = new ArrayList<ImageView>();
    TranslateAnimation ta, ta1, ta2, ta3, ta4, ta5, ta6;
    int state = 0; //0 for throw state, 1 for up state, 2foe fall state
    int stage = 1; //number of pokemon troll
    int floor = 1450;
    boolean candead = false;
    Vibrator v;
    int x[] = new int[5];
    int y[] = new int[5];
    boolean pause = false;
    int L_R = 0; //0=left 1=right 2=not check
    LinearLayout pauseLayout,leftLayout,rightLayout;
    ImageButton pBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
        pauseLayout = (LinearLayout)findViewById(R.id.pause);
        leftLayout = (LinearLayout)findViewById(R.id.left);
        rightLayout = (LinearLayout)findViewById(R.id.right);
        pBtn = (ImageButton)findViewById(R.id.puaseBtn);
        pauseLayout.setVisibility(LinearLayout.INVISIBLE);
        leftLayout.setVisibility(LinearLayout.INVISIBLE);
        rightLayout.setVisibility(LinearLayout.INVISIBLE);
        v = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager2=(SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        accelerometer = sensorManager2.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager2.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager2.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager2.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        MediaPlayer song = MediaPlayer.create(MainActivity2.this, R.raw.bgsong);
        song.setLooping(true);
        song.start();
        createBtn();
        setPoke();
    }

    public void onSensorChanged(SensorEvent event){
        int type = event.sensor.getType();
        if(type==Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values;
            acc_x=event.values[0];
            acc_y=event.values[1];
            acc_z=event.values[2];
        }
        if(type==Sensor.TYPE_MAGNETIC_FIELD){
            mGeomagnetic = event.values;
        }
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                orien_y = orientation[2]; // orientation contains: azimut, pitch and roll
            }
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
        if(stage==4&&canTake==0){

        }
        if(state==0&&!pause)
        {
            if( (Math.abs(acc_x)>shake_throw) || (Math.abs(acc_y)>shake_throw) || (Math.abs(acc_z)>shake_throw) ) {
                v.vibrate(500);
                throwPoke();
                state=1;
            }
        }
        else if(state==1&&!pause)
        {
            if((orien_y<(-20))&&L_R==0){
                leftLayout.setVisibility(LinearLayout.INVISIBLE);
                canTake--;
                L_R=2;
            } else if((orien_y<(20))&&L_R==1){
                rightLayout.setVisibility(LinearLayout.INVISIBLE);
                canTake--;
                L_R=2;
            }
            fallPoke();
            int[] loca = new int[2];
            list.get(0).getLocationOnScreen(loca);
            if(canTake==0){
                state=0;
                stage++;
            }
            if(loca[1]==floor&&canTake>0){
                Intent intent = new Intent(MainActivity2.this,
                        MainActivity3.class);
                startActivity(intent);
            }
        }
    }

    public void throwPoke(){
        Random r = new Random();
        int rand_x2 = r.nextInt(800);
        int rand_y2 = r.nextInt(200)-400;
        ta = new TranslateAnimation(x[0], rand_x2, floor, rand_y2);
        int rand_speed = r.nextInt(200)+800;
        L_R = r.nextInt(1);
        ta.setDuration(rand_speed);
        ta.setFillAfter(true);
        MediaPlayer song = MediaPlayer.create(MainActivity2.this, R.raw.through);
        song.setLooping(false);
        song.start();
        list.get(0).startAnimation(ta);
        x[0] = rand_x2;
        y[0] = rand_y2;
        switch(stage){
            case 1: canTake=1;
            case 2: canTake=2;
            case 3: canTake=3;
            case 4: canTake=4;
        }
    }

    public void fallPoke(){
        Random r = new Random();
        int rand_x2 = r.nextInt(800);
        ta = new TranslateAnimation(x[0], rand_x2, y[0], floor);
        int rand_speed = r.nextInt(200)+800;
        ta.setDuration(rand_speed);
        ta.setFillAfter(true);
        list.get(0).startAnimation(ta);
        x[0] = rand_x2;
        y[0] = floor;
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

    public void pause(){
        pause = true;
        pauseLayout.setVisibility(LinearLayout.VISIBLE);
        pBtn.setVisibility(ImageButton.INVISIBLE);
    }

    public void unpuase(){
        pause = false;
        pauseLayout.setVisibility(LinearLayout.INVISIBLE);
        pBtn.setVisibility(ImageButton.VISIBLE);
    }

    public void restart(){
        finish();
        startActivity(getIntent());
    }

    public void createBtn(){
        ImageButton btnPuase = (ImageButton)findViewById(R.id.puaseBtn);
        btnPuase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pause();
            }
        });
        ImageButton btnPlay = (ImageButton)findViewById(R.id.play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unpuase();
            }
        });
        ImageButton btnRestart= (ImageButton)findViewById(R.id.restart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                restart();
            }
        });
    }
}
