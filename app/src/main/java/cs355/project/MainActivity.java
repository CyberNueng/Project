package cs355.project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    MediaPlayer song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //LinearLayout layout =(LinearLayout)findViewById(R.id.background);
        //layout.setBackgroundResource(R.drawable.background);

        song = MediaPlayer.create(MainActivity.this, R.raw.bgsong);
        song.setLooping(true);
        song.start();

        Button Button = (Button) findViewById(R.id.button);
        Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                song.stop();
                Intent intent = new Intent(MainActivity.this,
                        MainActivity2.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        song.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        song.stop();
    }

    @Override
    public void onBackPressed(){
        song.stop();
        finish();
    }
}
