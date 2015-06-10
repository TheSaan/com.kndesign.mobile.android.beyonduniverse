package com.thesaan.gameengine.android.handler;

import java.util.Random;

/**
 * Created by Michael Knöfler on 09.03.2015.
 */
public class RandomHandler {
    public static int createIntegerFromRange(int aStart, int aEnd, Random aRandom){
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long)aEnd - (long)aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * aRandom.nextDouble());
        int randomNumber =  (int)(fraction + aStart);
        return randomNumber;
    }

    public static double createDoubleFromRange(double start, double end, Random aRandom){
        double range = end - start;
        double scaled = aRandom.nextDouble() * range;
        double shifted = scaled + start;
        return shifted; // == (rand.nextDouble() * (max-min)) + min;
    }
    public static float createFloatFromRange(float start, float end, Random aRandom){
        float range = end - start;
        float scaled = aRandom.nextFloat() * range;
        float shifted = scaled + start;
        return shifted; // == (rand.nextDouble() * (max-min)) + min;
    }
}
