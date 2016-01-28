package com.thesaan.gameengine.android.opengl.shapes;

import android.opengl.GLES20;

import com.thesaan.gameengine.android.handler.MathHandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Michael on 31.12.2015.
 */
public class Vertex extends Shape {


    public Vertex(float[] map_coordinates) {
        super(map_coordinates);
    }

    public Vertex(float x, float y, float z) {
        super(x, y, z);
    }


    public void draw(float[] mvpMatrix) {

        super.draw(mvpMatrix, GLES20.GL_POINTS);

    }


}
