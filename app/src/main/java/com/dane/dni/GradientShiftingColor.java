package com.dane.dni;

import android.graphics.Color;
import android.graphics.Shader;

/**
 * Created by Dane on 10/12/2015.
 */
public class GradientShiftingColor {
    int[] colors;
    float[] positions;
    boolean useLargerUnitPosition;

    public GradientShiftingColor(int[] colors, float[] positions, boolean useLargerUnitPosition) {
        this.colors = colors;
        this.positions = positions;
        this.useLargerUnitPosition = useLargerUnitPosition;
    }

    public boolean useLargerUnitPosition() {
        return useLargerUnitPosition;
    }

    public int getColor(float position) {
        int color1 = colors[0];
        int color2 = colors[0];
        float position1 = 0.0f;
        float position2 = 0.0f;

        for (int i = 0; i < positions.length; i++) {
            position1 = position2;
            position2 = positions[i];
            color1 = color2;
            color2 = colors[i];
            if (position2 > position) {
                break;
            }
        }

        if (position > position2) {
            return color2;
        }

        float ratio = (position - position1) / (position2 - position1);
        return Color.rgb(
                (int) (Color.red(color1)*(1 - ratio) + Color.red(color2)*ratio),
                (int) (Color.green(color1)*(1 - ratio) + Color.green(color2)*ratio),
                (int) (Color.blue(color1)*(1 - ratio) + Color.blue(color2)*ratio));
    }
}
