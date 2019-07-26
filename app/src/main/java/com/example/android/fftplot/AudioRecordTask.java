package com.example.android.fftplot;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.Vector;

public class AudioRecordTask extends AsyncTask <Void, float[], Boolean>{
    private WeakReference<MainActivity> mMainActivity;
    private int mAudioSource;
    private int mSampleRate;
    private int mBufferSizeRead;
    private int mSamplesPerUpdate;
    private int mFFTSize;

    AudioRecordTask(int audioSource,
                    int sampleRate,
                    int bufferSizeRead,
                    int samplesPerUpdate,
                    int FFTSize,
                    MainActivity mainActivity) {
        mMainActivity = new WeakReference<>(mainActivity);
        mAudioSource = audioSource;
        mSampleRate = sampleRate;
        mBufferSizeRead = bufferSizeRead;
        mSamplesPerUpdate = samplesPerUpdate;
        mFFTSize = FFTSize;
    }

    // Runs in a separate thread.
    @Override
    protected Boolean doInBackground(Void... voids) {
        final int channelMask = AudioFormat.CHANNEL_IN_MONO;
        final int encoding = AudioFormat.ENCODING_PCM_FLOAT;
        int numSamples = 0;

        int bufferSizeInt = AudioRecord.getMinBufferSize(mSampleRate, channelMask, encoding);

        // Recording parameters are not supported by the hardware or an invalid parameter was passed.
        if (bufferSizeInt == AudioRecord.ERROR_BAD_VALUE) {
            return false;
        }
        bufferSizeInt *= 2;

        // It is unclear whether getMinBufferSize can return small values.
        if (bufferSizeInt < 2 * mBufferSizeRead) {
            bufferSizeInt = 2 * mBufferSizeRead;
        }

        AudioRecord audioRecord = new AudioRecord(mAudioSource, mSampleRate, channelMask, encoding,
                bufferSizeInt);

        // Invalid parameters or failure to acquire hardware resources.
        if (audioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
            return false;
        }

        float[] readBuffer = new float[mBufferSizeRead];
        float[] ringBuffer = new float[mFFTSize];
        int ringBufferIndex = 0;
        Complex[] FFTBuffer = new Complex[mFFTSize];

        audioRecord.startRecording();

        while (mMainActivity.get().getRecordingStatus()) {
            int numRead = audioRecord.read(readBuffer, 0, readBuffer.length, AudioRecord.READ_BLOCKING);

            for (int indRead = 0; indRead < numRead; indRead++) {
                ringBuffer[ringBufferIndex] = readBuffer[indRead];

                numSamples++;
                ringBufferIndex = ((ringBufferIndex + 1) % mFFTSize);

                // mSamplesPerUpdate samples collected. Let's perform FFT:
                if (numSamples == mSamplesPerUpdate) {
                    numSamples = 0;

                    for (int indArray = 0; indArray < mFFTSize; indArray++) {
                        FFTBuffer[indArray] = new Complex(ringBuffer[indArray], 0);
                    }
                    FastFourierTransform.fft(FFTBuffer);

                    // Interpret the FFT data as a list of frequencies and amplitudes.
                    float[] freqList = new float[mFFTSize / 2];
                    float[] amplList = new float[mFFTSize / 2];

                    for (int indArray = 0; indArray < mFFTSize / 2; indArray++) {
                        freqList[indArray] = ((float) mSampleRate) * ((float) indArray) / ((float) mFFTSize);
                        amplList[indArray] = (float) FFTBuffer[indArray].abs();
                    }
                    // Transmit data to the UI thread.
                    publishProgress(freqList, amplList);
                }
            }
        }

        audioRecord.stop();
        return true;
    }

    // In the UI thread, transmit FFT data to the MainActivity.
    protected void onProgressUpdate(float[]... vectors) {
        Vector<Float> freqList= new Vector<Float>();
        Vector<Float> amplList= new Vector<Float>();

        for (int indArray = 0; indArray < vectors[0].length; indArray++) {
            freqList.add(new Float(vectors[0][indArray]));
            amplList.add(new Float(vectors[1][indArray]));
        }
        mMainActivity.get().receiveData(freqList, amplList);
    }
}
