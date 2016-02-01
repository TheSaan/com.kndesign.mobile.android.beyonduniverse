package com.thesaan.gameengine.android.opengl.text;

import android.content.res.AssetManager;

import com.thesaan.gameengine.android.opengl.text.lib.GLText;


/**
 * Created by Michael on 29.01.2016.
 */
public class GameFont extends GLText {
    private final String ROBOTO_REG = "Roboto-Regular.ttf";
    private int letterPaddingX = 2;
    private int letterPaddingY = 2;
    private int fontSize = 14;

    private final float RED = 0.5f;
    private final float GREEN = 0.5f;
    private final float BLUE = 0.5f;
    private final float ALPHA = 1f;

    private float[] projectionMatrix = new float[16];

    public GameFont(AssetManager assets) {
        super(assets);

        // Create Font (Height: 14 Pixels / X+Y Padding 2 Pixels)
        load(ROBOTO_REG, fontSize, letterPaddingX, letterPaddingY);
    }

    /**
     * @param text
     * @param fontSize
     */
    public void write(String text, int fontSize, float x, float y, float z, float angle) {
        if (fontSize != this.fontSize) {
            load(ROBOTO_REG, fontSize, letterPaddingX, letterPaddingY);
        }

        write(text, x, y, z, angle);
    }

    /**
     * @param text
     */
    public void write(String text, float x, float y, float z, float angle) {
        begin(RED, GREEN, BLUE, ALPHA, projectionMatrix);
        drawC(text, x, y, z, angle);
        end();
    }

    public void setProjectionMatrix(float[] projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }
}
