package app.aya.music;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button     B_music , B_video ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        B_music = (Button) findViewById(R.id.music);
        B_video = (Button) findViewById(R.id.video);

        B_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(MainActivity.this,ListMusic.class);
                startActivity(intent);
            }
        });

        B_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(MainActivity.this,ListMusic.class);
                startActivity(intent);
            }
        });
    }

}
