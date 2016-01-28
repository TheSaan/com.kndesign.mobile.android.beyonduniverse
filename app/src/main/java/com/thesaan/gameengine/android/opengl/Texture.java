package com.thesaan.gameengine.android.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Michael on 18.01.2016.
 */
public class Texture {

    Bitmap bm;
    private FloatBuffer textureBuffer;

    private int texture[] = new int[1];

    public Texture(Context context, int resId, GL10 gl) {
        InputStream is = context.getResources().openRawResource(resId);

        try {
            bm = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        //convert into gl texture
        gl.glGenTextures(1,texture,0);

//        gl.glBindTexture(GL10.GL_TEXTURE_2D );
    }
}
