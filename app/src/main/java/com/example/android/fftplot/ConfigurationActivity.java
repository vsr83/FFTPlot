package com.example.android.fftplot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.widget.TextView;

public class ConfigurationActivity extends AppCompatActivity  implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener  {

    public static final String mLogKey = "com.example.android.fftplot.ConfigurationActivity";

    private FFTPlotConfiguration mFFTPlotConfiguration;
    private TextView mTextViewFFTSize;
    private SeekBar  mSeekBarFFTSize;
    private TextView mTextViewSamplesPerUpdate;
    private SeekBar  mSeekBarSamplesPerUpdate;

    private TextView mTextViewMinimumFrequency;
    private TextView mTextViewMaximumFrequency;
    private TextView mTextViewMinimumAmplitude;
    private TextView mTextViewMaximumAmplitude;

    private SeekBar mSeekBarMinimumFrequency;
    private SeekBar mSeekBarMaximumFrequency;
    private SeekBar mSeekBarMinimumAmplitude;
    private SeekBar mSeekBarMaximumAmplitude;

    private GestureDetectorCompat gDetector;
    private static final float SWIPE_THRESHOLD = 100.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mFFTPlotConfiguration = new FFTPlotConfiguration();
        mFFTPlotConfiguration.readConfigurationFromIntent(intent);

        setContentView(R.layout.activity_configuration);

        mTextViewFFTSize = findViewById(R.id.text_FFTSize);
        mSeekBarFFTSize = findViewById(R.id.seekBar_FFTSize);
        mTextViewSamplesPerUpdate = findViewById(R.id.text_SamplesPerUpdate);
        mSeekBarSamplesPerUpdate = findViewById(R.id.seekBar_SamplesPerUpdate);

        mTextViewMinimumFrequency = findViewById(R.id.text_MinimumFrequency);
        mTextViewMaximumFrequency = findViewById(R.id.text_MaximumFrequency);
        mTextViewMinimumAmplitude = findViewById(R.id.text_MinimumAmplitude);
        mTextViewMaximumAmplitude = findViewById(R.id.text_MaximumAmplitude);

        mSeekBarMinimumFrequency = findViewById(R.id.seekBar_MinimumFrequency);
        mSeekBarMaximumFrequency = findViewById(R.id.seekBar_MaximumFrequency);
        mSeekBarMinimumAmplitude = findViewById(R.id.seekBar_MinimumAmplitude);
        mSeekBarMaximumAmplitude = findViewById(R.id.seekBar_MaximumAmplitude);

        mSeekBarSamplesPerUpdate.setOnSeekBarChangeListener(new SeekBarListener());
        mSeekBarFFTSize.setOnSeekBarChangeListener(new SeekBarListener());
        mSeekBarMinimumFrequency.setOnSeekBarChangeListener(new SeekBarListener());
        mSeekBarMaximumFrequency.setOnSeekBarChangeListener(new SeekBarListener());
        mSeekBarMinimumAmplitude.setOnSeekBarChangeListener(new SeekBarListener());
        mSeekBarMaximumAmplitude.setOnSeekBarChangeListener(new SeekBarListener());

        mSeekBarFFTSize.setProgress(FFTSizetoProgress(mFFTPlotConfiguration.mFFTSize));
        mSeekBarSamplesPerUpdate.setProgress(samplestoProgress(mFFTPlotConfiguration.mSamplesPerUpdate));
        mSeekBarMinimumFrequency.setProgress(frequencytoProgress(mFFTPlotConfiguration.mMinFrequency));
        mSeekBarMaximumFrequency.setProgress(frequencytoProgress(mFFTPlotConfiguration.mMaxFrequency));
        mSeekBarMinimumAmplitude.setProgress(amplitudetoProgress(mFFTPlotConfiguration.mMinAmplitude));
        mSeekBarMaximumAmplitude.setProgress(amplitudetoProgress(mFFTPlotConfiguration.mMaxAmplitude));

        this.gDetector = new GestureDetectorCompat(this, this);

        Log.d(MainActivity.mLogKey, mFFTPlotConfiguration.toString());

    }

    private void returnToMainActivity() {
        Intent replyIntent = new Intent();
        mFFTPlotConfiguration.writeConfigurationToIntent(replyIntent);
        setResult(RESULT_OK, replyIntent);
        finish();
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

        if (distanceX < -SWIPE_THRESHOLD) {
            returnToMainActivity();
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
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    private static final double CUBE_ROOT_OF_10 = Math.pow(10.0, 1.0 / 3.0);

    static private int   FFTSizetoProgress(int FFTsize) {return (int) Math.round(Math.log((double) FFTsize) / Math.log(2.0)) - 4;}
    static private int   progresstoFFTSize(int progress) {return 16 << progress;}
    static private int   samplestoProgress(int FFTsize) {return (int) Math.round(Math.log((double) FFTsize) / Math.log(2.0)) - 8;}
    static private int   progresstoSamples(int progress) {return 256 << progress;}
    static private int   frequencytoProgress(float frequency) {return (int) Math.round(Math.log((double)frequency) / Math.log(CUBE_ROOT_OF_10));}
    static private float progressToFrequency(int progress) {return (float) Math.pow(CUBE_ROOT_OF_10, (double)progress);}
    static private int   amplitudetoProgress(float amplitude) {return (int) Math.round(Math.log10((double)amplitude)) + (int)5;}
    static private float progressToAmplitude(int progress) {return (float) Math.pow(10.0, (double)(progress - 5.0));}

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()) {
                case R.id.seekBar_FFTSize:
                    Log.d(MainActivity.mLogKey, "mSeekBarFFTSize " + progress);
                    mFFTPlotConfiguration.mFFTSize = progresstoFFTSize(progress);
                    mTextViewFFTSize.setText("" + mFFTPlotConfiguration.mFFTSize);
                    break;
                case R.id.seekBar_SamplesPerUpdate:
                    Log.d(MainActivity.mLogKey, "mSeekBarSamplesPerUpdate " + progress);
                    mFFTPlotConfiguration.mSamplesPerUpdate = progresstoSamples(progress);
                    mTextViewSamplesPerUpdate.setText("" + mFFTPlotConfiguration.mSamplesPerUpdate);
                    break;
                case R.id.seekBar_MinimumFrequency:
                    mFFTPlotConfiguration.mMinFrequency = progressToFrequency(progress);
                    Log.d(MainActivity.mLogKey, "mSeekBarMinimumFrequency " + progress + " " + mFFTPlotConfiguration.mMinFrequency);
                    mTextViewMinimumFrequency.setText("" + mFFTPlotConfiguration.mMinFrequency + "Hz");

                    if (mFFTPlotConfiguration.mMinFrequency >= mFFTPlotConfiguration.mMaxFrequency) {
                        if (progress < mSeekBarMaximumFrequency.getMax()) {
                            mSeekBarMaximumFrequency.setProgress(progress + 1);
                        } else {
                            mSeekBarMaximumFrequency.setProgress(mSeekBarMaximumFrequency.getMax());
                            mSeekBarMinimumFrequency.setProgress(mSeekBarMaximumFrequency.getMax() - 1);
                        }
                    }
                    break;
                case R.id.seekBar_MaximumFrequency:
                    mFFTPlotConfiguration.mMaxFrequency = progressToFrequency(progress);
                    Log.d(MainActivity.mLogKey, "mSeekBarMaximumFrequency " + progress + " " + mFFTPlotConfiguration.mMaxFrequency);
                    mTextViewMaximumFrequency.setText("" + mFFTPlotConfiguration.mMaxFrequency + "Hz");

                    if (mFFTPlotConfiguration.mMinFrequency >= mFFTPlotConfiguration.mMaxFrequency) {
                        if (progress > 0) {
                            mSeekBarMinimumFrequency.setProgress(progress - 1);
                        } else {
                            mSeekBarMinimumFrequency.setProgress(0);
                            mSeekBarMaximumFrequency.setProgress(1);
                        }
                    }
                    break;
                case R.id.seekBar_MinimumAmplitude:
                    mFFTPlotConfiguration.mMinAmplitude = progressToAmplitude(progress);
                    Log.d(MainActivity.mLogKey, "mSeekBarMinimumAmplitude " + progress + " " + mFFTPlotConfiguration.mMinAmplitude);
                    mTextViewMinimumAmplitude.setText("" + Math.round(20.0 * Math.log10((double) mFFTPlotConfiguration.mMinAmplitude)) + " dB");

                    if (mFFTPlotConfiguration.mMinAmplitude >= mFFTPlotConfiguration.mMaxAmplitude) {
                        if (progress < mSeekBarMinimumAmplitude.getMax()) {
                            mSeekBarMaximumAmplitude.setProgress(progress + 1);
                        } else {
                            mSeekBarMaximumAmplitude.setProgress(mSeekBarMinimumAmplitude.getMax());
                            mSeekBarMinimumAmplitude.setProgress(mSeekBarMinimumAmplitude.getMax() - 1);
                        }
                    }
                    break;
                case R.id.seekBar_MaximumAmplitude:
                    mFFTPlotConfiguration.mMaxAmplitude = progressToAmplitude(progress);
                    Log.d(MainActivity.mLogKey, "mSeekBarMaximumAmplitude " + progress + " " + mFFTPlotConfiguration.mMaxAmplitude);
                    mTextViewMaximumAmplitude.setText("" + Math.round(20.0 * Math.log10((double) mFFTPlotConfiguration.mMaxAmplitude)) + " dB");
                    if (mFFTPlotConfiguration.mMinAmplitude >= mFFTPlotConfiguration.mMaxAmplitude) {
                        if (progress > 0) {
                            mSeekBarMinimumAmplitude.setProgress(progress - 1);
                        } else {
                            mSeekBarMinimumAmplitude.setProgress(0);
                            mSeekBarMaximumAmplitude.setProgress(1);
                        }
                    }
                    break;
                default:
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
