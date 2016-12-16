package com.intrusoft.library;

import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;

/**
 * Created by Intruder Shanky on 12/16/2016.
 */

public class GradientGenerator {

    public static Shader getRadialGradient(float width, float height, int startColor, int endColor) {
        float radius = Math.max(width, height) / 2;
        return new RadialGradient(width / 2, height / 2, radius, startColor, endColor, Shader.TileMode.CLAMP);
    }

    public static Shader getLinearGradient(int angle, float width, float height, int startColor, int endColor) {
        float x1 = 0, y1 = 0, y2 = 0;
        float x2 = (float) (height / Math.tan(angle));
        if (angle <= 180) {
            y1 = height;
            if (angle > 90)
                x1 = width;
        } else if (angle <= 360) {
            y2 = height;
            if (angle <= 270)
                x1 = width;
        }
        return new LinearGradient(x1, y1, x2, y2, startColor, endColor, Shader.TileMode.CLAMP);
    }

}
