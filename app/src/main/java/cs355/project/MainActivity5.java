package cs355.project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity5 extends AppCompatActivity {
    MediaPlayer song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        song = MediaPlayer.create(MainActivity5.this, R.raw.bgsong);
        song.setLooping(true);
        song.start();
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
        super.onBackPressed();
        song.stop();
        Intent intent = new Intent(MainActivity5.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }
}
