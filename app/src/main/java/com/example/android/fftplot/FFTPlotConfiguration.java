package com.example.android.fftplot;

import android.content.Intent;
import android.media.MediaRecorder;
import android.util.Log;

public class FFTPlotConfiguration {
    private static final String EXTRA_AUDIO_SOURCE       = "com.example.android.fftplot.extra.AudioSource";
    private static final String EXTRA_SAMPLE_RATE        = "com.example.android.fftplot.extra.SampleRate";
    private static final String EXTRA_FFTSIZE            = "com.example.android.fftplot.extra.FFTSize";
    private static final String EXTRA_SAMPLES_PER_UPDATE = "com.example.android.fftplot.extra.SamplesPerUpdate";
    private static final String EXTRA_BUFFER_SIZE_READ   = "com.example.android.fftplot.extra.BufferSizeRead";
    private static final String EXTRA_FFT_MIN_FREQUENCY  = "com.example.android.fftplot.extra.FFTMinFrequency";
    private static final String EXTRA_FFT_MAX_FREQUENCY  = "com.example.android.fftplot.extra.FFTMaxFrequency";
    private static final String EXTRA_FFT_MIN_AMPLITUDE  = "com.example.android.fftplot.extra.FFTMinAmplitude";
    private static final String EXTRA_FFT_MAX_AMPLITUDE  = "com.example.android.fftplot.extra.FFTMaxAmplitude";

    public static final int sDefaultAudioSource      = MediaRecorder.AudioSource.MIC;
    public static final int sDefaultSampleRate       = 44100;
    public static final int sDefaultFFtSize          = 8192;
    public static final int sDefaultSamplesPerUpdate = 1024;
    public static final int sDefaultBufferSizeRead   = 512;

    public static final float sDefaultMinFrequency = 10.0f;
    public static final float sDefaultMaxFrequency = 20000.0f;
    public static final float sDefaultMinAmplitude = 0.1f;
    public static final float sDefaultMaxAmplitude = 1000.1f;

    public int mAudioSource;
    public int mSampleRate;
    public int mFFTSize;
    public int mSamplesPerUpdate;
    public int mBufferSizeRead;

    public float mMinFrequency;
    public float mMaxFrequency;
    public float mMinAmplitude;
    public float mMaxAmplitude;

    public FFTPlotConfiguration() {
        // Initialize default configuration:
        mAudioSource      = sDefaultAudioSource;
        mSampleRate       = sDefaultSampleRate;
        mFFTSize          = sDefaultFFtSize;
        mSamplesPerUpdate = sDefaultSamplesPerUpdate;
        mBufferSizeRead   = sDefaultBufferSizeRead;
        mMinFrequency     = sDefaultMinFrequency;
        mMaxFrequency     = sDefaultMaxFrequency;
        mMinAmplitude     = sDefaultMinAmplitude;
        mMaxAmplitude     = sDefaultMaxAmplitude;
    }

    public void writeConfigurationToIntent(Intent intent) {
        intent.putExtra(EXTRA_AUDIO_SOURCE, mAudioSource);
        intent.putExtra(EXTRA_SAMPLE_RATE, mSampleRate);
        intent.putExtra(EXTRA_FFTSIZE, mFFTSize);
        intent.putExtra(EXTRA_SAMPLES_PER_UPDATE, mSamplesPerUpdate);
        intent.putExtra(EXTRA_BUFFER_SIZE_READ, mBufferSizeRead);
        intent.putExtra(EXTRA_FFT_MIN_FREQUENCY, mMinFrequency);
        intent.putExtra(EXTRA_FFT_MAX_FREQUENCY, mMaxFrequency);
        intent.putExtra(EXTRA_FFT_MIN_AMPLITUDE, mMinAmplitude);
        intent.putExtra(EXTRA_FFT_MAX_AMPLITUDE, mMaxAmplitude);
    }

    public void readConfigurationFromIntent(Intent intent) {
        mAudioSource      = intent.getIntExtra(EXTRA_AUDIO_SOURCE, sDefaultAudioSource);
        mSampleRate       = intent.getIntExtra(EXTRA_SAMPLE_RATE, sDefaultSampleRate);
        mFFTSize          = intent.getIntExtra(EXTRA_FFTSIZE, sDefaultFFtSize);
        mSamplesPerUpdate = intent.getIntExtra(EXTRA_SAMPLES_PER_UPDATE, sDefaultSamplesPerUpdate);
        mBufferSizeRead   = intent.getIntExtra(EXTRA_BUFFER_SIZE_READ, sDefaultBufferSizeRead);
        mMinFrequency     = intent.getFloatExtra(EXTRA_FFT_MIN_FREQUENCY, sDefaultMinFrequency);
        mMaxFrequency     = intent.getFloatExtra(EXTRA_FFT_MAX_FREQUENCY, sDefaultMaxFrequency);
        mMinAmplitude     = intent.getFloatExtra(EXTRA_FFT_MIN_AMPLITUDE, sDefaultMinAmplitude);
        mMaxAmplitude     = intent.getFloatExtra(EXTRA_FFT_MAX_AMPLITUDE, sDefaultMaxAmplitude);
    }

    public String toString() {
        return "Audio Source " + mAudioSource +
               " Sample Rate " + mSampleRate +
                ", FFT Size " + mFFTSize +
                ", Samples per Update " + mSamplesPerUpdate +
                ", Buffer Read Size  " + mBufferSizeRead +
                ", Min Frequency " + mMinFrequency +
                ", Max Frequency " + mMaxFrequency +
                ", Min Amplitude " + mMinAmplitude +
                ", Max Amplitude " + mMaxAmplitude;
    }
}
