package com.example.android.fftplot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private Boolean mRecordingStatus = false;
    private LinearLayout mLinearLayout;
    private FFTView mFFTView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayout = findViewById(R.id.linear_layout);
        mFFTView = new FFTView(this);

        mLinearLayout.addView(mFFTView);

    }

    public Boolean getRecordingStatus() {
        return mRecordingStatus;
    }

    void receiveData(Vector<Float> freqList, Vector<Float> amplList) {

    }
}
