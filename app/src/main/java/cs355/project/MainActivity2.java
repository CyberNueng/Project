package cs355.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        RelativeLayout layout =(RelativeLayout)findViewById(R.id.background);
        layout.setBackgroundResource(R.drawable.background);
    }
}
