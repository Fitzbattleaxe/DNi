package com.dane.dni;

import com.dane.dni.watch.views.utils.DampedHarmonicOscillator;

import org.junit.Test;

/**
 * Created by Dane on 8/26/2015.
 */
public class DampedHarmonicOscillatorTest {

    @Test
    public void testOscillator() {
        DampedHarmonicOscillator oscillator =
                new DampedHarmonicOscillator(-1.0f, 1.0f / 100, 1.0f / 200, 0.5f);
        Float[] functionValues = oscillator.getCachedFunctionValues(0, 2000);
    }
}
