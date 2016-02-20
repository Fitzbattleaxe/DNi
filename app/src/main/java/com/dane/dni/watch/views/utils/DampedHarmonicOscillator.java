package com.dane.dni.watch.views.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dane on 8/26/2015.
 */
public class DampedHarmonicOscillator {
    float x0;
    float w0;
    float v0;
    float z;
    float a;
    float b;
    float wd;

    public DampedHarmonicOscillator(float x0, float w0, float v0, float z) {
        this.x0 = x0;
        this.w0 = w0;
        this.v0 = v0;
        this.z = z;

        if (z == 1.0f) {
            a = x0;
            b = v0 + w0*x0;
        } else if (z < 1.0f) {
            a = x0;
            wd = (float) (w0*Math.sqrt(1 - Math.pow(z, 2)));
            b = 1 / wd * (z * w0 * x0 + v0);
        } else {
            throw new RuntimeException("Over-damping not supported");
        }
    }

    public float x(int t) {
        if (z == 1.0f) {
            return (float) ((a + b*t)*Math.exp(-w0*t));
        } else if (z < 1.0f) {
            return (float) (Math.exp(-z*w0*t)*(a*Math.cos(wd*t) + b*Math.sin(wd*t)));
        } else {
            throw new RuntimeException("Over-damping not supported");
        }
   }

    public Float[] getCachedFunctionValues(int tStart, int tEnd) {
        Float[] rv = new Float[tEnd - tStart + 1];
        for (int t = tStart; t <= tEnd - 1; t ++) {
            float val = x(t);
            rv[t] = val;
        }
        rv[tEnd] = 0.0f;
        return rv;
    }
}
