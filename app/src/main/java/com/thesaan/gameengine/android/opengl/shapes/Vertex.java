package com.thesaan.gameengine.android.opengl.shapes;

import android.opengl.GLES20;

import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.opengl.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Michael on 31.12.2015.
 */
public class Vertex extends Shape {

    private FloatBuffer vertexBuffer;


    int screenWidth;
    int screenHeight;

    float pointCoords[];   // in counterclockwise order:

    public MathHandler.Vector mVector; //for the own MathHandler class

//    private final String vertexShaderCode =
//            "attribute vec4 vPosition;" +
//                    "void main() {" +
//                    "  gl_Position = vPosition;" +
//                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    private int mProgram;

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    public Vertex(float[] map_coordinates) {
        super();

        pointCoords = new float[map_coordinates.length];
        copyCoordinates(pointCoords, map_coordinates);
        vertexCount = pointCoords.length / COORDS_PER_VERTEX;

        init();
    }

    public Vertex(float x, float y, float z) {
        super(x, y, z);
        pointCoords = new float[]{x, y, z};
        vertexCount = pointCoords.length / COORDS_PER_VERTEX;
        init();
    }


    public void draw(float[] mvpMatrix) { // pass in the calculated transformation matrix

        updateBuffer();

        super.draw(mvpMatrix, GLES20.GL_POINTS);

    }

    /**
     * Set the current screen dimension
     *
     * @param width
     * @param height
     */
    public void setDimension(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public void convertToScreenPercentage() {
        MathHandler.Matrix m = new MathHandler.Matrix();
        m.setScreenDimension(screenWidth, screenHeight);

        for (int i = 0; i < pointCoords.length; i += 3) {
            for (int k = 0; k < 3; k++) {

                switch (k) {
                    case 0://x
                        pointCoords[i + k] = (float) m.getWidthPercentage(pointCoords[i + k], screenWidth) / 100;
                        break;
                    case 1://y
                        pointCoords[i + k] = (float) m.getHeightPercentage(pointCoords[i + k], screenHeight) / 100;
                        break;
                    case 2://z
                        pointCoords[i + k] = (float) m.getWidthPercentage(pointCoords[i + k], screenWidth * 2) / 100;
                        break;
                }

            }
        }
    }

    /**
     * Setup the vertexBuffer with the new Coordinates
     */
    public void updateBuffer() {

        //reset pointCoords from Mathhandler.Vektor
//        mVector.definePositionAsScreenPercentage();

//        applyVectorTranslation();

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                pointCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

//        vertexBuffer = null;

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(pointCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }

    /**
     * @return
     */
    public float[] getPointCoords() {
        return pointCoords;
    }

    public void setPointCoords(float[] coords) {
        this.pointCoords = coords;
    }
}
