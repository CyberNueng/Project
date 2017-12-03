package cs355.project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity5 extends AppCompatActivity {
    MediaPlayer song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        song = MediaPlayer.create(MainActivity5.this, R.raw.bgsong);
        song.setLooping(true);
        song.start();
        TextView t1 = (TextView) findViewById(R.id.textView2);
        t1.setMovementMethod(LinkMovementMethod.getInstance());
        TextView t2 = (TextView) findViewById(R.id.textView3);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
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
