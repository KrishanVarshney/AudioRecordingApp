package com.example.keshu.audiorecordingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
{
    Button play,stop,record;
    MediaRecorder my_audio_recoder;
    MediaPlayer my_media;
    String outputfile;

    // Requesting permission to RECORD_AUDIO
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int WRITE_EXTERNAL_STORAGE = 201;
    private static final int READ_EXTERNAL_STORAGE = 202;

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case WRITE_EXTERNAL_STORAGE:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
            case READ_EXTERNAL_STORAGE:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play=(Button)findViewById(R.id.button);
        stop=(Button)findViewById(R.id.button2);
        record=(Button)findViewById(R.id.button3);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);



        stop.setEnabled(false);
        play.setEnabled(false);
        outputfile= Environment.getExternalStorageDirectory().getAbsolutePath()+"/recording.3gp";

        my_audio_recoder=new MediaRecorder();
        my_audio_recoder.reset();
        my_audio_recoder.setAudioSource(MediaRecorder.AudioSource.MIC);
        my_audio_recoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        my_audio_recoder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        my_audio_recoder.setOutputFile(outputfile);




        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    my_audio_recoder.prepare();
                    my_audio_recoder.start();
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, ""+e , Toast.LENGTH_SHORT).show();
                }
                record.setEnabled(false);
                stop.setEnabled(true);
                Toast.makeText(MainActivity.this, "Recording satrt...", Toast.LENGTH_SHORT).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_audio_recoder.stop();
                my_audio_recoder.release();
                my_audio_recoder=null;
                record.setEnabled(true);
                stop.setEnabled(false);
                play.setEnabled(true);
                Toast.makeText(MainActivity.this, "Successfuly Recorded..", Toast.LENGTH_SHORT).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer my_media=new MediaPlayer();
                try
                {
                    my_media.setDataSource(outputfile);
                    my_media.prepare();
                    my_media.start();
                    Toast.makeText(MainActivity.this, "Playing ....", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, ""+e , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
