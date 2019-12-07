package com.shivtechs.mediaplayemodule;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;

import static com.shivtechs.mediaplayemodule.AudioPlayer.MODE_PATH;

public class PlayPauseDialog extends DialogFragment {

    private boolean audioRecordFlag = true;//True-playing,False-Not playing
    private MediaPlayer mediaPlayer = null;
    private SeekBar play_pause_seekbar;
    private ImageView recordBtn;
    public static String FILE_PROVIDER_NAME;
    private static int choice = MODE_PATH;

    //    Runnable run = this::seekUpdation;
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };
    Handler seekHandler = new Handler();

    private void seekUpdation() {

        if (mediaPlayer != null) {
            play_pause_seekbar.setProgress(mediaPlayer.getCurrentPosition());
        }

        if (play_pause_seekbar.getProgress() == play_pause_seekbar.getMax()) {
            recordBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            audioRecordFlag = false;
        }
        seekHandler.postDelayed(run, 1000);
    }

    public static PlayPauseDialog newInstance(String filprovider,@Nullable String filepath, @Nullable Integer resourcePath, int choice) {

        Bundle args = new Bundle();
        PlayPauseDialog.choice = choice;
        FILE_PROVIDER_NAME = filprovider;
        if (choice == MODE_PATH) {
            args.putString("filepath", filepath);
        } else {
            args.putInt("filepath", resourcePath);
        }
        PlayPauseDialog fragment = new PlayPauseDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String filepath;
        int resourcePath = R.raw.guitar;
        Uri audioUri1 = null;
        if (choice == MODE_PATH) {
            filepath = getArguments().getString("filepath");
            audioUri1 = FileProvider.getUriForFile(getContext(), FILE_PROVIDER_NAME, new File(filepath));
        } else resourcePath = getArguments().getInt("filepath");
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.record_dialog, false)
                .show();

        View layout = dialog.getCustomView();
        recordBtn = layout.findViewById(R.id.recording_button);
        play_pause_seekbar = layout.findViewById(R.id.recording_seekbar);

        if (choice == MODE_PATH) mediaPlayer = MediaPlayer.create(getContext(), audioUri1);
        else {
            try {
                mediaPlayer = MediaPlayer.create(getContext(), resourcePath);
            } catch (Exception e) {
                Toast.makeText(getContext(), "The provided resource file doesn't exist", Toast.LENGTH_SHORT).show();
            }
        }

        if (mediaPlayer != null) {
            play_pause_seekbar.setMax(mediaPlayer.getDuration());
        } else {
            Toast.makeText(getActivity(), "File not downloaded yet", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

        if (savedInstanceState != null) {
            int progress = savedInstanceState.getInt("progress");
            audioRecordFlag = savedInstanceState.getBoolean("audioRecordFlag");
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(progress);
            }
            play_pause_seekbar.setProgress(progress);
        }

        seekUpdation();

        if (audioRecordFlag) {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
            recordBtn.setImageResource(R.drawable.ic_pause_black_24dp);
        }

        //Listen to the seek changes
        play_pause_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(seekBar.getProgress());

                    if (!mediaPlayer.isPlaying() && mediaPlayer.getCurrentPosition() != mediaPlayer.getDuration()) {
                        mediaPlayer.start();
                        recordBtn.setImageResource(R.drawable.ic_pause_black_24dp);
                        audioRecordFlag = true;
                    }
                }
            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    if (!audioRecordFlag) {
                        //play the audio
                        mediaPlayer.start();
                        recordBtn.setImageResource(R.drawable.ic_pause_black_24dp);
                        audioRecordFlag = true;
                    } else {
                        //stop the audio
                        mediaPlayer.pause();
                        recordBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        audioRecordFlag = false;
                    }
                }
            }
        });

        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        audioRecordFlag = false;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("progress", play_pause_seekbar.getProgress());
        outState.putBoolean("audioRecordFlag", audioRecordFlag);
    }
}
