package com.example.android.fftplot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import android.widget.LinearLayout;

import java.util.Vector;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {

    private Boolean mRecordingStatus = false;
    private LinearLayout mLinearLayout;
    private FFTView mFFTView;
    private static final int TEXT_REQUEST = 1;

    private static final float SWIPE_THRESHOLD = 100.0f;
    private static final int PERMISSION_RECORD_AUDIO = 0;
    private static final int PERMISSION_CAPTURE_AUDIO_OUTPUT = 1;

    private FFTPlotConfiguration mFFTPlotConfiguration;
    private boolean mConfigurationActivityLaunched;

    public static final String mLogKey = "com.example.android.fftplot.MainActivity";

    private GestureDetectorCompat gDetector;

    AudioRecordTask mAudioRecordTask;

    private void setFFTActive() {
        if (mRecordingStatus) {
            mRecordingStatus = false;
        } else {
            mRecordingStatus = true;
            mAudioRecordTask = new AudioRecordTask(
                    mFFTPlotConfiguration.mAudioSource,
                    mFFTPlotConfiguration.mSampleRate,
                    mFFTPlotConfiguration.mBufferSizeRead,
                    mFFTPlotConfiguration.mSamplesPerUpdate,
                    mFFTPlotConfiguration.mFFTSize,
                    this);
            mAudioRecordTask.execute();
        }

        mFFTView.setActive(mRecordingStatus);
        Log.d(mLogKey, "MainActivity.setFFTActive");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize default configuration:
        mFFTPlotConfiguration = new FFTPlotConfiguration();

        mLinearLayout = findViewById(R.id.linear_layout);
        mFFTView = new FFTView(this);
        mFFTView.setConfiguration(mFFTPlotConfiguration.mMinFrequency*0.9999f,
                mFFTPlotConfiguration.mMaxFrequency*1.0001f,
                mFFTPlotConfiguration.mMinAmplitude*0.9999f,
                mFFTPlotConfiguration.mMaxAmplitude*1.0001f);


        this.gDetector = new GestureDetectorCompat(this, this);
        gDetector.setOnDoubleTapListener(this);

        mLinearLayout.addView(mFFTView);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO);
        }

        mRecordingStatus = true;
        getSupportActionBar().hide();
        mConfigurationActivityLaunched = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mConfigurationActivityLaunched = false;

        Log.d(mLogKey, "onActivityResult " + requestCode + " " + resultCode);

        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                mFFTPlotConfiguration.readConfigurationFromIntent(data);
                mFFTView.setConfiguration(mFFTPlotConfiguration.mMinFrequency*0.9999f,
                                          mFFTPlotConfiguration.mMaxFrequency*1.0001f,
                                          mFFTPlotConfiguration.mMinAmplitude*0.9999f,
                                          mFFTPlotConfiguration.mMaxAmplitude*1.0001f);
                Log.d(mLogKey, "New Configuration " + mFFTPlotConfiguration.toString());
            }
        }
    }

    private void launchConfiguration() {
        if (mConfigurationActivityLaunched == false) {
            mConfigurationActivityLaunched = true;
            mRecordingStatus = false;
            mFFTView.setActive(mRecordingStatus);

            Intent intent = new Intent(this, ConfigurationActivity.class);
            mFFTPlotConfiguration.writeConfigurationToIntent(intent);
            Log.d(mLogKey, "Configuration set to intent " + mFFTPlotConfiguration.toString());
            startActivityForResult(intent, TEXT_REQUEST);
        }
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(mLogKey, "onDown");
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
        Log.d(mLogKey, "onFlingf");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(mLogKey, "onLongPress");
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
        Log.d(mLogKey, "onScroll " + distanceX);

        if (distanceX > SWIPE_THRESHOLD) {
            launchConfiguration();
        }

        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(mLogKey, "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(mLogKey, "onSingleTapUp");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(mLogKey, "onDoubleTapEvent");
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(mLogKey, "onDoubleTap");
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(mLogKey, "onSingleTapConfirmed");
        setFFTActive();

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public Boolean getRecordingStatus() {
        return mRecordingStatus;
    }

    void receiveData(Vector<Float> freqList, Vector<Float> amplList) {
        mFFTView.setData(freqList, amplList);

        Log.d(mLogKey, "receiveData " + freqList.size());
    }
}
