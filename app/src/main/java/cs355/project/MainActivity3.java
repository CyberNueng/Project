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

public class MainActivity3 extends AppCompatActivity {
    MediaPlayer song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main3);
        ImageButton ib = (ImageButton)findViewById(R.id.dead);
        ib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                song.stop();
                Intent intent = new Intent(MainActivity3.this,
                        MainActivity2.class);

                startActivity(intent);
                finish();
            }
        });
        song = MediaPlayer.create(MainActivity3.this, R.raw.lose);
        song.setLooping(false);
        song.start();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
