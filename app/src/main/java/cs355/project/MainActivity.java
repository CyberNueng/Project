package cs355.project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //LinearLayout layout =(LinearLayout)findViewById(R.id.background);
        //layout.setBackgroundResource(R.drawable.background);

        MediaPlayer song = MediaPlayer.create(MainActivity.this, R.raw.mon);
        song.setLooping(true);
        song.start();

        Button Button = (Button) findViewById(R.id.button);
        Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        MainActivity2.class);

                startActivity(intent);
            }
        });


    }
}
