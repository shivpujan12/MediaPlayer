package com.shivtechs.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.shivtechs.mediaplayemodule.AudioPlayer;

import static com.shivtechs.mediaplayemodule.AudioPlayer.MODE_PATH;
import static com.shivtechs.mediaplayemodule.AudioPlayer.MODE_RESOURCE;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 AudioPlayer player2 = new AudioPlayer(MainActivity.this,isFinishing(),getSupportFragmentManager(),"/storage/emulated/0/Download/a.mp3",MODE_PATH);
//                AudioPlayer player = new AudioPlayer(MainActivity.this,isFinishing(),getSupportFragmentManager(),R.raw.guitar,MODE_RESOURCE);
            }
        });
    }
}
