package com.example.android.fftplot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import java.util.Vector;

public class FFTView extends View {
    private Vector<Float> frequencyValues;
    private Vector<Float> amplitudeValues;

    private float mPaddingDp = 30.0f;
    private float mBorderXDp = 15.0f;
    private float mBorderYDp = 10.0f;

    private float mMinimumFrequency = 10.0f;
    private float mMaximumFrequency = 20000.0f;
    private float mMinimumAmplitude = 0.1f;
    private float mMaximumAmplitude = 1000.01f;
    private Boolean mActiveStatus = false;

    private Paint mPaintThin, mPaintGrid, mPaintBorder, mPaintSpectrum, mPaintText, mPaintTitle;

    public void setConfiguration(float minimumFrequency, float maximumFrequency, float minimumAmplitude, float maximumAmplitude) {
        mMinimumFrequency = minimumFrequency;
        mMaximumFrequency = maximumFrequency;
        mMinimumAmplitude = minimumAmplitude;
        mMaximumAmplitude = maximumAmplitude;

        invalidate();
    }

    void setActive(Boolean activeStatus) {
        mActiveStatus = activeStatus;
        invalidate();
    }

    public FFTView(Context context) {
        super(context);

        mPaintSpectrum = new Paint();
        mPaintSpectrum.setAntiAlias(true);
        mPaintSpectrum.setStyle(Paint.Style.STROKE);
        mPaintSpectrum.setColor(Color.BLUE);
        mPaintSpectrum.setStrokeWidth(3.0f);

        mPaintThin = new Paint();
        mPaintThin.setAntiAlias(true);
        mPaintThin.setStyle(Paint.Style.STROKE);
        mPaintThin.setColor(Color.BLACK);
        mPaintThin.setStrokeWidth(1.0f);

        mPaintBorder = new Paint();
        mPaintBorder.setAntiAlias(true);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setColor(Color.BLACK);
        mPaintBorder.setStrokeWidth(2.0f);

        mPaintGrid = new Paint();
        mPaintGrid.setAntiAlias(true);
        mPaintGrid.setStyle(Paint.Style.STROKE);
        mPaintGrid.setPathEffect(new DashPathEffect(new float[] {5, 10}, 0));
        mPaintGrid.setColor(Color.BLACK);
        mPaintGrid.setStrokeWidth(2.0f);

        mPaintText = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(pxFromDp(context, 12));

        mPaintTitle = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mPaintTitle.setColor(Color.BLACK);
        mPaintTitle.setTextSize(pxFromDp(context, 20));

    }

    void setData(Vector<Float> frequencyList, Vector<Float> amplitudeList) {
        frequencyValues = frequencyList;
        amplitudeValues = amplitudeList;

        // Triggers redrawing of the canvas.
        invalidate();
    }

    // Obviously there is a lot of computation in the below methods that is repeated.
    // However, the computational effort is small compared to the FFT computation.

    private float mapFreqtoPx(float freq) {
        float paddingPx = pxFromDp(getContext(), mPaddingDp);
        float borderXPx = pxFromDp(getContext(), mBorderXDp);
        float plotStartX = paddingPx + borderXPx;
        float canvasWidth = getRight() - getLeft();
        float plotWidth = canvasWidth - 2*paddingPx - borderXPx;

        float freqLog = (float) Math.log10(freq);
        float minimumFrequencyLog = (float) Math.log10(mMinimumFrequency);
        float maximumFrequencyLog = (float) Math.log10(mMaximumFrequency);

        return plotStartX + plotWidth * (freqLog - minimumFrequencyLog) / (maximumFrequencyLog - minimumFrequencyLog);
    }

    private float mapAmpltoPx(float ampl) {
        float paddingPx = pxFromDp(getContext(), mPaddingDp);
        float borderYPx = pxFromDp(getContext(), mBorderYDp);
        float plotStartY = getTop() + paddingPx;
        float canvasHeight = getBottom() - getTop();
        float plotHeight = canvasHeight - 2*paddingPx - borderYPx;

        float amplLog = (float) Math.log10(ampl);
        float minimumAmplitudeLog = (float) Math.log10(mMinimumAmplitude);
        float maximumAmplitudeLog = (float) Math.log10(mMaximumAmplitude);

        return plotStartY + plotHeight * (1.0f - (amplLog - minimumAmplitudeLog) / (maximumAmplitudeLog - minimumAmplitudeLog));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float minimumAmplitudeLog = (float) Math.log10(mMinimumAmplitude);
        float maximumAmplitudeLog = (float) Math.log10(mMaximumAmplitude);
        float minimumFrequencyLog = (float) Math.log10(mMinimumFrequency);
        float maximumFrequencyLog = (float) Math.log10(mMaximumFrequency);
        float paddingPx = pxFromDp(getContext(), mPaddingDp);

        super.onDraw(canvas);

        canvas.drawRect(mapFreqtoPx(mMinimumFrequency), mapAmpltoPx(mMinimumAmplitude),
                        mapFreqtoPx(mMaximumFrequency), mapAmpltoPx(mMaximumAmplitude), mPaintBorder);

        for (int freqLog = (int)Math.floor(Math.log10(mMinimumFrequency)); freqLog < Math.ceil(Math.log10(mMaximumFrequency)); freqLog++) {
            for (int df = 1; df < 10; df++) {
                float freq = df * (float) Math.pow(10.0, (double) freqLog);
                float freqPx = mapFreqtoPx(freq);

                if (df == 1) {
                    canvas.drawLine(freqPx, mapAmpltoPx(mMinimumAmplitude), freqPx, mapAmpltoPx(mMaximumAmplitude), mPaintThin);

                    mPaintText.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(String.format("%d Hz", (int)Math.pow(10.0, (double)freqLog)), freqPx, getBottom() - paddingPx, mPaintText);
                } else {
                    if (freq >= mMinimumFrequency && freq <= mMaximumFrequency) {
                        canvas.drawLine(freqPx, mapAmpltoPx(mMinimumAmplitude), freqPx, mapAmpltoPx(mMaximumAmplitude), mPaintGrid);
                    }
                }
            }
        }

        for (int amplLog = (int)Math.floor(Math.log10(mMinimumAmplitude)); amplLog < Math.ceil(Math.log10(mMaximumAmplitude)); amplLog++) {
            for (int df = 1; df < 10; df++) {
                float ampl = df * (float) Math.pow(10.0, (double) amplLog);
                float amplPx = mapAmpltoPx(ampl);

                if (df == 1) {
                    canvas.drawLine(mapFreqtoPx(mMinimumFrequency), amplPx, mapFreqtoPx(mMaximumFrequency), amplPx, mPaintThin);

                    mPaintText.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(String.format("%d dB ", (int) amplLog * 20), mapFreqtoPx(mMinimumFrequency), amplPx, mPaintText);
                } else {
                    if (ampl >= mMinimumAmplitude && ampl <= mMaximumAmplitude) {
                        canvas.drawLine(mapFreqtoPx(mMinimumFrequency), amplPx, mapFreqtoPx(mMaximumFrequency), amplPx, mPaintGrid);
                    }
                }
            }
        }

        // Draw FFT Data:
        canvas.save();
        canvas.clipRect(mapFreqtoPx(mMinimumFrequency), mapAmpltoPx(mMaximumAmplitude),
                        mapFreqtoPx(mMaximumFrequency), mapAmpltoPx(mMinimumAmplitude));
        if (frequencyValues != null && amplitudeValues != null) {
            Path path = new Path();

            for (int indValue = 0; indValue < frequencyValues.size(); indValue++) {
                float x = mapFreqtoPx(frequencyValues.get(indValue));
                float y = mapAmpltoPx(amplitudeValues.get(indValue));

                if (x <= 0.0f) x = 0.0f;
                if (y <= 0.0f) y = 0.0f;

                if (indValue == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            canvas.drawPath(path, mPaintSpectrum);
        }
        // Draw Status Labels
        canvas.restore();
        mPaintTitle.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Active=" + mActiveStatus , mapFreqtoPx(mMinimumFrequency), paddingPx * 0.66f, mPaintTitle);
        mPaintTitle.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Swipe left for configuration" , mapFreqtoPx(mMaximumFrequency), paddingPx * 0.66f, mPaintTitle);
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
