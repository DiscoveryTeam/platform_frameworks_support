/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v7.graphics;

import java.util.Arrays;

/**
 * Class which provides a histogram for RGB values.
 */
final class ColorHistogram {

    private final int[] mColors;
    private final int[] mColorCounts;
    private final int mNumberColors;

    /**
     * A new {@link ColorHistogram} instance.
     *
     * @param imagePixels array of image contents
     */
    ColorHistogram(final int[] imagePixels) {
        // Lets take a copy of the original pixels
        final int[] pixels = new int[imagePixels.length];
        System.arraycopy(imagePixels, 0, pixels, 0, pixels.length);

        // Sort the pixels to enable counting below
        Arrays.sort(pixels);

        mNumberColors = countDistinctColors(pixels);

        // Create arrays
        mColors = new int[mNumberColors];
        mColorCounts = new int[mNumberColors];

        countFrequencies(pixels);
    }

    /**
     * @return number of distinct colors in the image.
     */
    int getNumberOfColors() {
        return mNumberColors;
    }

    /**
     * @return an array containing all of the distinct colors in the image.
     */
    int[] getColors() {
        return mColors;
    }

    /**
     * @return an array containing the frequency of a distinct colors within the image.
     */
    int[] getColorCounts() {
        return mColorCounts;
    }

    static int countDistinctColors(final int[] pixels) {
        if (pixels.length == 0) {
            return 0;
        }

        // If we have at least 1 pixel, we have a minimum of 1 color
        int colorCount = 1;
        int currentColor = pixels[0];

        // Now iterate from the second pixel to the end, population distinct colors
        for (int i = 1; i < pixels.length; i++) {
            // If we encounter a new color, increase the population
            if (pixels[i] != currentColor) {
                currentColor = pixels[i];
                colorCount++;
            }
        }

        return colorCount;
    }

    private void countFrequencies(final int[] pixels) {
        if (pixels.length == 0) {
            return;
        }

        int currentColorIndex = 0;
        int currentColor = pixels[0];

        mColors[currentColorIndex] = currentColor;
        mColorCounts[currentColorIndex] = 1;

        // Now iterate from the second pixel to the end, population distinct colors
        for (int i = 1; i < pixels.length; i++) {
            if (pixels[i] == currentColor) {
                // We've hit the same color as before, increase population
                mColorCounts[currentColorIndex]++;
            } else {
                // We've hit a new color, increase index
                currentColor = pixels[i];

                currentColorIndex++;
                mColors[currentColorIndex] = currentColor;
                mColorCounts[currentColorIndex] = 1;
            }
        }
    }

}
