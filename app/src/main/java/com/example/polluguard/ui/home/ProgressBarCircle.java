package com.example.polluguard.ui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.polluguard.R;

public class ProgressBarCircle extends View {

    private final int MAX_AQI = 300;

    private Paint backgroundCircle;
    private Paint progressCircle;

    private int progressColor;

    private float aqiPercentage = 0;

    public ProgressBarCircle(Context context) {
        super(context);
        initialize();
    }

    public ProgressBarCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize(){

        // REFACTORINGGGGG!!!
        backgroundCircle = new Paint();
        backgroundCircle.setColor(ContextCompat.getColor(getContext(), R.color.gray));
        backgroundCircle.setStyle(Paint.Style.STROKE);
        backgroundCircle.setStrokeWidth(35);
        backgroundCircle.setAntiAlias(true);

        // Paint for progress circle
        progressCircle = new Paint();
        progressCircle.setColor(ContextCompat.getColor(getContext(), R.color.gray)); // default the progress bar is empty
        progressCircle.setStyle(Paint.Style.STROKE);
        progressCircle.setStrokeWidth(35);
        progressCircle.setAntiAlias(true);
        progressCircle.setStrokeCap(Paint.Cap.ROUND);
    }



    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float radius = Math.min(width, height) / 2f - 20;

        canvas.drawCircle(width / 2f, height / 2f, radius, backgroundCircle);

        float sweepAngle = (aqiPercentage / 100f) * 360;
        canvas.drawArc(
                20, // Left
                20, // Top
                width - 20, // Right
                height - 20, // Bottom
                -90, // Start angle (top center)
                sweepAngle, // Sweep angle
                false, // Don't fill the arc
                progressCircle
        );
    }

    public void setCircleColor(int colorRes){
        this.progressColor = ContextCompat.getColor(getContext(), colorRes);
        progressCircle.setColor(progressColor);
        invalidate();
    }

    public void setAqiPercentage(float aqi) {
        this.aqiPercentage = aqiToAqiPercentage(aqi);
        Log.i("AQI PERCENTAGE = ", "aqi persen = " + aqiPercentage);
        invalidate();
    }

    public float aqiToAqiPercentage(float aqi){
        Log.i("TESTTT = ", "aqi berapa > " + aqi + "coba ya = " +  aqi/MAX_AQI*100);
        return aqi/MAX_AQI*100; // maksimal aqi seharusnya adalah 300
    }
}
