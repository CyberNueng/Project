package cs355.project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity4 extends AppCompatActivity {
    MediaPlayer song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main4);
        ImageButton ib = (ImageButton)findViewById(R.id.dead2);
        song = MediaPlayer.create(MainActivity4.this, R.raw.win);
        ib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                song.stop();
                Intent intent = new Intent(MainActivity4.this,
                        MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });
        song.setLooping(false);
        song.start();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        song.stop();
        Intent intent = new Intent(MainActivity4.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }
}
