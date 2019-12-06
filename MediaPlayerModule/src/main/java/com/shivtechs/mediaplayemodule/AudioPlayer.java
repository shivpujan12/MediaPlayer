package com.shivtechs.mediaplayemodule;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

public class AudioPlayer {
    private int choice;
    private FragmentManager fragmentManager;
    private boolean isFinishing;
    private Activity activity;
    private String path;
    private int resourcePath;
    public static  final  int MODE_PATH = 0;
    public static final int MODE_RESOURCE = 1;

    public AudioPlayer(Activity activity, boolean isFinishing,FragmentManager fragmentManager,String path,int choice){
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.isFinishing = isFinishing;
        this.path = path;
        this.choice = choice;
        playAudio();
    }

    public AudioPlayer(Activity activity,boolean isFinishing,FragmentManager fragmentManager,int path,int choice){
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.isFinishing = isFinishing;
        this.resourcePath = path;
        this.choice = choice;
        playAudio();
    }


    private void playAudio(){
        //Permission to read and write file on to storage
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1208);
        } else {
            if (!isFinishing && fragmentManager.findFragmentByTag("play_pause_dialog") == null) {
                if(choice==MODE_PATH) {
                    if (!path.equals("") && !path.isEmpty()) {
                        PlayPauseDialog newFragment = PlayPauseDialog.newInstance(path,null,choice);
                        newFragment.show(fragmentManager, "play_pause_dialog");
                    }
                } else if(choice==MODE_RESOURCE){

                        PlayPauseDialog newFragment = PlayPauseDialog.newInstance(null,resourcePath,choice);
                        newFragment.show(fragmentManager, "play_pause_dialog");

                }
            }
        }
    }
}
