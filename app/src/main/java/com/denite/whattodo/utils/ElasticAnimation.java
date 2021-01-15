package com.denite.whattodo.utils;

public class ElasticAnimation implements android.view.animation.Interpolator
{
    private double mAmplitude = 1;
    private double mFrequency = 10;

    public ElasticAnimation(double amplitude, double frequency)
    {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time)
    {
        return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) * Math.cos(mFrequency * time) + 1);
    }
}