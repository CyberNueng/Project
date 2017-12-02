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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    Handler hdr = new Handler();
    float acc_x, acc_y, acc_z, loca;
    int POLL_INTERVAL = 250;
    int shake_throw = 15;
    int canTake = 0;
    BroadcastReceiver batteryInfoReceiver;
    ArrayList<ImageView> list;
    TranslateAnimation ta, ta1, ta2;
    int state = 0; //0 for throw state, 1 for up state, 2foe fall state
    int stage = 1; //number of pokemon troll
    int floor = 1450;
    Vibrator v;
    int x[] = new int[5];
    int y[] = new int[5];
    boolean pause = false;
    int L_R = 0; //0=left 1=right 2=not check
    LinearLayout pauseLayout,leftLayout,rightLayout;
    ImageButton pBtn;
    boolean fall = false;
    boolean canthrow = true;
    Random r = new Random();
    MediaPlayer songThrow, song, songtake;

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
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        song = MediaPlayer.create(MainActivity2.this, R.raw.bgsong);
        songThrow = MediaPlayer.create(MainActivity2.this, R.raw.through);
        song.setLooping(true);
        songThrow.setLooping(false);
        song.start();
        createBtn();
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
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
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
        if(state==0&&!pause)
        {
            if(stage==5){
                Intent intent = new Intent(MainActivity2.this,
                        MainActivity4.class);
                startActivity(intent);
            }
            if( (Math.abs(acc_x)>shake_throw) || (Math.abs(acc_y)>shake_throw) || (Math.abs(acc_z)>shake_throw) && canthrow) {
                v.vibrate(500);
                throwPoke();
                state=1;
            }
        }
        else if(state==1&&!pause)
        {
            if(canTake==0&&list.size()==1){
                state=0;
                stage++;
                setPoke();
            }

            if((acc_x>(5))&&L_R==0){
                leftLayout.setVisibility(LinearLayout.INVISIBLE);
                songtake = MediaPlayer.create(MainActivity2.this, R.raw.sucess);
                songtake.setLooping(false);
                songtake.start();
                list.get(1).clearAnimation();
                list.remove(1).setVisibility(View.GONE);
                canTake--;
                L_R=2;
            } else if((acc_x<(-5))&&L_R==1){
                rightLayout.setVisibility(LinearLayout.INVISIBLE);
                songtake.start();
                list.get(1).clearAnimation();
                list.remove(1).setVisibility(View.GONE);
                canTake--;
                L_R=2;
            }

            if((acc_x<(5))&&(acc_x>(-5))&&L_R==2&&canTake>0&&list.size()>=2){
                L_R = r.nextInt(2);
                if(canTake>0) {
                    if (L_R == 0) leftLayout.setVisibility(LinearLayout.VISIBLE);
                    else if (L_R == 1) rightLayout.setVisibility(LinearLayout.VISIBLE);
                }
            }
            if(loca==floor&&canTake==0){
                state=0;
            }else if(loca==floor&&canTake>0){
                Intent intent = new Intent(MainActivity2.this,
                        MainActivity3.class);
                startActivity(intent);
            }

        }
    }

    public void throwPoke(){
        int rand_x2 = r.nextInt(800);
        int rand_y2 = r.nextInt(200)-400;
        ta2 = new TranslateAnimation(x[0], rand_x2, floor, rand_y2);
        int speed = 1500;
        L_R = r.nextInt(2);
        ta2.setDuration(speed);
        ta2.setFillAfter(true);
        fall = true;
        canthrow =false;
        ta2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                loca = 0;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(fall) {
                    fall =false;
                    fallPoke();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        songThrow.start();
        list.get(0).startAnimation(ta2);
        x[0] = rand_x2;
        y[0] = rand_y2;
        switch(stage){
            case 1: canTake=1; break;
            case 2: canTake=Math.min(2,list.size()-1); break;
            case 3: canTake=Math.min(3,list.size()-1); break;
            case 4: canTake=Math.min(4,list.size()-1); break;
        }
        if(L_R==0) leftLayout.setVisibility(LinearLayout.VISIBLE);
        else if(L_R==1) rightLayout.setVisibility(LinearLayout.VISIBLE);
    }

    public void fallPoke(){
        int rand_x2 = r.nextInt(800);
        ta1 = new TranslateAnimation(x[0], rand_x2, y[0], floor);
        int speed = 1500;
        ta1.setDuration(speed);
        ta1.setFillAfter(true);
        ta1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                loca = 1450;
                canthrow = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        list.get(0).startAnimation(ta1);
        x[0] = rand_x2;
        y[0] = floor;
    }

    public void setPoke(){
        list = new ArrayList<ImageView>();
        list.add((ImageView)findViewById(R.id.img1));
        list.add((ImageView)findViewById(R.id.img2));
        list.add((ImageView)findViewById(R.id.img3));
        list.add((ImageView)findViewById(R.id.img4));
        list.add((ImageView)findViewById(R.id.img5));
        list.add((ImageView)findViewById(R.id.img6));
        Collections.shuffle(list);
        list.remove(0).setVisibility(View.GONE);
        for(int i = 0; i<5; i++) {
            r = new Random();
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
