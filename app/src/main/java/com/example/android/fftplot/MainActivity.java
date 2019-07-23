package com.example.android.fftplot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private Boolean mRecordingStatus = false;
    private LinearLayout mLinearLayout;
    private FFTView mFFTView;
    private static final int PERMISSION_RECORD_AUDIO = 0;

    private String mLogKey = "com.example.android.fftplot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayout = findViewById(R.id.linear_layout);
        mFFTView = new FFTView(this);

        mLinearLayout.addView(mFFTView);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO);
        }
        new AudioRecordTask(MediaRecorder.AudioSource.MIC,
                44100,
                512,
                1024,
                8192,
                this).execute();
        mRecordingStatus = true;


        getSupportActionBar().hide();

    }

    public Boolean getRecordingStatus() {
        return mRecordingStatus;
    }

    void receiveData(Vector<Float> freqList, Vector<Float> amplList) {
        mFFTView.setData(freqList, amplList);

        Log.d(mLogKey, "receiveData " + freqList.size());
    }
}
